package com.example.quickstart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.example.quickstart.annotation.MethodRunLog;
import com.example.quickstart.bo.PagingTool;
import com.example.quickstart.bo.R;
import com.example.quickstart.constant.ResultConstant;
import com.example.quickstart.entity.SystemUser;
import com.example.quickstart.entity.SystemUserRole;
import com.example.quickstart.mapper.SystemUserMapper;
import com.example.quickstart.service.ISystemUserRoleService;
import com.example.quickstart.service.ISystemUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.quickstart.utils.Md5Util;
import com.example.quickstart.utils.RsaUtil;
import com.example.quickstart.vo.SystemUserAndRoleVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统用户服务类接口实现
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-07-13
 */
@Service
@Slf4j
@AllArgsConstructor
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements ISystemUserService {

    private final SystemUserMapper systemUserMapper;

    private final ISystemUserRoleService iSystemUserRoleService;

    @Override
    public boolean checkPassword(String systemPassword, String password, String salt) {
        String saltPassword = Md5Util.encode(password.concat(salt));
        return systemPassword.equalsIgnoreCase(saltPassword);
    }

    @MethodRunLog
    @Override
    public R<String> login(HttpServletRequest request) {
        //验证token
        try {
            String sessionToken = request.getSession().getAttribute("token").toString();
            String requestToken = request.getParameter("token");
            if (!sessionToken.equals(requestToken)) {
                return R.fail(ResultConstant.TOKEN_ERROR);
            }
        } catch (Exception e) {
            return R.fail(ResultConstant.TOKEN_ERROR);
        }
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        //账户名，密码解密
        try {
            userName = RsaUtil.decrypt(userName);
            password = RsaUtil.decrypt(password);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("登录账户密码RSA解密失败！" + e.getMessage(), e);
            return R.fail(ResultConstant.SERVER_ERROR);
        }

        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
            subject.login(token);
        } catch (AuthenticationException e) {
            log.info(e.getMessage());
            return R.fail(e.getMessage());
        }
        return R.success(ResultConstant.LOGIN_SUCCESS);
    }

    @Override
    public PagingTool<SystemUser> pageList(Page<SystemUser> page, SystemUser systemUser) {
        PagingTool<SystemUser> systemUserPagingTool = new PagingTool<>();
        LambdaQueryWrapper<SystemUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(SystemUser::getUserId, SystemUser::getEmail, SystemUser::getPhone, SystemUser::getRealName, SystemUser::getStatus,
                SystemUser::getUserName);
        if (!StringUtils.isEmpty(systemUser.getUserName())) {
            lambdaQueryWrapper.like(SystemUser::getUserName, systemUser.getUserName());
        }
        if (!StringUtils.isEmpty(systemUser.getStatus())) {
            lambdaQueryWrapper.eq(SystemUser::getUserName, systemUser.getStatus());
        }
        IPage<SystemUser> page1 = systemUserMapper.selectPage(page, lambdaQueryWrapper);
        systemUserPagingTool.setData(page1.getRecords());
        systemUserPagingTool.setCount(page1.getTotal());
        return systemUserPagingTool;
    }

    @Override
    public R<String> addUser(HttpServletRequest request) {
        //真实姓名
        String name = request.getParameter("name");
        //登录名称
        String username = request.getParameter("username");

        Integer userByName = systemUserMapper.selectCount(new LambdaQueryWrapper<SystemUser>().eq(SystemUser::getUserName, username));
        if (SqlHelper.retBool(userByName)) {
            return R.fail(ResultConstant.USER_EXIST);
        }

        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String pass = request.getParameter("pass");

        SystemUser systemUser = new SystemUser();
        systemUser.setEmail(email);
        systemUser.setPhone(phone);
        systemUser.setUserName(username);
        systemUser.setRealName(name);
        systemUser.setSalt(Md5Util.encode(identifying()));
        systemUser.setPassword(Md5Util.encode(pass + systemUser.getSalt()));
        //默认不启用
        systemUser.setStatus("1");
        try {
            if (!SqlHelper.retBool(systemUserMapper.insert(systemUser))) {
                log.error("新增用户失败！用户信息：{}", systemUser.toString());
                return R.fail(ResultConstant.SAVE_FAIL);
            }

            SystemUser user = systemUserMapper.selectOne(new LambdaQueryWrapper<SystemUser>().eq(SystemUser::getUserName, username));
            //获取角色列表
            List<SystemUserRole> systemUserRoles = buildSystemUserRoles(request, user.getUserId());
            if (!systemUserRoles.isEmpty()) {
                if (!iSystemUserRoleService.saveBatch(systemUserRoles)) {
                    log.error("新增角色信息失败！角色信息：{}", systemUserRoles.toString());
                    throw new Exception(ResultConstant.SAVE_FAIL);
                }
            }

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
        return R.success(ResultConstant.SAVE_SUCCESS);
    }

    @Override
    public SystemUserAndRoleVo queryUserRole(String userId) {
        return systemUserMapper.queryUserRole(userId);
    }

    @Override
    public R<String> updateUserInfo(HttpServletRequest request, SystemUser systemUser) {
        //判断状态
        String status = "on";
        if (status.equals(systemUser.getStatus())) {
            systemUser.setStatus("0");
        } else {
            systemUser.setStatus("1");
        }

        try {
            if (!SqlHelper.retBool(systemUserMapper.updateById(systemUser))) {
                log.error("修改用户信息失败！用户信息：{}", systemUser.toString());
                return R.fail(ResultConstant.UPDATE_FAIL);
            }

            //删除当前所有的用户角色信息，重新赋予角色
            LambdaQueryWrapper<SystemUserRole> lambdaQueryWrapper = new LambdaQueryWrapper<SystemUserRole>()
                    .eq(SystemUserRole::getUserId, systemUser.getUserId());
            if (!iSystemUserRoleService.list(lambdaQueryWrapper).isEmpty()) {
                if (!iSystemUserRoleService.remove(lambdaQueryWrapper)) {
                    log.error("删除用户信息失败！");
                    throw new Exception(ResultConstant.REMOVE_FAIL);
                }
            }

            //前端传入选择的用户选择角色列表
            List<SystemUserRole> systemUserRoles = buildSystemUserRoles(request, systemUser.getUserId());
            //新增
            if (!systemUserRoles.isEmpty()) {
                if (!iSystemUserRoleService.saveBatch(systemUserRoles)) {
                    log.error("批量新增用户信息失败！");
                    throw new Exception(ResultConstant.SAVE_FAIL);
                }
            }

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
        return R.success(ResultConstant.UPDATE_SUCCESS);
    }

    @Override
    public R<String> resetPassword(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        //再次验证密码
        String passCode = request.getParameter("pass");
        String passWord = request.getParameter("repass");
        String userId = request.getParameter("userId");
        //验证用户是否存在
        SystemUser systemUser = systemUserMapper.selectOne(new LambdaQueryWrapper<SystemUser>().eq(SystemUser::getUserId, userId));
        if (systemUser == null) {
            return R.fail(ResultConstant.USER_NOT_EXIST);
        }
        log.info("正在修改用户密码，当前登录人：{}，被修改用户信息：{}", subject.getPrincipal(), systemUser.toString());

        if (passCode.equals(passWord)) {
            systemUser.setSalt(Md5Util.encode(identifying()));
            systemUser.setPassword(Md5Util.encode(passCode + systemUser.getSalt()));
            if (!SqlHelper.retBool(systemUserMapper.updateById(systemUser))) {
                log.error("修改用户密码失败！用户信息：{}", systemUser.toString());
                return R.fail(ResultConstant.UPDATE_FAIL);
            }
        } else {
            return R.fail(ResultConstant.CHECK_PASSWORD_FAIL);
        }
        return R.success(ResultConstant.UPDATE_SUCCESS);
    }

    @MethodRunLog(methodName = "删除用户")
    @Override
    public R<String> removeUserByUserId(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        log.info("正在删除用户信息，当前登录人：{}", subject.getPrincipal());
        String userId = request.getParameter("userId");
        try {
            //验证用户是否存在
            SystemUser systemUser = systemUserMapper.selectOne(new LambdaQueryWrapper<SystemUser>().eq(SystemUser::getUserId, userId));
            if (systemUser == null) {
                log.info("用户不存在，id{}", userId);
                return R.fail(ResultConstant.USER_NOT_EXIST);
            }
            log.info("被删除用户信息，{}", systemUser.toString());

            //删除用户信息
            if (!SqlHelper.retBool(systemUserMapper.deleteById(userId))) {
                log.info("删除用户失败，id{}", userId);
                return R.fail(ResultConstant.REMOVE_FAIL);
            }
            //删除用户角色信息
            LambdaQueryWrapper<SystemUserRole> lambdaQueryWrapper = new LambdaQueryWrapper<SystemUserRole>()
                    .eq(SystemUserRole::getUserId, systemUser.getUserId());

            if (iSystemUserRoleService.count(lambdaQueryWrapper) > 0) {
                if (!iSystemUserRoleService.remove(lambdaQueryWrapper)) {
                    log.info("删除用户失败，id{}", userId);
                    throw new Exception(ResultConstant.REMOVE_FAIL);
                }
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
        return R.success(ResultConstant.REMOVE_SUCCESS);
    }

    @Override
    public SystemUser queryUserByUserName(String userName) {
        return getOne(new LambdaQueryWrapper<SystemUser>()
                .eq(SystemUser::getUserName, userName));
    }

    @Override
    public R<String> updateUserInfoByUser(SystemUser systemUser) {
        if (updateById(systemUser)) {
            return R.success(ResultConstant.UPDATE_SUCCESS);
        }
        return R.fail(ResultConstant.UPDATE_FAIL);
    }

    @Override
    public R<String> updatePassWord(HttpServletRequest request) {
        String oldPassWord = request.getParameter("oldPass");
        String newPassWord = request.getParameter("newPass");

        String username = (String) SecurityUtils.getSubject().getPrincipal();
        LambdaQueryWrapper<SystemUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SystemUser::getUserName, username);
        SystemUser systemUser = getOne(lambdaQueryWrapper);
        if (systemUser == null) {
            return R.fail(ResultConstant.USER_NOT_EXIST);
        } else if ("01".equals(systemUser.getStatus())) {
            return R.fail("用户已被停用！");
        }

        if (!checkPassword(systemUser.getPassword(), oldPassWord, systemUser.getSalt())) {
            return R.fail("原密码输入错误！");
        }

        //修改密码
        systemUser.setSalt(Md5Util.encode(identifying()));
        systemUser.setPassword(Md5Util.encode(newPassWord + systemUser.getSalt()));
        if (!updateById(systemUser)) {
            log.error("修改用户密码失败！用户信息：{}", systemUser.toString());
            return R.fail(ResultConstant.UPDATE_FAIL);
        }
        return R.success(ResultConstant.UPDATE_SUCCESS);
    }

    private List<SystemUserRole> buildSystemUserRoles(HttpServletRequest request, Integer uid) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        List<SystemUserRole> systemUserRoles = new ArrayList<>();
        for (String key : parameterMap.keySet()) {
            if ("on".equals(parameterMap.get(key)[0])) {
                try {
                    Integer.parseInt(key);
                } catch (NumberFormatException e) {
                    continue;
                }
                SystemUserRole systemUserRole = new SystemUserRole();
                systemUserRole.setUserId(uid);
                systemUserRole.setRoleId(Integer.parseInt(key));
                systemUserRoles.add(systemUserRole);
            }
        }
        return systemUserRoles;
    }

    private String identifying() {
        StringBuilder letter = new StringBuilder();
        // 小写
        for (int i = 0; i < 10; i++) {
            letter.append((char) (Math.random() * 26 + 'a'));
        }
        return letter.toString();
    }
}
