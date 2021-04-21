package com.example.quickstart.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.quickstart.bo.PagingTool;
import com.example.quickstart.bo.R;
import com.example.quickstart.entity.SystemLog;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 系统操作日志接口服务类
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-07-29
 */
public interface ISystemLogService extends IService<SystemLog> {

    /**
     * 系统操作日志分页查询
     *
     * @param request 请求参数
     * @param page    分页对象
     * @return PagingTool
     */
    PagingTool<SystemLog> pageList(HttpServletRequest request, Page<SystemLog> page);

    /**
     * 删除系统操作记录
     *
     * @param request 请求信息
     * @return R
     */
    R<String> delete(HttpServletRequest request);
}
