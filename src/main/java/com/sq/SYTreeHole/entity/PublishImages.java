package com.sq.SYTreeHole.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@ApiModel("publishImage-entity")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PublishImages {
    private String id;
    private String url;
    @TableField(select = false)
    private String saveName;
    private String publishId;
}
