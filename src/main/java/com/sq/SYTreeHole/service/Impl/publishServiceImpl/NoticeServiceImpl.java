package com.sq.SYTreeHole.service.Impl.publishServiceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sq.SYTreeHole.dao.publishDao.NoticeMapper;
import com.sq.SYTreeHole.entity.Notice;
import com.sq.SYTreeHole.exception.NoticeException;
import com.sq.SYTreeHole.service.publishService.NoticeService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper,Notice> implements NoticeService {
    @Override
    public List<Notice> findMyNotices(String userId) {
        return getBaseMapper().findMyNotices(userId);
    }

    @Override
    public void setMyNotice(Notice notice) {
        if(notice.getIndex() == 0)
            notice.setText("赞了你的动态");
        else if(notice.getIndex() == 1)
            notice.setText("评论了你的动态");
        else
            throw new NoticeException("Index异常");
        save(notice);
    }
}
