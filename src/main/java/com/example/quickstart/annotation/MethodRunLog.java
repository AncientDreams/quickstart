package com.example.quickstart.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *  该注解用于方法运行时 的 日志信息打印！
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-04-09 16:44
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface MethodRunLog {

    /**
     * 是否开启方法运行时间监控
     */
    boolean runTime() default true;

    /**
     * 是否日志打印方法参数
     */
    boolean parameters() default true;

    /**
     * 是否日志打印方法 返回值
     */
    boolean result() default true;

    /**
     * 日志打印包含方法名称
     */
    String methodName() default "";

}
