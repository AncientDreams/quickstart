package com.example.quickstart.config.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 实现 HttpServletRequestWrapper对象，对 HttpServletRequest 进行重写！
 * 目的:HttpServletRequest不能对Parameter 进行set。重写后加入此功能，方便对请求中的参数过滤
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-07-01 11:06
 */
public class MyHttpServletRequest extends HttpServletRequestWrapper {

    /**
     * 存放request中请求参数
     */
    private Map<String, String[]> params = new ConcurrentHashMap<>();

    public MyHttpServletRequest(HttpServletRequest request) throws UnsupportedEncodingException {
        super(request);
        request.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        //将原请求的请求参数拿到，重新定义
        Map<String, String[]> oldRequestKeys = request.getParameterMap();
        for (String requestKey : oldRequestKeys.keySet()) {
            Object value = request.getParameter(requestKey);
            //过滤特殊字符
            params.put(requestKey, new String[]{filterString(value.toString())});
        }
    }

    @Override
    public String getParameter(String name) {
        //重写getParameter，代表参数从当前类中的map获取
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    @Override
    public String[] getParameterValues(String name) {
        //重写getParameter
        return params.get(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        //getParameterMap
        return params;
    }


    /**
     * 字符过滤
     *
     * @param sourceStr 原字符
     * @return 已过滤字符
     */
    private String filterString(String sourceStr) {
        //过滤字符&
        sourceStr = sourceStr.replaceAll("&", "&amp;");
        //过滤字符;
        sourceStr = sourceStr.replaceAll(";", "");
        //过滤字符'
        sourceStr = sourceStr.replaceAll("'", "");
        //过滤字符<
        sourceStr = sourceStr.replaceAll("<", "&lt;");
        //过滤字符>
        sourceStr = sourceStr.replaceAll(">", "&gt");
        //过滤字符%
        sourceStr = sourceStr.replaceAll("%", "");
//        //过滤字符/  这两个字符暂时不能过滤，影响加解密
//        sourceStr = sourceStr.replaceAll("/", "");
//        //过滤字符=
//        sourceStr = sourceStr.replaceAll("=", "");
        return sourceStr;
    }
}
