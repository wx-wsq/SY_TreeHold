package com.sq.SYTreeHole.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
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
    private Integer commentsNumber;
    @TableField(exist = false)
    private User user;
    @Version
    private Integer version;
    @TableLogic
    private Integer isDelete;
}
