package com.example.quickstart.service;

import com.example.quickstart.bo.R;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 日志服务接口
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-07-29 11:11
 */
public interface ILogService {

    /**
     * 根据日期查询对应的日志时段， 如：2020-07-29  这一天 10、11、12、13 点的日志
     *
     * @param date 日期
     * @return R
     */
    R<List<String>> queryTimeByDate(String date);

    /**
     * 根据日志文件名称查询对应的日志信息
     *
     * @param request 请求
     * @return R
     */
    R<List<String>> queryLogFileInfo(HttpServletRequest request);

    /**
     * 查询日志前先获取日志大小
     *
     * @param httpServletRequest 请求参数
     * @return R
     */
    R<String> getFileSize(HttpServletRequest httpServletRequest);
}
