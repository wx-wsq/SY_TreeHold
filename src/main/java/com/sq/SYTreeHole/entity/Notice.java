package com.sq.SYTreeHole.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Notice implements Serializable {
    private String id;
    private String userId;
    private String publishId;
    private String publishUserId;
    private Date createTime;
    private String text;
    @TableLogic
    private Integer isDelete;
    @TableField(exist = false)
    private Integer index;
    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private Publish publish;

    public Notice(String userId, String publishId, String publishUserId, Integer index) {
        this.userId = userId;
        this.publishId = publishId;
        this.publishUserId = publishUserId;
        this.index = index;
    }
}
