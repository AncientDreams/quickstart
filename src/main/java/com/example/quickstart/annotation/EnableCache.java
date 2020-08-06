package com.example.quickstart.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 开启缓存注解，加入此注解后该方法的返回结果将被缓存<br>
 * 在一定时间内执行方法都会返回缓存中的值。 作用域方法级，适用于更新频率低的查询。
 *
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-07-10 9:41
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface EnableCache {


    /**
     * 缓存过期时间，过期后将自动更新缓存，单位毫秒，为0时表示缓存永久有效
     */
    long expirationTime();


}
