package com.sq.SYTreeHole.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User {
    @TableId
    private String id;
    private String username;
    @TableField(select = false)
    private String password;
    private String name;
    private Date create_time;
    private String stuId;
    private Integer sex;
    private Date birthday;
    @Version
    private Integer version;
    @TableLogic
    private Integer isDelete;
}
