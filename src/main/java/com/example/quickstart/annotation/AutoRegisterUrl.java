package com.example.quickstart.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 因为开发人员每次新增功能后就会有对应得权限URL需要加入到权限管理，那么需要在投产后加入到数据库中。
 * 所以每次都需要在投产前整理好权限信息，所以可以使用此注解标注在Controller中的方法中，实现自动的一个注册！<br>
 * 使用此注解再Controller指定 URL映射关系，实现自动将权限配置到数据库中<br/>
 * ！！！！注意：不适用与多重指向，如： @RequestMapping(value ={"/test.do","/test1.do"}) 不适用<br>
 * 使用例子 ： {@link com.example.quickstart.controller.example.AutoRegisterUrlExampleController}
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-07-23 21:11
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface AutoRegisterUrl {

    /**
     * 权限名称
     */
    String permissionName();

    /**
     * 在左边的菜单栏中是否显示<br>
     * true:显示  false 不显示
     */
    boolean isShow();

    /**
     * 权限图标 <br>
     * 默认是图标是右箭头，可以代码指定，也可以在自动注册后再修改
     */
    String icon() default "layui-icon-right";

    /**
     * 父节点名称，请确保父节点存在！
     */
    String parentName() default "";

    /**
     * 注册优先级别 <br>
     * 如同一个类下多个需要注册的权限，父节点就需要优先注册。 数字越小优先级别越高！0表示不参与排序。<br>
     * 注意：请保证排序顺序 如 1，2，3，4，5
     */
    int priority() default 0;
}
