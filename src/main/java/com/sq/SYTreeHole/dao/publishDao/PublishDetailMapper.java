package com.sq.SYTreeHole.dao.publishDao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sq.SYTreeHole.entity.Publish;
import org.apache.ibatis.annotations.*;

public interface PublishDetailMapper extends BaseMapper<Publish> {


    @Results({
            @Result(column = "user_id",property = "user",one=@One(select = "com.sq.SYTreeHole.dao.UserDao.UserMapper.selectById")),
            @Result(column = "user_id",property = "userId")
    })
    @Select("select * from th_publish where #{publishId} = id")
    Publish getById(@Param("publishId") String publishId);

}
