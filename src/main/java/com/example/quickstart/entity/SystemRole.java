package com.example.quickstart.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 系统角色实体类
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-07-13
 */
@TableName("system_role")
@Data
public class SystemRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色编号
     */
    @TableId(value = "role_no", type = IdType.AUTO)
    private Integer roleNo;

    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;

    /**
     * 角色描述
     */
    @TableField("role_describe")
    private String roleDescribe;

}
