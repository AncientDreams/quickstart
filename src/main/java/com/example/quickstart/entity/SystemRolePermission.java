package com.example.quickstart.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  系统角色对应的权限实体类
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-07-13
 */
@TableName("system_role_permission")
@Data
public class SystemRolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "role_permission_id", type = IdType.AUTO)
    private Integer rolePermissionId;

    @TableField("role_id")
    private Integer roleId;

    @TableField("permission_id")
    private Integer permissionId;
}
