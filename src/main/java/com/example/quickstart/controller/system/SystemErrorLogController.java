package com.example.quickstart.controller.system;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.quickstart.bo.PagingTool;
import com.example.quickstart.bo.R;
import com.example.quickstart.constant.SystemUrlConstant;
import com.example.quickstart.entity.SystemErrorLog;
import com.example.quickstart.service.ISystemErrorLogService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 错误日志前端控制器
 * </p>
 *
 * @author ZhangXianYu
 * @since 2021-04-23
 */
@RestController
@RequestMapping("/systemErrorLog")
@AllArgsConstructor
public class SystemErrorLogController {

    private final ISystemErrorLogService iSystemErrorLogService;

    @RequestMapping(value = SystemUrlConstant.VIEW)
    public ModelAndView modelAndView() {
        return new ModelAndView("system/system_error_log");
    }

    @RequestMapping(SystemUrlConstant.LIST)
    @ResponseBody
    public PagingTool<SystemErrorLog> list(@RequestParam(value = "page") int page,
                                           @RequestParam(value = "limit") int limit, SystemErrorLog systemErrorLog) {
        return iSystemErrorLogService.pageList(new Page<>(page, limit), systemErrorLog);
    }

    @RequestMapping(value = "/solve")
    @ResponseBody
    public R<String> solve(SystemErrorLog systemErrorLog) {
        return iSystemErrorLogService.solve(systemErrorLog);
    }

}
