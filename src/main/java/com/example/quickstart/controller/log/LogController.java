package com.example.quickstart.controller.log;

import com.example.quickstart.annotation.AllowAccess;
import com.example.quickstart.bo.R;
import com.example.quickstart.constant.SystemUrlConstant;
import com.example.quickstart.service.ILogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


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
@AllArgsConstructor
public class LogController {

    private final ILogService iLogService;

    @RequestMapping(value = SystemUrlConstant.VIEW)
    public ModelAndView view() {
        return new ModelAndView("log/logger");
    }

    @RequestMapping(value = SystemUrlConstant.LIST)
    @ResponseBody
    public R<List<String>> list(String date) {
        return iLogService.queryTimeByDate(date);
    }

    @RequestMapping(value = "/queryLog")
    @ResponseBody
    public R<List<String>> queryLogFileInfo(HttpServletRequest request) {
        return iLogService.queryLogFileInfo(request);
    }

    @RequestMapping(value = "/getFileSize")
    @ResponseBody
    @AllowAccess
    public R<String> getFileSize(HttpServletRequest request) {
        return iLogService.getFileSize(request);
    }


}

