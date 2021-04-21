package com.example.quickstart.config.system;

import com.example.quickstart.annotation.EnableCache;
import com.example.quickstart.annotation.MethodRunLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * Aop configuration center
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-04-09 16:44
 */
@Aspect
@Component
@Slf4j
public class AopConfig {

    /**
     * 缓存线程安全容器
     */
    private final Map<String, CacheProperty> cacheMap = new ConcurrentHashMap<>();

    @Around("@annotation(methodRunLog)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, MethodRunLog methodRunLog) {
        Object obj = null;
        try {
            Class<?> pointClass = proceedingJoinPoint.getTarget().getClass();
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            Method method = pointClass.getDeclaredMethod(signature.getName(),
                    signature.getMethod().getParameterTypes());
            //方法名称
            String methodName = "执行方法名称：" + method.getName()
                    + (StringUtils.isEmpty(methodRunLog.methodName()) ? "，" : "(" + methodRunLog.methodName() + ")，");

            //记录方法参数
            if (methodRunLog.parameters()) {
                Object[] params = proceedingJoinPoint.getArgs();
                String[] requestParams = new String[params.length];
                for (int i = 0; i < params.length; i++) {
                    if (params[i].getClass() == ShiroHttpServletRequest.class) {
                        HttpServletRequest request = (ShiroHttpServletRequest) params[i];
                        requestParams[i] = "Request:" + parameterMapToString(request.getParameterMap());
                        continue;
                    }
                    requestParams[i] = params[i].toString();
                }
                log.info(methodName + "调用参数：{}", Arrays.toString(requestParams));
            }

            //执行方法，记录执行时间
            if (methodRunLog.runTime()) {
                long startTime = System.currentTimeMillis();
                obj = proceedingJoinPoint.proceed();
                float executeTime = (float) (System.currentTimeMillis() - startTime) / 1000;
                log.info(methodName + "耗时：{}s", executeTime);
            } else {
                obj = proceedingJoinPoint.proceed();
            }

            //记录方法返回值
            if (methodRunLog.result()) {
                if (method.getReturnType() != void.class) {
                    log.info(methodName + "方法返回值：{}", obj.toString());
                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return obj;
    }

    @Around("@annotation(enableCache)")
    public Object around1(ProceedingJoinPoint proceedingJoinPoint, EnableCache enableCache) {
        Object obj = null;
        try {
            Class<?> pointClass = proceedingJoinPoint.getTarget().getClass();
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            //全类名加方法名称做key
            String caCheKey = pointClass.getName().concat(".").concat(signature.getName());
            //注解设置的过期时间
            long expirationTime = enableCache.expirationTime();
            if (expirationTime < 0) {
                log.error("开始缓存失败，缓存过期时间不能小于0，位置：{}", caCheKey);
                return proceedingJoinPoint.proceed();
            } else if (expirationTime == 0) {
                //缓存永久有效
                expirationTime = System.currentTimeMillis();
            }
            CacheProperty caCheValue = cacheMap.get(caCheKey);
            if (caCheValue == null || (caCheValue.getExpirationTime() + expirationTime) <= System.currentTimeMillis()) {
                //缓存结果
                obj = proceedingJoinPoint.proceed();
                CacheProperty cacheProperty = new CacheProperty(obj, System.currentTimeMillis());
                cacheMap.put(caCheKey, cacheProperty);
                return obj;
            }
            return caCheValue.getResult();
        } catch (Throwable e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return obj;
    }

    private String parameterMapToString(Map<String, String[]> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object mapKey : map.keySet()) {
            stringBuilder.append("[key:").append(mapKey).append(",value:")
                    .append(Arrays.toString(map.get(mapKey))).append("]");
        }
        return stringBuilder.toString();
    }


    static class CacheProperty {

        /**
         * 返回结果
         */
        private Object result;

        /**
         * 超时时间
         */
        private long expirationTime;


        public Object getResult() {
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
        }

        long getExpirationTime() {
            return expirationTime;
        }

        public void setExpirationTime(int expirationTime) {
            this.expirationTime = expirationTime;
        }

        CacheProperty(Object result, long expirationTime) {
            this.result = result;
            this.expirationTime = expirationTime;
        }
    }
}
