package com.example.quickstart.config.shiro;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.quickstart.annotation.AllowAccess;
import com.example.quickstart.annotation.AutoRegisterUrl;
import com.example.quickstart.constant.ShiroConstant;
import com.example.quickstart.constant.SystemUrlConstant;
import com.example.quickstart.entity.SystemPermission;
import com.example.quickstart.service.ISystemLogService;
import com.example.quickstart.service.ISystemPermissionService;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.Filter;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <p>
 * Shiro 配置管理，启动权限自动注册
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-07-13 10:25
 */
@Configuration
public class ShiroConfig {

    private ApplicationContext applicationContext;

    private UserRealm userRealm;

    private ISystemLogService iSystemLogService;

    private ISystemPermissionService iSystemPermissionService;

    public ShiroConfig(ApplicationContext applicationContext, UserRealm userRealm, ISystemLogService iSystemLogService, ISystemPermissionService iSystemPermissionService) {
        this.applicationContext = applicationContext;
        this.userRealm = userRealm;
        this.iSystemLogService = iSystemLogService;
        this.iSystemPermissionService = iSystemPermissionService;
    }

    /**
     * 容器初始化时候保存记录所有的需要管理的RUL，合理使用保证线程安区。
     */
    public static List<String> urlList = new ArrayList<>();

    private Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(SystemUrlConstant.LOGIN);

        //自定义过滤器
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put(ShiroConstant.PERMS, new PermissionDeniedConfig(iSystemLogService));
        shiroFilterFactoryBean.setFilters(filters);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(setFilterChainDefinitionMap());
        return shiroFilterFactoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 设置过滤器链定义映射
     *
     * @return Map<String, String>
     */
    private Map<String, String> setFilterChainDefinitionMap() {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        // 所有用户允许的地址
        filterChainDefinitionMap.put(SystemUrlConstant.LOGIN, ShiroConstant.ANON);
        filterChainDefinitionMap.put(SystemUrlConstant.USER + SystemUrlConstant.LOGIN, ShiroConstant.ANON);
        filterChainDefinitionMap.put(SystemUrlConstant.ERROR, ShiroConstant.ANON);
        filterChainDefinitionMap.put(SystemUrlConstant.WELCOME, ShiroConstant.ANON);
        filterChainDefinitionMap.put(SystemUrlConstant.MENU, ShiroConstant.ANON);

        // 静态资源
        filterChainDefinitionMap.put("/static/**", ShiroConstant.ANON);
        filterChainDefinitionMap.put("/js/**", ShiroConstant.ANON);
        filterChainDefinitionMap.put("/css/**", ShiroConstant.ANON);
        filterChainDefinitionMap.put("/fonts/**", ShiroConstant.ANON);
        filterChainDefinitionMap.put("/images/**", ShiroConstant.ANON);
        filterChainDefinitionMap.put("/lib/**", ShiroConstant.ANON);

        // Druid监控平台
        filterChainDefinitionMap.put(SystemUrlConstant.DRUID, ShiroConstant.USER);

        buildNeedPerms(filterChainDefinitionMap);
        filterChainDefinitionMap.put("/**", ShiroConstant.USER);

        return filterChainDefinitionMap;
    }

    private void buildNeedPerms(Map<String, String> filterChainDefinitionMap) {
        Set<String> controllerClass = getAllControllerClass();
        //数据库URL列表
        List<String> urlList = iSystemPermissionService.findAllUrl();
        //反射获取controller  的URL
        controllerClass.forEach(className -> {
            try {
                Class z = Class.forName(className);
                if (z.isAnnotationPresent(RequestMapping.class) && !z.isAnnotationPresent(AllowAccess.class)) {
                    RequestMapping requestMapping = (RequestMapping) z.getAnnotation(RequestMapping.class);
                    String headUrl = restUrl(requestMapping.value()[0]);
                    Method[] methods = arrayReverse(z.getDeclaredMethods());
                    String url;
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(AllowAccess.class)) {
                            continue;
                        }
                        url = getMethodUrl(method);
                        //检查权限是否已经被注册过，跳过自动注册
                        if(!urlList.contains(restUrl(headUrl + url))){
                            autoRegisterUrl(method,restUrl(headUrl + url));
                        }
                        if (StringUtils.isEmpty(url)) {
                            continue;
                        }
                        url = restUrl(headUrl.concat(url));
                        filterChainDefinitionMap.put(url, ShiroConstant.PERMS + "[" + url + "]");
                        urlList.add(url);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void autoRegisterUrl(Method method, String url) {
        if (method.isAnnotationPresent(AutoRegisterUrl.class)) {
            AutoRegisterUrl autoRegisterUrl = method.getAnnotation(AutoRegisterUrl.class);

            SystemPermission saveInfo = new SystemPermission();
            saveInfo.setIcon(autoRegisterUrl.icon());
            saveInfo.setPermissionName(autoRegisterUrl.permissionName());
            saveInfo.setExhibition(autoRegisterUrl.isShow());
            if (!StringUtils.isEmpty(autoRegisterUrl.parentName())) {
                LambdaQueryWrapper<SystemPermission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(SystemPermission::getPermissionName, autoRegisterUrl.parentName());
                List<SystemPermission> permissionList = iSystemPermissionService.list(lambdaQueryWrapper);
                if (permissionList.isEmpty()) {
                    logger.error("自动注册权限失败，原因：Url-【{}】父节点名称【{}】找不到父节点。", url, autoRegisterUrl.parentName());
                    return;
                } else if (permissionList.size() > 1) {
                    logger.error("自动注册权限失败，原因：Url-【{}】父节点名称【{}】找到多个记录，名称重复!", url, autoRegisterUrl.parentName());
                    return;
                }
                SystemPermission parenPermission = permissionList.get(0);
                saveInfo.setUrl(url);
                saveInfo.setParentno(parenPermission.getPermissionId());
            }else{
                //没有Url的菜单栏，无法通过Url区分是否存在于系统，判断是否重复
               if(iSystemPermissionService.count(new LambdaQueryWrapper<SystemPermission>()
                        .eq(SystemPermission::getPermissionName,autoRegisterUrl.permissionName())) > 0){
                   return;
               }
            }
            if (!iSystemPermissionService.save(saveInfo)) {
                logger.error("自动注册权限失败，原因：Url-【{}】保存失败信息失败！", url);
            }
        }

    }


    /**
     * 获取方法上的 value
     *
     * @param method method
     * @return String
     */
    private String getMethodUrl(Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping methodAnnotation = method.getAnnotation(RequestMapping.class);
            return methodAnnotation.value()[0];
        } else if (method.isAnnotationPresent(GetMapping.class)) {
            GetMapping methodAnnotation = method.getAnnotation(GetMapping.class);
            return methodAnnotation.value()[0];
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            PostMapping methodAnnotation = method.getAnnotation(PostMapping.class);
            return methodAnnotation.value()[0];
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            DeleteMapping methodAnnotation = method.getAnnotation(DeleteMapping.class);
            return methodAnnotation.value()[0];
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            PutMapping methodAnnotation = method.getAnnotation(PutMapping.class);
            return methodAnnotation.value()[0];
        }
        return null;
    }

    /**
     * 重置URL
     *
     * @param url url
     * @return String
     */
    private String restUrl(String url) {
        return url.startsWith("/") ? url : "/".concat(url);
    }


    /**
     * 获取容器中所有的Controller全类名
     *
     * @return Set<String>
     */
    private Set<String> getAllControllerClass() {
        //扫描，获取容器中包含Controller注解的bean
        String[] controllerName = applicationContext.getBeanNamesForAnnotation(Controller.class);
        //容器用户自定义Controller 全类名
        Set<String> controllerClassName = new HashSet<>();
        //SpringBoot 自带controller名称
        String name = "basicErrorController";
        for (String value : controllerName) {
            if (value.equals(name)) {
                continue;
            }
            String className = applicationContext.getBean(value).getClass().getName();
            if (className.contains("$")) {
                //代表该类被动态代理过，去除多余的字符
                controllerClassName.add(className.substring(0, className.indexOf("$")));
                continue;
            }
            controllerClassName.add(className);
        }
        return controllerClassName;
    }

    /**
     * 数组倒过来，保证方法是从上往下解析
     *
     * @param methods 方法数组
     * @return 排序后
     */
    private Method[] arrayReverse(Method[] methods) {
        Method[] newMethods = new Method[methods.length];
        for (int i = 0; i < methods.length; i++) {
            newMethods[(methods.length - 1) - i] = methods[i];
        }
        return newMethods;
    }

}
