package com.example.quickstart.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  系统权限实体类
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-07-13
 */
@TableName("system_permission")
@Data
public class SystemPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限编号
     */
    @TableId(value = "permission_id", type = IdType.AUTO)
    private Integer permissionId;

    /**
     * 权限名
     */
    @TableField("permission_name")
    private String permissionName;

    /**
     * 请求地址
     */
    @TableField("url")
    private String url;

    /**
     * 显示菜单图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 是否在菜单中显示
     */
    @TableField("exhibition")
    private boolean exhibition;

    /**
     * 父级权限编号
     */
    @TableField("parentno")
    private Integer parentno;

}
