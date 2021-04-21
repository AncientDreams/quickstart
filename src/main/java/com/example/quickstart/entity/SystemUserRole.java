package com.example.quickstart.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  系统用户对应的角色实体类
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-07-13
 */
@TableName("system_user_role")
@Data
public class SystemUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_role_id", type = IdType.AUTO)
    private Integer userRoleId;

    @TableField("user_id")
    private Integer userId;

    @TableField("role_id")
    private Integer roleId;
}
