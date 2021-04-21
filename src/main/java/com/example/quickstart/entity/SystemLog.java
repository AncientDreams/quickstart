package com.example.quickstart.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 系统操作记录
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-07-29
 */
@TableName("system_log")
@Data
public class SystemLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "log_id", type = IdType.AUTO)
    private Integer logId;

    /**
     * 请求ip
     */
    @TableField("request_ip")
    private String requestIp;

    /**
     * 服务器ip
     */
    @TableField("server_ip")
    private String serverIp;

    /**
     * 请求地址
     */
    @TableField("url")
    private String url;

    /**
     * 请求类型，如：post、get
     */
    @TableField("request_type")
    private String requestType;

    /**
     * 是否是ajax请求
     */
    @TableField("is_ajax")
    private boolean isAjax;

    /**
     * 请求时间
     */
    @TableField("request_time")
    private LocalDateTime requestTime;

    /**
     * 请求创建人
     */
    @TableField("request_by")
    private String requestBy;

    @TableField("request_parameter")
    private String requestParameter;

    /**
     * 逻辑删除标记
     */
    @TableField("flag")
    private String flag;
}
