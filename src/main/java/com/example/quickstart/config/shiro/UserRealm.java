package com.example.quickstart.config.shiro;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.quickstart.constant.MessageConstant;
import com.example.quickstart.entity.SystemPermission;
import com.example.quickstart.entity.SystemUser;
import com.example.quickstart.entity.SystemUserRole;
import com.example.quickstart.service.ISystemPermissionService;
import com.example.quickstart.service.ISystemUserRoleService;
import com.example.quickstart.service.ISystemUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * UserRealm，shiro
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-07-15 17:30
 */
@Component
public class UserRealm extends AuthorizingRealm {

    private ISystemUserService iSystemUserService;

    private ISystemUserRoleService iSystemUserRoleService;

    private ISystemPermissionService iSystemPermissionService;

    public UserRealm(ISystemUserService iSystemUserService, ISystemUserRoleService iSystemUserRoleService
            , ISystemPermissionService iSystemPermissionService) {
        this.iSystemPermissionService = iSystemPermissionService;
        this.iSystemUserService = iSystemUserService;
        this.iSystemUserRoleService = iSystemUserRoleService;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        LambdaQueryWrapper<SystemUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SystemUser::getUserName, username).eq(SystemUser::getStatus,"0");
        SystemUser systemUser = iSystemUserService.getOne(lambdaQueryWrapper);

        //用户角色
        List<SystemUserRole> systemUserRoles = iSystemUserRoleService.
                list(new LambdaQueryWrapper<SystemUserRole>().eq(SystemUserRole::getUserId, systemUser.getUserId()));

        for (SystemUserRole userRole : systemUserRoles) {
            //添加角色
            simpleAuthorizationInfo.addRole(String.valueOf(userRole.getRoleId()));
            //角色的权限
            List<SystemPermission> systemPermissionList = iSystemPermissionService
                    .findByUerName(systemUser.getUserName(), null);
            systemPermissionList.forEach(systemPermission -> {
                String url = systemPermission.getUrl();
                if (!StringUtils.isEmpty(url)) {
                    //添加权限（URL）
                    simpleAuthorizationInfo.addStringPermission(systemPermission.getUrl().trim());
                }
            });
        }
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //判断用户名  token中的用户信息是登录时候传进来的
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        LambdaQueryWrapper<SystemUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SystemUser::getUserName, token.getUsername()).eq(SystemUser::getStatus, "0");
        SystemUser systemUser = iSystemUserService.getOne(lambdaQueryWrapper);
        if (systemUser == null) {
            throw new AccountException(MessageConstant.USER_NOT_EXIST);
        }

        String password = new String((char[]) token.getCredentials());
        if (!iSystemUserService.checkPassword(systemUser.getPassword(), password, systemUser.getSalt())) {
            throw new AccountException(MessageConstant.LOGIN_ERROR);
        }
        return new SimpleAuthenticationInfo(token.getPrincipal(), password, getName());
    }

}
