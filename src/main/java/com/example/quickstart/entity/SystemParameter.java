package com.example.quickstart.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 *  系统参数
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-08-03
 */
@TableName("system_parameter")
public class SystemParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "parameter_id", type = IdType.AUTO)
    private String parameterId;

    @TableField("parameter_key")
    private String parameterKey;

    @TableField("parameter_name")
    private String parameterName;

    @TableField("parameter_value")
    private String parameterValue;

    @TableField("creation_time")
    private LocalDateTime creationTime;

    public String getParameterId() {
        return parameterId;
    }

    public void setParameterId(String parameterId) {
        this.parameterId = parameterId;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterKey() {
        return parameterKey;
    }

    public void setParameterKey(String parameterKey) {
        this.parameterKey = parameterKey;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String toString() {
        return "SystemParameter{" +
                "parameterName='" + parameterName + '\'' +
                ", parameterKey='" + parameterKey + '\'' +
                ", parameterValue='" + parameterValue + '\'' +
                ", creationTime=" + creationTime +
                '}';
    }
}
