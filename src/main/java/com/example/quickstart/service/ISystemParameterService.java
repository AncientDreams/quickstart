package com.example.quickstart.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.quickstart.bo.PagingTool;
import com.example.quickstart.bo.ResultBody;
import com.example.quickstart.entity.SystemParameter;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统参数接口服务类
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-08-03
 */
public interface ISystemParameterService extends IService<SystemParameter> {

    /**
     * 分页查询
     *
     * @param page            分页对象
     * @param systemParameter 筛选对象
     * @return PagingTool<SystemUser>
     */
    PagingTool<SystemParameter> pageList(Page<SystemParameter> page, SystemParameter systemParameter);

    /**
     * 添加系统参数
     *
     * @param systemParameter systemParameter
     * @return ResultBody
     */
    ResultBody addSystemParameter(SystemParameter systemParameter);

    /**
     * 修改系统参数
     *
     * @param systemParameter systemParameter
     * @return ResultBody
     */
    ResultBody updateSystemParameter(SystemParameter systemParameter);

    /**
     * 通过key删除参数名称
     *
     * @param key 参数key
     * @return ResultBody
     */
    ResultBody removeByKey(String key);
}
