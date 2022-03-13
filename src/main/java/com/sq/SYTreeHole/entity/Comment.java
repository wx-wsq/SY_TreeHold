package com.sq.SYTreeHole.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.Date;

@ApiModel("comment-entity")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Comment {
    private String id;
    private String userId;
    private String publishId;
    private Integer star;
    private String text;
    private Date createTime;
    @Version
    private Integer version;
    @TableLogic
    private Integer isDelete;
}
