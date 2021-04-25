package com.example.quickstart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.quickstart.bo.PagingTool;
import com.example.quickstart.bo.R;
import com.example.quickstart.constant.ResultConstant;
import com.example.quickstart.entity.SystemErrorLog;
import com.example.quickstart.mapper.SystemErrorLogMapper;
import com.example.quickstart.service.ISystemErrorLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ZhangXianYu
 * @since 2021-04-23
 */
@Service
public class SystemErrorLogServiceImpl extends ServiceImpl<SystemErrorLogMapper, SystemErrorLog> implements ISystemErrorLogService {

    @Override
    public PagingTool<SystemErrorLog> pageList(Page<SystemErrorLog> page, SystemErrorLog systemErrorLog) {
        PagingTool<SystemErrorLog> systemErrorLogPagingTool = new PagingTool<>();
        LambdaQueryWrapper<SystemErrorLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(systemErrorLog.getServerIp())) {
            lambdaQueryWrapper.eq(SystemErrorLog::getServerIp, systemErrorLog.getServerIp());
        }
        if (!StringUtils.isEmpty(systemErrorLog.getSolve())) {
            lambdaQueryWrapper.eq(SystemErrorLog::getSolve, systemErrorLog.getSolve());
        }
        lambdaQueryWrapper.orderByDesc(SystemErrorLog::getSolve);
        IPage<SystemErrorLog> page1 = this.page(page, lambdaQueryWrapper);
        systemErrorLogPagingTool.setData(page1.getRecords());
        systemErrorLogPagingTool.setCount(page1.getTotal());
        return systemErrorLogPagingTool;
    }

    @Override
    public R<String> solve(SystemErrorLog systemErrorLog) {
        systemErrorLog.setSolvePeople(SecurityUtils.getSubject().getPrincipal().toString());
        systemErrorLog.setSolveTime(LocalDateTime.now());
        return this.updateById(systemErrorLog) ? R.success(ResultConstant.UPDATE_SUCCESS) : R.fail(ResultConstant.UPDATE_FAIL);
    }

}
