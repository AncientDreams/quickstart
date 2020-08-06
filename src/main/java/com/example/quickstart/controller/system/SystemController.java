package com.example.quickstart.controller.system;


import com.example.quickstart.bo.MenuNode;
import com.example.quickstart.bo.ResultBody;
import com.example.quickstart.constant.SystemUrlConstant;
import com.example.quickstart.entity.SystemPermission;
import com.example.quickstart.service.ISystemPermissionService;
import com.example.quickstart.service.ISystemUserService;
import com.example.quickstart.utils.RsaUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * <p>
 * 系统管理控制器
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-07-14 9:53
 */
@Controller
public class SystemController {

    private ISystemPermissionService iSystemPermissionService;

    private ISystemUserService iSystemUserService;

    public SystemController(ISystemPermissionService iSystemPermissionService, ISystemUserService iSystemUserService) {
        this.iSystemPermissionService = iSystemPermissionService;
        this.iSystemUserService = iSystemUserService;
    }

    @RequestMapping(SystemUrlConstant.LOGIN)
    public ModelAndView login(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("publicKey", RsaUtil.getRsaPublicKey());
        modelAndView.addObject("message", request.getParameter("message"));
        return modelAndView;
    }

    @RequestMapping(SystemUrlConstant.EXIT)
    public ModelAndView logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return new ModelAndView("redirect:" + SystemUrlConstant.LOGIN);
    }

    @RequestMapping(SystemUrlConstant.INDEX)
    public ModelAndView index() {
        Subject subject = SecurityUtils.getSubject();
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("userInfo",
                iSystemUserService.queryUserByUserName(subject.getPrincipal().toString()));
        return modelAndView;
    }

    @RequestMapping(SystemUrlConstant.WELCOME)
    public ModelAndView welcome() {
        return new ModelAndView("welcome");
    }

    @GetMapping(value = SystemUrlConstant.MENU)
    @ResponseBody
    public List<MenuNode> menuNode() {
        Subject subject = SecurityUtils.getSubject();
        //侧边栏显示
        String exhibition = "1";
        //获取当前登录对象的所有权限
        List<SystemPermission> list = iSystemPermissionService.findByUerName((String) subject.getPrincipal(), exhibition);
        return iSystemPermissionService.buildMenuNode(list, null);
    }

    @GetMapping(value = SystemUrlConstant.MENU + "/treeSelect")
    @ResponseBody
    public List<MenuNode> treeSelect() {
        return iSystemPermissionService.buildMenuNodeForTreeSelect(iSystemPermissionService.list());
    }

    @RequestMapping(value = SystemUrlConstant.PERMISSION_DENIED)
    @ResponseBody
    public ResultBody permissionDenied(HttpServletRequest request) {
        return new ResultBody(false, request.getParameter("message"));
    }

}
