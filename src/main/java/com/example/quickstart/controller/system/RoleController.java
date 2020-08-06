package com.example.quickstart.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.quickstart.bo.PagingTool;
import com.example.quickstart.bo.ResultBody;
import com.example.quickstart.constant.MessageConstant;
import com.example.quickstart.constant.SystemUrlConstant;
import com.example.quickstart.entity.SystemRole;
import com.example.quickstart.service.ISystemRoleService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


/**
 * <p>
 * 系统角色控制器
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-07-15 16:42
 */
@RestController
@RequestMapping(value = SystemUrlConstant.ROLE)
public class RoleController {

    private ISystemRoleService iSystemRoleService;

    public RoleController(ISystemRoleService iSystemRoleService) {
        this.iSystemRoleService = iSystemRoleService;
    }

    @GetMapping(SystemUrlConstant.VIEW)
    public ModelAndView view() {
        return new ModelAndView("system/role");
    }

    @GetMapping(value = "/addRolePage")
    public ModelAndView addRolePage() {
        return new ModelAndView("system/role-add");
    }

    @RequestMapping(SystemUrlConstant.LIST)
    public ResultBody list() {
        return new ResultBody<>(true, MessageConstant.QUERY_SUCCESS, iSystemRoleService.list());
    }

    @RequestMapping(SystemUrlConstant.INIT)
    public PagingTool<SystemRole> init(@RequestParam(value = "page") int page,
                                       @RequestParam(value = "limit") int limit) {
        return iSystemRoleService.init(new Page<>(page, limit));
    }

    @PostMapping(SystemUrlConstant.SAVE)
    public ResultBody save(SystemRole systemRole) {
        return iSystemRoleService.saveSystemRole(systemRole);
    }

    @PostMapping(SystemUrlConstant.UPDATE)
    public ResultBody update(SystemRole systemRole) {
        if (iSystemRoleService.updateById(systemRole)) {
            return new ResultBody(true, MessageConstant.UPDATE_SUCCESS);
        }
        return new ResultBody(false, MessageConstant.UPDATE_FAIL);
    }

    @PostMapping(SystemUrlConstant.REMOVE)
    public ResultBody delete(SystemRole systemRole) {
        return iSystemRoleService.deleteSystemRole(systemRole);
    }

}
