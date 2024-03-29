package com.sq.SYTreeHole.dao.publishDao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sq.SYTreeHole.entity.Publish;
import org.apache.ibatis.annotations.*;
import java.io.Serializable;
import java.util.List;


public interface PublishMapper extends BaseMapper<Publish> {

    @Results({
            @Result(column = "user_id",property = "user",one=@One(select = "com.sq.SYTreeHole.dao.UserDao.UserMapper.selectById")),
            @Result(column = "user_id",property = "userId")
    })
    @Select("select * from th_publish where" +
            "                               IF(#{index}=0,mark between 0 and 1," +
            "                                   IF(#{index}=1,mark between 0 and 0.299," +
            "                                       IF(#{index}=2,mark between 0.3 and 0.499," +
            "                                           IF(#{index}=3,mark between 0.5 and 0.699," +
            "                                               IF(#{index}=4,mark between 0.7 and 1, mark<0))))) and anonymity=0 and is_delete=0 " +
            "                                                   order by create_time DESC limit #{start}, #{end}")
    List<Publish> publishesAsTime(@Param("start") Serializable start, @Param("end") Serializable end, @Param("index") Serializable index);


    @Results({
            @Result(column = "user_id",property = "user",one=@One(select = "com.sq.SYTreeHole.dao.UserDao.UserMapper.selectById")),
            @Result(column = "user_id",property = "userId")
    })
    @Select("select * from th_publish where" +
            "                               IF(#{index}=0,mark between 0 and 1," +
            "                                   IF(#{index}=1,mark between 0 and 0.299," +
            "                                       IF(#{index}=2,mark between 0.3 and 0.499," +
            "                                           IF(#{index}=3,mark between 0.5 and 0.699," +
            "                                               IF(#{index}=4,mark between 0.7 and 1, mark<0))))) and anonymity=0 and is_delete=0" +
            "                                                   order by (visits+th_publish.star*3+th_publish.comments_number*6)/10 DESC limit #{start}, #{end}")
    List<Publish> publishesAsHot(@Param("start") Serializable start, @Param("end") Serializable end, @Param("index") Serializable index);

    //TODO 可能追加
}
