package com.sq.SYTreeHole.service.Impl.publishServiceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sq.SYTreeHole.dao.publishDao.NoticeMapper;
import com.sq.SYTreeHole.entity.Notice;
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
        save(notice);
    }
}
