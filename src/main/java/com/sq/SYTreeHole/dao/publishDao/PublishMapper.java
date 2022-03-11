package com.sq.SYTreeHole.dao.publishDao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sq.SYTreeHole.entity.Publish;
import org.apache.ibatis.annotations.*;
import java.io.Serializable;
import java.util.List;


public interface PublishMapper extends BaseMapper<Publish> {

    @Results({@Result(column = "user_id",property = "user",one=@One(select = "com.sq.SYTreeHole.dao.UserDao.UserMapper.selectById"))})
    @Select("select * from th_publish order by modify_time DESC limit #{start}, #{end}")
    List<Publish> publishesAsTime(@Param("start") Serializable start, @Param("end") Serializable end);


    @Results({@Result(column = "user_id",property = "user",one=@One(select = "com.sq.SYTreeHole.dao.UserDao.UserMapper.selectById"))})
    @Select("select * from th_publish order by (visits+th_publish.star*3+th_publish.comments_number*6)/10 DESC limit #{start}, #{end}")
    List<Publish> publishesAsHot(@Param("start") Serializable start, @Param("end") Serializable end);

    //TODO 可能追加
}
