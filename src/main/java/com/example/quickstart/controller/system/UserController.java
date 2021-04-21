package com.example.quickstart.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.quickstart.annotation.AllowAccess;
import com.example.quickstart.bo.PagingTool;
import com.example.quickstart.bo.R;
import com.example.quickstart.constant.SystemUrlConstant;
import com.example.quickstart.entity.SystemRole;
import com.example.quickstart.entity.SystemUser;
import com.example.quickstart.service.ISystemUserService;
import com.example.quickstart.vo.SystemUserAndRoleVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


/**
 * <p>
 * 用户控制器
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-07-13 16:24
 */
@Controller
@RequestMapping(value = SystemUrlConstant.USER)
@AllArgsConstructor
public class UserController {

    private final ISystemUserService iSystemUserService;

    @AllowAccess
    @PostMapping(SystemUrlConstant.LOGIN)
    @ResponseBody
    public R<String> login(HttpServletRequest request) {
        return iSystemUserService.login(request);
    }

    @RequestMapping(SystemUrlConstant.VIEW)
    public ModelAndView view() {
        return new ModelAndView("system/user-list");
    }


    @RequestMapping(SystemUrlConstant.LIST)
    @ResponseBody
    public PagingTool<SystemUser> list(@RequestParam(value = "page") int page,
                                       @RequestParam(value = "limit") int limit, SystemUser systemUser) {
        return iSystemUserService.pageList(new Page<>(page, limit), systemUser);
    }

    @PostMapping(SystemUrlConstant.SAVE)
    @ResponseBody
    public R<String> addUser(HttpServletRequest request) {
        return iSystemUserService.addUser(request);
    }

    @RequestMapping("/addPage")
    @ResponseBody
    public ModelAndView addPage() {
        return new ModelAndView("system/user-add");
    }

    @RequestMapping("/updatePage")
    @ResponseBody
    public ModelAndView updatePage(String uid) {
        ModelAndView modelAndView = new ModelAndView("system/user-edit");
        SystemUserAndRoleVo systemUserAndRole = iSystemUserService.queryUserRole(uid);
        modelAndView.addObject("userInfo", systemUserAndRole);

        //获取用户所拥有的角色，前端转数组
        StringBuilder stringBuilder = new StringBuilder();
        for (SystemRole role : systemUserAndRole.getRoleList()) {
            if (role.getRoleNo().equals(systemUserAndRole.getRoleList().get(systemUserAndRole.getRoleList().size() - 1).getRoleNo())) {
                stringBuilder.append(role.getRoleNo());
            } else {
                stringBuilder.append(role.getRoleNo()).append(",");
            }
        }
        modelAndView.addObject("roles", stringBuilder);
        return modelAndView;
    }

    @PostMapping(SystemUrlConstant.UPDATE)
    @ResponseBody
    public R<String> update(SystemUser systemUser, HttpServletRequest request) {
        return iSystemUserService.updateUserInfo(request, systemUser);
    }

    @PostMapping(value = "/resetPassword")
    @ResponseBody
    public R<String> resetPassword(HttpServletRequest request) {
        return iSystemUserService.resetPassword(request);
    }

    @PostMapping(value = SystemUrlConstant.REMOVE)
    @ResponseBody
    public R<String> removeUser(HttpServletRequest request) {
        return iSystemUserService.removeUserByUserId(request);
    }


    /**
     * 用户修改用户自身信息
     */
    @PostMapping(value = "/updateUserInfo")
    @ResponseBody
    @AllowAccess
    public R<String> updateUserInfoByUser(SystemUser systemUser) {
        return iSystemUserService.updateUserInfoByUser(systemUser);
    }

    @PostMapping(value = "/updatePassWord")
    @ResponseBody
    @AllowAccess
    public R<String> updatePassWord(HttpServletRequest request) {
        return iSystemUserService.updatePassWord(request);
    }

}
