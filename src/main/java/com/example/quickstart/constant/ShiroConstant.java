package com.example.quickstart.constant;

/**
 * <p>
 * Shiro 安全框架使用常量
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-07-13 14:54
 */
public interface ShiroConstant {

    /**
     * anon: 无需认证（登录）可以访问
     */
    String ANON = "anon";

    /**
     * authc: 必须认证才可以访问
     */
    String AUTHC = "authc";

    /**
     * user: 如果使用rememberMe的功能可以直接访问
     */
    String USER = "user";

    /**
     * perms： 该资源必须得到资源权限才可以访问
     */
    String PERMS = "perms";

    /**
     * role: 该资源必须得到角色权限才可以访问
     */
    String ROLE = "role";

}
