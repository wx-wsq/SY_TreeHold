package com.sq.SYTreeHole.service.Impl.publishServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sq.SYTreeHole.Utils.RedisUtils;
import com.sq.SYTreeHole.Utils.SentimentAnalysisUtils.EmotionalAnalysis;
import com.sq.SYTreeHole.dao.publishDao.PublishImagesMapper;
import com.sq.SYTreeHole.dao.publishDao.PublishManagementMapper;
import com.sq.SYTreeHole.dao.publishDao.commentsDao.CommentsMapper;
import com.sq.SYTreeHole.entity.Comment;
import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.entity.PublishImages;
import com.sq.SYTreeHole.exception.ManagementPublishException;
import com.sq.SYTreeHole.service.publishService.PublishManagementService;
import org.apache.logging.log4j.util.Strings;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("all")
public class PublishManagementServiceImpl extends ServiceImpl<PublishManagementMapper, Publish> implements PublishManagementService {

    @Resource
    private PublishImagesMapper publishImagesMapper;

    @Resource
    private CommentsMapper commentsMapper;

    @Transactional
    @Override
    public boolean insert(Publish publish, HttpServletRequest httpServletRequest) {
        if (Objects.isNull(publish) || Strings.isBlank(publish.getUserId()) || Strings.isBlank(publish.getText()))
            throw new ManagementPublishException("空参异常");
        Publish publishMark = new Publish();
        BeanUtils.copyProperties(publish,publishMark);
        double mark = reckonMark(publishMark);
        publish.setMark(mark);
        if (save(publish)) {
            if (ServletFileUpload.isMultipartContent(httpServletRequest)) {
                List<MultipartFile> images = ((MultipartHttpServletRequest) httpServletRequest).getFiles("images");
                saveImages(images, publish);
            }
            RedisUtils.setPublishCache(publish);
            RedisUtils.clearPublishListCacheOfId();
            return true;
        } else
            throw new ManagementPublishException("新增失败");
    }

    @Transactional
    @Override
    public boolean modify(Publish publishParam, HttpServletRequest httpServletRequest) {
        if (Objects.isNull(publishParam) || Strings.isBlank(publishParam.getUserId()))
            throw new ManagementPublishException("空参异常");
        QueryWrapper<Publish> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("id", publishParam.getId())
                .eq("user_id", publishParam.getUserId());
        if (Objects.isNull(getOne(queryWrapper)))
            throw new ManagementPublishException("系统错误...");
        Publish publish = new Publish();
        BeanUtils.copyProperties(publishParam,publish);
        double mark = reckonMark(publish);
        publishParam.setMark(mark);
        if (updateById(publishParam)) {
            RedisUtils.setPublishCache(publishParam);
            RedisUtils.delPublishImageCache(publishParam.getId());
            if (ServletFileUpload.isMultipartContent(httpServletRequest)) {
                QueryWrapper<PublishImages> publishImagesQueryWrapper = new QueryWrapper<>();
                publishImagesQueryWrapper.eq("publish_id", publishParam.getId());
                List<PublishImages> publishImages = publishImagesMapper.selectList(publishImagesQueryWrapper);
                publishImagesMapper.deleteBatchIds(publishImages);
                delImages(publishImages);
                List<MultipartFile> multipartFiles = ((MultipartHttpServletRequest) httpServletRequest).getFiles("images");
                saveImages(multipartFiles, publishParam);
            }
            return true;
        } else
            return false;
    }

    @Transactional
    @Override
    public boolean delete(String publishId, String userId) {
        QueryWrapper<Publish> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("id", publishId)
                .eq("user_id", userId);
        if (remove(queryWrapper)) {
            QueryWrapper<PublishImages> publishImagesQueryWrapper = new QueryWrapper<>();
            publishImagesQueryWrapper.eq("publish_id", publishId);
            List<PublishImages> publishImages = publishImagesMapper.selectList(publishImagesQueryWrapper);
            publishImagesMapper.delete(publishImagesQueryWrapper);
            QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
            commentQueryWrapper.eq("publish_id",publishId);
            commentsMapper.delete(commentQueryWrapper);
            delImages(publishImages);
            RedisUtils.delPublishCache(publishId);
            RedisUtils.delPublishImageCache(publishId);
            RedisUtils.clearPublishListCacheOfId();
            return true;
        } else
            return false;
    }

    @Override
    public Map<String,Object> countMyPublish(String userId){
        if(Strings.isBlank(userId))
            throw new ManagementPublishException("空参异常");
        QueryWrapper<Publish> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("user_id",userId)
                .groupBy("anonymity")
                .select("count(id) as result");
        List<Map<String, Object>> maps = listMaps(queryWrapper);
        HashMap<String, Object> map = new HashMap<>();
        String[] keys = new String[]{"count","anonymity"};
        for (int i = 0; i < maps.size(); i++)
            map.put(keys[i],maps.get(i).get("result"));
        map.putIfAbsent("anonymity",0L);
        map.put("count", (Long)map.get("anonymity")+(Long)map.get("count"));
        return map;
    }

    @Override
    public List<Publish> selectMyPublish(String userId, String page, String index) {
        if (Strings.isBlank(userId) || Strings.isBlank(page) || Strings.isBlank(index))
            throw new ManagementPublishException("空参异常");
        IPage<Publish> iPage = new Page<>(Long.parseLong(page), 10);
        QueryWrapper<Publish> queryWrapper = new QueryWrapper<>();
        if(!index.equals("-1"))
            queryWrapper.eq("anonymity",index);
        queryWrapper.eq("user_id", userId)
                    .orderByDesc("modify_time");
        List<Publish> records = getBaseMapper().selectPage(iPage, queryWrapper).getRecords();
        List<Publish> collect = records
                .stream()
                .map(v->RedisUtils.getPublishCache(v.getId()))
                .filter(v->v.getId()!=null)
                .collect(Collectors.toList());
        if(collect.size()<records.size()) {
            records.forEach(RedisUtils::setPublishCache);
            return records;
        }else
            return collect;
    }

    @Override
    public List<List<PublishImages>> selectMyPublishImage(List<Publish> publishes) {
        List<List<PublishImages>> imageList = new ArrayList<>();
        for (Publish publish : publishes) {
            List<PublishImages> publishImageCache = RedisUtils.getPublishImageCache(publish.getId());
            if (publishImageCache == null) {
                QueryWrapper<PublishImages> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("publish_id", publish.getId());
                List<PublishImages> publishImages = publishImagesMapper.selectList(queryWrapper);
                imageList.add(publishImages);
                RedisUtils.setPublishImagesCache(publishImages);
            } else
                imageList.add(publishImageCache);
        }
        return imageList;
    }

    private void saveImages(List<MultipartFile> images, Publish publish) {
        for (MultipartFile image : images) {
            if (image.isEmpty())
                throw new ManagementPublishException("图片上传失败");
            String imageName = image.getOriginalFilename();
            assert imageName != null;
            String suffix = imageName.substring(imageName.lastIndexOf('.'));
            String saveName = UUID.randomUUID() + suffix;
            File file = new File("/usr/images/" + saveName);
            File path = new File(file.getParent());
            if (!path.exists())
                path.mkdir();
            try {
                image.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("文件保存失败...");
            }
            publishImagesMapper.insert(
                    new PublishImages()
                            .setSaveName(file.getName())
                            .setUrl("http://101.43.249.71/images/" + saveName)
                            .setPublishId(publish.getId()));
        }
    }

    private void delImages(List<PublishImages> publishImages) {
        for (PublishImages publishImage : publishImages) {
            File file = new File("/usr/images/" + publishImage.getSaveName());
            if (file.exists())
                file.delete();
        }
    }

    private double reckonMark(Publish publish) {
        publish.setText(publish.getText().replaceAll("[A-Za-z0-9]",""));
        if(publish.getText().length() == 0)
            return 0.6;
        int length = publish.getText().length();
        double mark = 0.6;
        if (length > 100) {
            Map<Object, Object> markToMapStart = EmotionalAnalysis.getMarkToMap(publish.getText().substring(0, 100));
            Map<Object, Object> markToMapLast = EmotionalAnalysis.getMarkToMap(publish.getText().substring(length - 100, length));
            double mark1 = Math.round(((Map<String, Double>) markToMapStart.get("data")).get("score") * 100) / 100.0;
            double mark2 = Math.round(((Map<String, Double>) markToMapLast.get("data")).get("score") * 100) / 100.0;
            mark = (mark1 + mark2) / 2;
        } else {
            Map<Object, Object> markToMap = EmotionalAnalysis.getMarkToMap(publish.getText());
            mark = Math.round(((Map<String, Double>) markToMap.get("data")).get("score") * 100) / 100.0;
        }
        return mark;
    }
}
