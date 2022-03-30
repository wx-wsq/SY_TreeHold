package com.sq.SYTreeHole.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.Date;

@ApiModel("user-entity")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class User {
    @TableId
    private String id;
    private String username;
    @TableField(select = false)
    private String password;
    private String name;
    private String head;
    private Date create_time;
    private String stuId;
    private Integer sex;
    private Date birthday;
    @Version
    private Integer version;
    @TableLogic
    private Integer isDelete;
}
