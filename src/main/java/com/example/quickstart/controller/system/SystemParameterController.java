package com.example.quickstart.controller.system;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.quickstart.bo.PagingTool;
import com.example.quickstart.bo.ResultBody;
import com.example.quickstart.constant.SystemUrlConstant;
import com.example.quickstart.entity.SystemParameter;
import com.example.quickstart.service.ISystemParameterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-08-03
 */
@Controller
@RequestMapping(SystemUrlConstant.SYSTEM_PARAMETER)
public class SystemParameterController {

    private ISystemParameterService iSystemParameterService;

    public SystemParameterController(ISystemParameterService iSystemParameterService) {
        this.iSystemParameterService = iSystemParameterService;
    }

    @RequestMapping(value = SystemUrlConstant.VIEW)
    public ModelAndView view() {
        return new ModelAndView("system/parameters");
    }

    @RequestMapping(SystemUrlConstant.LIST)
    @ResponseBody
    public PagingTool<SystemParameter> list(@RequestParam(value = "page") int page,
                                            @RequestParam(value = "limit") int limit, SystemParameter systemParameter) {
        return iSystemParameterService.pageList(new Page<>(page, limit), systemParameter);
    }

    @PostMapping(SystemUrlConstant.SAVE)
    @ResponseBody
    public ResultBody save(SystemParameter systemParameter) {
        return iSystemParameterService.addSystemParameter(systemParameter);
    }

    @PostMapping(SystemUrlConstant.UPDATE)
    @ResponseBody
    public ResultBody update(SystemParameter systemParameter) {
        return iSystemParameterService.updateSystemParameter(systemParameter);
    }

    @PostMapping(SystemUrlConstant.REMOVE)
    @ResponseBody
    public ResultBody remove(String parameterKey) {
        return iSystemParameterService.removeByKey(parameterKey);
    }
}
