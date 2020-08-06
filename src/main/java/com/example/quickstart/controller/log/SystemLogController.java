package com.example.quickstart.controller.log;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.quickstart.bo.PagingTool;
import com.example.quickstart.bo.ResultBody;
import com.example.quickstart.constant.SystemUrlConstant;
import com.example.quickstart.entity.SystemLog;
import com.example.quickstart.service.ISystemLogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户操作日志前端控制器
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-07-29
 */
@Controller
@RequestMapping("/systemLog")
public class SystemLogController {

    private ISystemLogService iSystemLogService;

    public SystemLogController(ISystemLogService iSystemLogService) {
        this.iSystemLogService = iSystemLogService;
    }

    @GetMapping(value = SystemUrlConstant.VIEW)
    public ModelAndView view() {
        return new ModelAndView("log/log");
    }

    @GetMapping(value = SystemUrlConstant.LIST)
    @ResponseBody
    public PagingTool<SystemLog> list(HttpServletRequest request, int limit, int page) {
        return iSystemLogService.pageList(request, new Page<>(page, limit));
    }

    @PostMapping(value = SystemUrlConstant.REMOVE)
    @ResponseBody
    public ResultBody delete(HttpServletRequest request) {
        return iSystemLogService.delete(request);
    }
}
