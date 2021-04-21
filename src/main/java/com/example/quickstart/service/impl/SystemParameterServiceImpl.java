package com.example.quickstart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.quickstart.bo.PagingTool;
import com.example.quickstart.bo.R;
import com.example.quickstart.constant.ResultConstant;
import com.example.quickstart.entity.SystemParameter;
import com.example.quickstart.mapper.SystemParameterMapper;
import com.example.quickstart.service.ISystemParameterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SystemParameterServiceImpl extends ServiceImpl<SystemParameterMapper, SystemParameter> implements ISystemParameterService {

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

        return new PagingTool<>(ResultConstant.QUERY_SUCCESS, parameterPage.getRecords(), parameterPage.getTotal());
    }

    @Override
    public R<String> addSystemParameter(SystemParameter systemParameter) {
        try {
            if (existByKey(systemParameter.getParameterKey())) {
                return R.fail("KEY（" + systemParameter.getParameterKey() + "）已存在");
            }
            systemParameter.setCreationTime(LocalDateTime.now());
            if (!save(systemParameter)) {
                log.error("保存参数信息失败，参数信息：{}", systemParameter.toString());
                return R.fail(ResultConstant.SAVE_FAIL);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.fail(ResultConstant.SAVE_FAIL);
        }
        return R.success(ResultConstant.SAVE_SUCCESS);
    }

    @Override
    public R<String> updateSystemParameter(SystemParameter systemParameter) {
        try {
            if (!updateById(systemParameter)) {
                log.error("修改参数信息失败，参数信息：{}", systemParameter.toString());
                return R.fail(ResultConstant.UPDATE_FAIL);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.fail(ResultConstant.UPDATE_FAIL);
        }
        return R.success(ResultConstant.UPDATE_SUCCESS);
    }

    @Override
    public R<String> removeByKey(String key) {
        try {
            if (!removeById(key)) {
                log.error("删除参数信息失败，参数KEY：{}", key);
                return R.fail(ResultConstant.REMOVE_FAIL);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.fail(ResultConstant.REMOVE_FAIL);
        }
        return R.success(ResultConstant.REMOVE_SUCCESS);
    }

    private boolean existByKey(String key) {
        LambdaQueryWrapper<SystemParameter> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SystemParameter::getParameterKey, key);
        return count(lambdaQueryWrapper) > 0;
    }
}
