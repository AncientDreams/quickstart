package com.example.quickstart.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.quickstart.bo.PagingTool;
import com.example.quickstart.bo.R;
import com.example.quickstart.entity.SystemErrorLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author ZhangXianYu
 * @since 2021-04-23
 */
public interface ISystemErrorLogService extends IService<SystemErrorLog> {


    /**
     * 分页查询
     *
     * @param page           分页page
     * @param systemErrorLog 条件data
     * @return PagingTool<SystemErrorLog>
     */
    PagingTool<SystemErrorLog> pageList(Page<SystemErrorLog> page, SystemErrorLog systemErrorLog);

    /**
     * 解决错误异常问题
     *
     * @param systemErrorLog 系统错误信息
     * @return R
     */
    R<String> solve(SystemErrorLog systemErrorLog);
}
