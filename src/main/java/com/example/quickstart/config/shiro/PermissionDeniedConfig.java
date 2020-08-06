package com.example.quickstart.config.shiro;

import com.example.quickstart.config.system.MyHttpServletRequest;
import com.example.quickstart.constant.MessageConstant;
import com.example.quickstart.constant.SystemUrlConstant;
import com.example.quickstart.entity.SystemLog;
import com.example.quickstart.service.ISystemLogService;
import com.example.quickstart.utils.HttpUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Shiro 访问管理
 * <p>
 * 方法：onAccessDenied() 权限认证不通过<br>
 * isAccessAllowed() 允许访问
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-07-22 16:53
 */
public class PermissionDeniedConfig extends PermissionsAuthorizationFilter {

    private Logger logger = LoggerFactory.getLogger(PermissionDeniedConfig.class);

    private ISystemLogService iSystemLogService;

    PermissionDeniedConfig(ISystemLogService iSystemLogService) {
        this.iSystemLogService = iSystemLogService;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String uri = httpServletRequest.getRequestURI().replaceAll(httpServletRequest.getContextPath(), "");
        String message = "您没有访问资源【" + uri + "】权限,请联系管理员";

        Subject subject = SecurityUtils.getSubject();
        if (subject == null || subject.getPrincipal() == null) {
            message = "您的会话已过期请重新登录！";
        }
        httpServletResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
        boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"));
        if (isAjax) {
            //如果是AJAX异步访问，直接重定向到指定的controller异步返回错误信息
            String url = SystemUrlConstant.PERMISSION_DENIED + "?message=" + URLEncoder.encode(message, "UTF-8");
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + url);
            return false;
        }

        byte[] bytes = message.getBytes();
        httpServletResponse.getOutputStream().write(bytes);
        return false;
    }

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.getPrincipal() != null) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String url = httpServletRequest.getRequestURI().replaceAll(httpServletRequest.getContextPath(), "");
            if (filterUrl(url)) {
                SystemLog systemLog = new SystemLog();
                systemLog.setRequestIp(HttpUtil.getRealIp(httpServletRequest));
                systemLog.setServerIp(HttpUtil.getLocaliP());
                systemLog.setUrl(url);
                systemLog.setRequestTime(LocalDateTime.now());
                systemLog.setRequestType(httpServletRequest.getMethod());
                systemLog.setRequestBy(subject.getPrincipal().toString());
                boolean isAjax = "XMLHttpRequest".equalsIgnoreCase((httpServletRequest).getHeader("X-Requested-With"));
                systemLog.setAjax(isAjax);
                //请求参数
                systemLog.setRequestParameter(resolveRequestParameter(httpServletRequest));
                if (!iSystemLogService.save(systemLog)) {
                    logger.error(MessageConstant.SAVE_FAIL + systemLog.toString());
                }
            }
        }

        //MyHttpServletRequest 过滤原请求中的非法字符
        return super.isAccessAllowed(new MyHttpServletRequest((HttpServletRequest) request), response, mappedValue);
    }


    /**
     * 过滤非核心操作URL，非核心操作记录不报错到数据库
     *
     * @param url 请求url
     * @return true：保存
     */
    private boolean filterUrl(String url) {
        List<String> list = new ArrayList<>();
        list.add(SystemUrlConstant.VIEW);
        list.add(SystemUrlConstant.LIST);
        list.add(SystemUrlConstant.WELCOME);
        list.add(SystemUrlConstant.INDEX);
        list.add(SystemUrlConstant.MENU);
        list.add("Page");
        list.add("List");
        for (String param : list) {
            if (url.contains(param)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 请求请求中的请求参数
     *
     * @param request 请求
     * @return String
     */
    private String resolveRequestParameter(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        List<String> list = new ArrayList<>(params.size());
        for (String key : params.keySet()) {
            String[] value = params.get(key);
            if (value.length > 1) {
                list.add(key + "=" + Arrays.toString(value));
                continue;
            }
            list.add(key + "=" + value[0]);
        }
        return list.toString();
    }

}
