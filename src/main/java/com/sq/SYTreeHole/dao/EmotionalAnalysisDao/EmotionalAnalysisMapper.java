package com.sq.SYTreeHole.dao.EmotionalAnalysisDao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sq.SYTreeHole.entity.Publish;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;
import java.util.List;

public interface EmotionalAnalysisMapper extends BaseMapper<Publish> {

    @Select("select mark from th_publish where user_id =#{userId} order by modify_time desc limit 1")
    Double lastMark(@Param("userId") Serializable userId);

    @Select("select mark*100 mark, modify_time from th_publish where user_id =#{userId} and date_sub(curdate(), interval 7 day)<= modify_time")
    List<Publish> allMarkForLineChart(@Param("userId") Serializable userId);

    @Select("select mark, " +
            "    IF(mark < 0.3, '1', " +
            "       IF(mark between 0.3 and 0.499, '2', " +
            "          IF(mark between 0.5 and 0.699, '3', " +
            "             IF(mark between 0.7 and 1, '4', 0)))) as id " +
            "from th_publish where user_id = #{userId}")
    List<Publish> allMarkForPieChart(@Param("userId") Serializable userId);


}
