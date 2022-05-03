package com.sq.SYTreeHole.dao.publishDao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sq.SYTreeHole.entity.Notice;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;
import java.util.List;

public interface NoticeMapper extends BaseMapper<Notice> {


    @Results({
            @Result(column = "user_id",property = "user",
                    one = @One(select = "com.sq.SYTreeHole.dao.UserDao.UserMapper.selectById")),
            @Result(column = "publish_id",property = "publish",
                    one = @One(select = "com.sq.SYTreeHole.dao.publishDao.PublishDetailMapper.getById"))
    })
    @Select("select * from th_notice where publish_user_id = #{userId}")
    List<Notice> findMyNotices(Serializable userId);
}
