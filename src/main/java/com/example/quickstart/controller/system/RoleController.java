package com.example.quickstart.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.quickstart.bo.PagingTool;
import com.example.quickstart.bo.R;
import com.example.quickstart.constant.ResultConstant;
import com.example.quickstart.constant.SystemUrlConstant;
import com.example.quickstart.entity.SystemRole;
import com.example.quickstart.service.ISystemRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


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
@AllArgsConstructor
public class RoleController {

    private final ISystemRoleService iSystemRoleService;

    @GetMapping(SystemUrlConstant.VIEW)
    public ModelAndView view() {
        return new ModelAndView("system/role");
    }

    @RequestMapping(SystemUrlConstant.LIST)
    public R<List<SystemRole>> list() {
        return R.success(ResultConstant.QUERY_SUCCESS, iSystemRoleService.list());
    }

    @RequestMapping(SystemUrlConstant.INIT)
    public PagingTool<SystemRole> init(@RequestParam(value = "page") int page,
                                       @RequestParam(value = "limit") int limit) {
        return iSystemRoleService.init(new Page<>(page, limit));
    }

    @PostMapping(SystemUrlConstant.SAVE)
    public R<String> save(SystemRole systemRole) {
        return iSystemRoleService.saveSystemRole(systemRole);
    }

    @PostMapping(SystemUrlConstant.UPDATE)
    public R<String> update(SystemRole systemRole) {
        if (iSystemRoleService.updateById(systemRole)) {
            return R.success(ResultConstant.UPDATE_SUCCESS);
        }
        return R.fail(ResultConstant.UPDATE_FAIL);
    }

    @PostMapping(SystemUrlConstant.REMOVE)
    public R<String> delete(SystemRole systemRole) {
        return iSystemRoleService.deleteSystemRole(systemRole);
    }

}
