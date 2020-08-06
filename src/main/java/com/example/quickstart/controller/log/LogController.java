package com.example.quickstart.controller.log;

import com.example.quickstart.bo.ResultBody;
import com.example.quickstart.constant.SystemUrlConstant;
import com.example.quickstart.service.ILogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


/**
 * <p>
 * 系统日志控制器
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-07-29 10:55
 */
@Controller
@RequestMapping(value = "/log")
public class LogController {

    private ILogService iLogService;

    public LogController(ILogService iLogService) {
        this.iLogService = iLogService;
    }

    @RequestMapping(value = SystemUrlConstant.VIEW)
    public ModelAndView view() {
        return new ModelAndView("log/logger");
    }

    @RequestMapping(value = SystemUrlConstant.LIST)
    @ResponseBody
    public ResultBody list(String date) {
        return iLogService.queryTimeByDate(date);
    }

    @RequestMapping(value = "/queryLog")
    @ResponseBody
    public ResultBody queryLogFileInfo(HttpServletRequest request) {
        return iLogService.queryLogFileInfo(request);
    }

}

