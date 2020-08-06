package com.example.quickstart.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.quickstart.bo.PagingTool;
import com.example.quickstart.bo.ResultBody;
import com.example.quickstart.constant.MessageConstant;
import com.example.quickstart.constant.SystemUrlConstant;
import com.example.quickstart.entity.SystemPermission;
import com.example.quickstart.service.ISystemPermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * <p>
 * 系统权限控制器
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-07-15 16:14
 */
@RequestMapping(value = SystemUrlConstant.PERMISSION)
@Controller
public class PermissionController {

    private ISystemPermissionService iSystemPermissionService;

    public PermissionController(ISystemPermissionService iSystemPermissionService) {
        this.iSystemPermissionService = iSystemPermissionService;
    }

    @RequestMapping(value = SystemUrlConstant.LIST)
    @ResponseBody
    public ResultBody list() {
        return new ResultBody<>(true, MessageConstant.QUERY_SUCCESS, iSystemPermissionService.list());
    }

    @GetMapping(value = SystemUrlConstant.VIEW)
    public ModelAndView view() {
        return new ModelAndView("system/permission");
    }

    @RequestMapping(value = SystemUrlConstant.SAVE)
    @ResponseBody
    public ResultBody save(SystemPermission permission, HttpServletRequest request) {
        String exhibition = request.getParameter("exhibition");
        permission.setExhibition(!StringUtils.isEmpty(exhibition));
        boolean saveResult = iSystemPermissionService.save(permission);
        return new ResultBody<>(saveResult, saveResult ? MessageConstant.SAVE_SUCCESS : MessageConstant.SAVE_FAIL);
    }

    @RequestMapping(value = SystemUrlConstant.UPDATE)
    @ResponseBody
    public ResultBody update(SystemPermission permission, HttpServletRequest request) {
        return iSystemPermissionService.updateSystemPermission(permission, request);
    }

    @RequestMapping(value = SystemUrlConstant.REMOVE)
    @ResponseBody
    public ResultBody remove(Integer id) {
        return iSystemPermissionService.removeSystemPermissionById(id);
    }


    @RequestMapping(value = "/listPage")
    @ResponseBody
    public PagingTool<SystemPermission> listPage(@RequestParam(value = "page") int page,
                                                 @RequestParam(value = "limit") int limit, SystemPermission permission) {
        return iSystemPermissionService.listPage(new Page<>(page, limit), permission);
    }

    @RequestMapping(value = "/permissionList")
    @ResponseBody
    public ResultBody permissionList(String roleNo) {
        return new ResultBody<>(true, MessageConstant.QUERY_SUCCESS,
                iSystemPermissionService.buildMenuNode(iSystemPermissionService.list(), roleNo));
    }

    @PostMapping(SystemUrlConstant.AUTHORIZATION)
    @ResponseBody
    public ResultBody authorization(String permissionIds, Integer roleId) {
        //处理前端传来的数组字符串，为啥不转数组？方法都试过了，无效
        String nullArrayString = "[]";
        if (nullArrayString.equals(permissionIds)) {
            return iSystemPermissionService.authorization(null, roleId);
        } else {
            String[] ids = permissionIds.substring(1, permissionIds.length() - 1).split(",");
            return iSystemPermissionService.authorization(Arrays.asList(ids), roleId);
        }
    }

    @PostMapping("/buildUrl")
    @ResponseBody
    public ResultBody buildUrl() {
        return iSystemPermissionService.buildUrl();
    }
}
