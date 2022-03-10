package com.sq.SYTreeHole.entity;


import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Publish {

    @TableLogic
    private String id;
    private String userId;
    private String title;
    private String text;
    private Date createTime;
    private Date modifyTime;
    private String author;
    private Integer mark;
    private Integer visits;
    private Integer star;
    private Integer comments_number;
    @Version
    private Integer version;
    @TableLogic
    private Integer isDelete;
}