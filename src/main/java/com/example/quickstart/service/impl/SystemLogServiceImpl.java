package com.example.quickstart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.quickstart.bo.PagingTool;
import com.example.quickstart.bo.ResultBody;
import com.example.quickstart.constant.MessageConstant;
import com.example.quickstart.entity.SystemLog;
import com.example.quickstart.mapper.SystemLogMapper;
import com.example.quickstart.service.ISystemLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper, SystemLog> implements ISystemLogService {

    private Logger logger = LoggerFactory.getLogger(SystemLogServiceImpl.class);

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
            return new PagingTool<>(MessageConstant.QUERY_SUCCESS, systemLogPage.getRecords(), systemLogPage.getTotal());
        } catch (Exception e) {
            logger.error(MessageConstant.QUERY_FAIL + e.getMessage(), e);
        }
        return new PagingTool<>(MessageConstant.QUERY_FAIL, null, 0);
    }

    @Override
    public ResultBody delete(HttpServletRequest request) {
        String logId = request.getParameter("logId");
        SystemLog systemLog = this.getById(logId);
        if (systemLog == null) {
            return new ResultBody(false, "未找到LogID：" + logId + "操作记录信息。");
        }
        Subject subject = SecurityUtils.getSubject();
        logger.info("删除操作记录，当前登录人：{}，被删除记录信息：{}", subject.getPrincipal(), systemLog.toString());

        if (!this.removeById(logId)) {
            logger.info("删除失败，logID：{}", logId);
            return new ResultBody(false, MessageConstant.REMOVE_FAIL);
        }
        return new ResultBody(true, MessageConstant.REMOVE_SUCCESS);
    }
}
