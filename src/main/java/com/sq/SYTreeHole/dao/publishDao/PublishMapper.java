package com.sq.SYTreeHole.dao.publishDao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sq.SYTreeHole.entity.Publish;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;


public interface PublishMapper extends BaseMapper<Publish> {

    @Select("select * from th_publish order by modify_time DESC limit #{start}, #{end}")
    List<Publish> publishesAsTime(@Param("start") Serializable start, @Param("end") Serializable end);


    @Select("select * from th_publish order by (visits+th_publish.star*3+th_publish.comments_number*6)/10 DESC limit #{start}, #{end}")
    List<Publish> publishesAsHot(@Param("start") Serializable start, @Param("end") Serializable end);

    //TODO 可能追加
}
