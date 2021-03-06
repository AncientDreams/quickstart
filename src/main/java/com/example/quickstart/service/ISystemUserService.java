package com.example.quickstart.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.quickstart.bo.PagingTool;
import com.example.quickstart.bo.R;
import com.example.quickstart.entity.SystemUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.quickstart.vo.SystemUserAndRoleVo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 系统用户服务类接口
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-07-13
 */
public interface ISystemUserService extends IService<SystemUser> {

    /**
     * 检查密码是否正确
     *
     * @param systemPassword 数据库中密码
     * @param password       前端传入密码
     * @param salt           密码盐
     * @return 是否正确
     */
    boolean checkPassword(String systemPassword, String password, String salt);

    /**
     * 登录
     *
     * @param request 请求
     * @return 是否登录成功
     */
    R<String> login(HttpServletRequest request);

    /**
     * 分页查询
     *
     * @param page       分页page
     * @param systemUser 条件data
     * @return PagingTool<SystemUser>
     */
    PagingTool<SystemUser> pageList(Page<SystemUser> page, SystemUser systemUser);

    /**
     * 添加用户
     *
     * @param request 请求信息
     * @return R
     */
    @Transactional(rollbackFor = {Exception.class})
    R<String> addUser(HttpServletRequest request);

    /**
     * 查询用户和角色一对多关系
     *
     * @param userId 用户编号
     * @return List<SystemUserRole>
     */
    SystemUserAndRoleVo queryUserRole(String userId);

    /**
     * 修改用户信息
     *
     * @param request    请求信息
     * @param systemUser 用户
     * @return R
     */
    @Transactional(rollbackFor = {Exception.class})
    R<String> updateUserInfo(HttpServletRequest request, SystemUser systemUser);

    /**
     * 重置用户密码
     *
     * @param request 请求信息
     * @return returnR
     */
    R<String> resetPassword(HttpServletRequest request);

    /**
     * 删除用户，真正的删除，可以根据自身需求整改。
     *
     * @param request 请求信息
     * @return returnR
     */
    @Transactional(rollbackFor = {Exception.class})
    R<String> removeUserByUserId(HttpServletRequest request);

    /**
     * 根据用户名查询用户信息
     *
     * @param userName 用户名
     * @return SystemUser
     */
    SystemUser queryUserByUserName(String userName);

    /**
     * 用户自身修改个人信息
     *
     * @param systemUser systemUser
     * @return R
     */
    R<String> updateUserInfoByUser(SystemUser systemUser);

    /**
     * 用户自身修改密码
     *
     * @param request 请求参数
     * @return R
     */
    R<String> updatePassWord(HttpServletRequest request);

    /**
     * 获取AuthorizationInfo 注入到shiro中
     *
     * @param userName 用户名
     * @return AuthorizationInfo
     */
    AuthorizationInfo getAuthorizationInfo(String userName);

}
