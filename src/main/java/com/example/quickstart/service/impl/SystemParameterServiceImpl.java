package com.example.quickstart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.quickstart.bo.PagingTool;
import com.example.quickstart.bo.ResultBody;
import com.example.quickstart.constant.MessageConstant;
import com.example.quickstart.entity.SystemParameter;
import com.example.quickstart.mapper.SystemParameterMapper;
import com.example.quickstart.service.ISystemParameterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * <p>
 * 系统参数接口服务实现类
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-08-03
 */
@Service
public class SystemParameterServiceImpl extends ServiceImpl<SystemParameterMapper, SystemParameter> implements ISystemParameterService {

    private Logger logger = LoggerFactory.getLogger(SystemParameterServiceImpl.class);

    @Override
    public PagingTool<SystemParameter> pageList(Page<SystemParameter> page, SystemParameter systemParameter) {
        LambdaQueryWrapper<SystemParameter> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (!StringUtils.isEmpty(systemParameter.getParameterName())) {
            lambdaQueryWrapper.like(SystemParameter::getParameterName, systemParameter.getParameterName());
        }
        if (!StringUtils.isEmpty(systemParameter.getParameterKey())) {
            lambdaQueryWrapper.eq(SystemParameter::getParameterKey, systemParameter.getParameterKey());
        }
        lambdaQueryWrapper.orderByDesc(SystemParameter::getCreationTime);

        IPage<SystemParameter> parameterPage = page(page, lambdaQueryWrapper);

        return new PagingTool<>(MessageConstant.QUERY_SUCCESS, parameterPage.getRecords(), parameterPage.getTotal());
    }

    @Override
    public ResultBody addSystemParameter(SystemParameter systemParameter) {
        try {
            if (existByKey(systemParameter.getParameterKey())) {
                return new ResultBody(false, "KEY（" + systemParameter.getParameterKey() + "）已存在");
            }
            systemParameter.setCreationTime(LocalDateTime.now());
            if (!save(systemParameter)) {
                logger.error("保存参数信息失败，参数信息：{}", systemParameter.toString());
                return new ResultBody(false, MessageConstant.SAVE_FAIL);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultBody(false, MessageConstant.SAVE_FAIL);
        }
        return new ResultBody(true, MessageConstant.SAVE_SUCCESS);
    }

    @Override
    public ResultBody updateSystemParameter(SystemParameter systemParameter) {
        try {
            if (existByKey(systemParameter.getParameterKey())) {
                return new ResultBody(false, "KEY（" + systemParameter.getParameterKey() + "）已存在");
            }
            if (!updateById(systemParameter)) {
                logger.error("修改参数信息失败，参数信息：{}", systemParameter.toString());
                return new ResultBody(false, MessageConstant.UPDATE_FAIL);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultBody(false, MessageConstant.UPDATE_FAIL);
        }
        return new ResultBody(true, MessageConstant.UPDATE_SUCCESS);
    }

    @Override
    public ResultBody removeByKey(String key) {
        try {
            if (!removeById(key)) {
                logger.error("删除参数信息失败，参数KEY：{}", key);
                return new ResultBody(false, MessageConstant.REMOVE_FAIL);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultBody(false, MessageConstant.REMOVE_FAIL);
        }
        return new ResultBody(true, MessageConstant.REMOVE_SUCCESS);
    }

    private boolean existByKey(String key) {
        LambdaQueryWrapper<SystemParameter> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SystemParameter::getParameterKey, key);
        return count(lambdaQueryWrapper) > 0;
    }
}
