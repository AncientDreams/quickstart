package com.example.quickstart.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * 用户标记在控制器的方法或者类上跳过权限检查，
 * 如果在类上，该类下所有的方法跳过，如果在方法上，该方法跳过。
 * 就是允许登录用户访问，表示该路径或者该类下的路径不需要拥有权限 只要登录后就能访问
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-07-22 16:12
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowAccess {
}
