package com.example.quickstart.config.shiro;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.quickstart.constant.ResultConstant;
import com.example.quickstart.entity.SystemUser;
import com.example.quickstart.service.ISystemUserService;
import lombok.AllArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;


/**
 * <p>
 * UserRealm，shiro
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-07-15 17:30
 */
@Component
@AllArgsConstructor
public class UserRealm extends AuthorizingRealm {

    private final ISystemUserService iSystemUserService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        return iSystemUserService.getAuthorizationInfo(username);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //判断用户名  token中的用户信息是登录时候传进来的
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        LambdaQueryWrapper<SystemUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SystemUser::getUserName, token.getUsername()).eq(SystemUser::getStatus, "0");
        SystemUser systemUser = iSystemUserService.getOne(lambdaQueryWrapper);
        if (systemUser == null) {
            throw new AccountException(ResultConstant.USER_NOT_EXIST);
        }

        String password = new String((char[]) token.getCredentials());
        if (!iSystemUserService.checkPassword(systemUser.getPassword(), password, systemUser.getSalt())) {
            throw new AccountException(ResultConstant.LOGIN_ERROR);
        }
        return new SimpleAuthenticationInfo(token.getPrincipal(), password, getName());
    }

}
