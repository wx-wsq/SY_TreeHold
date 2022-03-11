package com.sq.SYTreeHole.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private String id;
    private String userId;
    private String publishId;
    private Integer star;
    private String text;
    private Date createTime;
    @TableLogic
    private Integer isDelete;
}
