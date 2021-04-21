package com.example.quickstart.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 系统参数
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-08-03
 */
@TableName("system_parameter")
@Data
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

    /**
     * 逻辑删除标记
     */
    @TableField("flag")
    private String flag;
}
