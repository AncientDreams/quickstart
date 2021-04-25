package com.example.quickstart.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *  错误日志实体类
 * </p>
 *
 * @author ZhangXianYu
 * @since 2021-04-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("system_error_log")
public class SystemErrorLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 日志的文件名称
     */
    @TableField("log_file_name")
    private String logFileName;

    /**
     * 错误行数
     */
    @TableField("error_line")
    private Integer errorLine;

    /**
     * 服务器ip
     */
    @TableField("server_ip")
    private String serverIp;

    /**
     * 单行错误信息
     */
    @TableField("error_info")
    private String errorInfo;

    /**
     * 00：解决 01：未解决
     */
    @TableField("solve")
    private String solve;

    /**
     * 解决人
     */
    @TableField("solve_people")
    private String solvePeople;

    /**
     * 解决时间
     */
    @TableField("solve_time")
    private LocalDateTime solveTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

}
