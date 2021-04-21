package com.example.quickstart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.quickstart.bo.PagingTool;
import com.example.quickstart.bo.R;
import com.example.quickstart.constant.ResultConstant;
import com.example.quickstart.entity.SystemLog;
import com.example.quickstart.mapper.SystemLogMapper;
import com.example.quickstart.service.ISystemLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 系统操作日志接口服务实现类
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-07-29
 */
@Service
@Slf4j
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper, SystemLog> implements ISystemLogService {

    @Override
    public PagingTool<SystemLog> pageList(HttpServletRequest request, Page<SystemLog> page) {
        try {
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String requestBy = request.getParameter("requestBy");
            String url = request.getParameter("url");
            LambdaQueryWrapper<SystemLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
                // >= startDate  and <= endDate
                lambdaQueryWrapper.ge(SystemLog::getRequestTime, startDate).le(SystemLog::getRequestTime, endDate);
            } else if (!StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {
                // >= startDate
                lambdaQueryWrapper.ge(SystemLog::getRequestTime, startDate);
            } else if (StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
                // <= endDate
                lambdaQueryWrapper.le(SystemLog::getRequestTime, endDate);
            }
            if (!StringUtils.isEmpty(requestBy)) {
                lambdaQueryWrapper.like(SystemLog::getRequestBy, requestBy);
            }
            if (!StringUtils.isEmpty(url)) {
                lambdaQueryWrapper.eq(SystemLog::getUrl, url);
            }
            lambdaQueryWrapper.orderByAsc(SystemLog::getRequestTime);

            IPage<SystemLog> systemLogPage = this.page(page, lambdaQueryWrapper);
            return new PagingTool<>(ResultConstant.QUERY_SUCCESS, systemLogPage.getRecords(), systemLogPage.getTotal());
        } catch (Exception e) {
            log.error(ResultConstant.QUERY_FAIL + e.getMessage(), e);
        }
        return new PagingTool<>(ResultConstant.QUERY_FAIL, null, 0);
    }

    @Override
    public R<String> delete(HttpServletRequest request) {
        String logId = request.getParameter("logId");
        SystemLog systemLog = this.getById(logId);
        if (systemLog == null) {
            return R.fail("未找到LogID：" + logId + "操作记录信息。");
        }
        Subject subject = SecurityUtils.getSubject();
        log.info("删除操作记录，当前登录人：{}，被删除记录信息：{}", subject.getPrincipal(), systemLog.toString());

        if (!this.removeById(logId)) {
            log.info("删除失败，logID：{}", logId);
            return R.fail(ResultConstant.REMOVE_FAIL);
        }
        return R.success(ResultConstant.REMOVE_SUCCESS);
    }
}
