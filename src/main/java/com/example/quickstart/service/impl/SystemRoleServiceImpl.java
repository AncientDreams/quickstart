package com.example.quickstart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.example.quickstart.annotation.MethodRunLog;
import com.example.quickstart.bo.PagingTool;
import com.example.quickstart.bo.ResultBody;
import com.example.quickstart.constant.MessageConstant;
import com.example.quickstart.entity.SystemRole;
import com.example.quickstart.entity.SystemRolePermission;
import com.example.quickstart.entity.SystemUserRole;
import com.example.quickstart.mapper.SystemRoleMapper;
import com.example.quickstart.service.ISystemRolePermissionService;
import com.example.quickstart.service.ISystemRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.quickstart.service.ISystemUserRoleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


/**
 * <p>
 * 系统角色实体类服务接口实现
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-07-13
 */
@Service
public class SystemRoleServiceImpl extends ServiceImpl<SystemRoleMapper, SystemRole> implements ISystemRoleService {

    private Logger logger = LoggerFactory.getLogger(SystemRoleServiceImpl.class);

    private SystemRoleMapper systemRoleMapper;

    private ISystemRolePermissionService iSystemRolePermissionService;

    private ISystemUserRoleService iSystemUserRoleService;


    public SystemRoleServiceImpl(SystemRoleMapper systemRoleMapper, ISystemRolePermissionService iSystemRolePermissionService,
                                 ISystemUserRoleService iSystemUserRoleService) {
        this.systemRoleMapper = systemRoleMapper;
        this.iSystemRolePermissionService = iSystemRolePermissionService;
        this.iSystemUserRoleService = iSystemUserRoleService;
    }

    @Override
    public PagingTool<SystemRole> init(Page<SystemRole> page) {
        PagingTool<SystemRole> rolePagingTool = new PagingTool<>();
        IPage<SystemRole> systemRolePage = systemRoleMapper.selectPage(page, null);
        rolePagingTool.setData(systemRolePage.getRecords());
        rolePagingTool.setCount(systemRolePage.getTotal());
        return rolePagingTool;
    }

    @Override
    public ResultBody saveSystemRole(SystemRole systemRole) {
        int systemRoleByName = systemRoleMapper.selectCount(new LambdaQueryWrapper<SystemRole>()
                .eq(SystemRole::getRoleName, systemRole.getRoleName()));
        if (SqlHelper.retBool(systemRoleByName)) {
            return new ResultBody(false, MessageConstant.ROLE_EXIST);
        }
        if (SqlHelper.retBool(systemRoleMapper.insert(systemRole))) {
            return new ResultBody(true, MessageConstant.SAVE_SUCCESS);
        }
        return new ResultBody(true, MessageConstant.SAVE_FAIL);
    }

    @MethodRunLog(methodName = "删除角色")
    @Override
    public ResultBody deleteSystemRole(SystemRole systemRole) {
        Subject subject = SecurityUtils.getSubject();
        logger.info("正在删除角色信息，当前登录人：{}，被删除角色信息：{}", subject.getPrincipal(), systemRole.toString());
        try {

            //删除角色对应的权限关系信息
            LambdaQueryWrapper<SystemRolePermission> systemRolePermissionLambdaQueryWrapper =
                    new LambdaQueryWrapper<SystemRolePermission>().eq(SystemRolePermission::getRoleId, systemRole.getRoleNo());

            if (iSystemRolePermissionService.count(systemRolePermissionLambdaQueryWrapper) > 0) {
                if (!iSystemRolePermissionService.remove(systemRolePermissionLambdaQueryWrapper)) {
                    logger.error("删除失败 _ SystemRolePermission！id{}", systemRole.getRoleNo());
                    return new ResultBody(false, MessageConstant.REMOVE_FAIL);
                }
            }

            //删除角色对应的用户关系信息
            LambdaQueryWrapper<SystemUserRole> systemUserRoleLambdaQueryWrapper =
                    new LambdaQueryWrapper<SystemUserRole>().eq(SystemUserRole::getRoleId, systemRole.getRoleNo());

            if (iSystemUserRoleService.count(systemUserRoleLambdaQueryWrapper) > 0) {
                if (!iSystemUserRoleService.remove(systemUserRoleLambdaQueryWrapper)) {
                    logger.error("删除失败 _ SystemUserRole！id{}", systemRole.getRoleNo());
                    throw new Exception(MessageConstant.REMOVE_FAIL);
                }
            }

            //删除角色信息
            if (systemRoleMapper.deleteById(systemRole.getRoleNo()) < 1) {
                logger.error("删除失败 _ SystemRole！id{}", systemRole.getRoleNo());
                throw new Exception(MessageConstant.REMOVE_FAIL);
            }

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return new ResultBody(false, MessageConstant.REMOVE_FAIL);
        }
        return new ResultBody(true, MessageConstant.REMOVE_SUCCESS);
    }

}
