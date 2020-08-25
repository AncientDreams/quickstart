package com.example.quickstart.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

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

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public boolean isAjax() {
        return isAjax;
    }

    public void setAjax(boolean ajax) {
        isAjax = ajax;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(String requestBy) {
        this.requestBy = requestBy;
    }

    public String getRequestParameter() {
        return requestParameter;
    }

    public void setRequestParameter(String requestParameter) {
        this.requestParameter = requestParameter;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "SystemLog{" +
                "logId=" + logId +
                ", requestIp='" + requestIp + '\'' +
                ", serverIp='" + serverIp + '\'' +
                ", url='" + url + '\'' +
                ", requestType='" + requestType + '\'' +
                ", isAjax=" + isAjax +
                ", requestTime=" + requestTime +
                ", requestBy='" + requestBy + '\'' +
                ", requestParameter='" + requestParameter + '\'' +
                '}';
    }
}
