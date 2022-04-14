package com.sq.SYTreeHole.service.Impl.publishServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sq.SYTreeHole.Utils.RedisUtils;
import com.sq.SYTreeHole.Utils.SentimentAnalysisUtils.EmotionalAnalysis;
import com.sq.SYTreeHole.dao.publishDao.PublishImagesMapper;
import com.sq.SYTreeHole.dao.publishDao.PublishManagementMapper;
import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.entity.PublishImages;
import com.sq.SYTreeHole.exception.ManagementPublishException;
import com.sq.SYTreeHole.service.publishService.PublishManagementService;
import org.apache.logging.log4j.util.Strings;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@SuppressWarnings("all")
public class PublishManagementServiceImpl extends ServiceImpl<PublishManagementMapper, Publish> implements PublishManagementService {

    @Resource
    private PublishImagesMapper publishImagesMapper;

    @Transactional
    @Override
    public boolean insert(Publish publish, HttpServletRequest httpServletRequest) {
        if (Objects.isNull(publish) || Strings.isBlank(publish.getUserId()) || Strings.isBlank(publish.getText()))
            throw new ManagementPublishException("空参异常");
        double mark = reckonMark(publish);
        publish.setMark(mark);
        if (save(publish)) {
            if (ServletFileUpload.isMultipartContent(httpServletRequest)) {
                List<MultipartFile> images = ((MultipartHttpServletRequest) httpServletRequest).getFiles("images");
                saveImages(images, publish);
            }
            RedisUtils.setPublishCache(publish);
            return true;
        } else
            throw new ManagementPublishException("新增失败");
    }

    @Transactional
    @Override
    public boolean modify(Publish publish, HttpServletRequest httpServletRequest) {
        if (Objects.isNull(publish) || Strings.isBlank(publish.getUserId()))
            throw new ManagementPublishException("空参异常");
        QueryWrapper<Publish> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("id", publish.getId())
                .eq("user_id", publish.getUserId());
        if (Objects.isNull(getOne(queryWrapper)))
            throw new ManagementPublishException("系统错误...");
        double mark = reckonMark(publish);
        publish.setMark(mark);
        if (updateById(publish)) {
            RedisUtils.setPublishCache(publish);
            RedisUtils.delPublishImageCache(publish.getId());
            if (ServletFileUpload.isMultipartContent(httpServletRequest)) {
                QueryWrapper<PublishImages> publishImagesQueryWrapper = new QueryWrapper<>();
                publishImagesQueryWrapper.eq("publish_id", publish.getId());
                List<PublishImages> publishImages = publishImagesMapper.selectList(publishImagesQueryWrapper);
                publishImagesMapper.deleteBatchIds(publishImages);
                delImages(publishImages);
                List<MultipartFile> multipartFiles = ((MultipartHttpServletRequest) httpServletRequest).getFiles("images");
                saveImages(multipartFiles, publish);
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
            delImages(publishImages);
            RedisUtils.delPublishCache(publishId);
            RedisUtils.delPublishImageCache(publishId);
            return true;
        } else
            return false;
    }

    @Override
    public List<Publish> selectMyPublish(String userId, String page) {
        if (Strings.isBlank(userId) || Strings.isBlank(page))
            throw new ManagementPublishException("空参异常");
        IPage<Publish> iPage = new Page<>(Long.parseLong(page), 10);
        QueryWrapper<Publish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return getBaseMapper().selectPage(iPage, queryWrapper).getRecords();
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
            //TODO 更改路径
            File file = new File("D:/image/" + saveName);
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
                    //TODO 保存地址需更改
                    new PublishImages()
                            .setSaveName(file.getName())
                            .setUrl("http://localhost/image/" + saveName)
                            .setPublishId(publish.getId()));
        }
    }

    private void delImages(List<PublishImages> publishImages) {
        for (PublishImages publishImage : publishImages) {
            //TODO 更改路径
            File file = new File("D:/image/" + publishImage.getSaveName());
            if (file.exists())
                file.delete();
        }
    }

    private double reckonMark(Publish publish) {
        publish.setText(publish.getText().replaceAll("[A-Za-z]",""));
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
