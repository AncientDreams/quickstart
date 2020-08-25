package com.example.quickstart.controller.example;

import com.example.quickstart.annotation.AutoRegisterUrl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * {@link com.example.quickstart.annotation.AutoRegisterUrl} 注解使用实例 <br>
 * <p>
 * <p>
 * 注意： 解析是从上往下解析，所以最外层的在上面，避免出现找不到父节点的问题。
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-07-24 15:08
 */
@RequestMapping(value = "/test")
@Controller
public class AutoRegisterUrlExampleController {


    /**
     * 菜单，无需@requestMapper，它无实际用处，只是用来分类。不指定父节点，表示最外层分类菜单.
     * 因为是本类中的其他权限的父节点，要保证它优先注册，所有使用属性 : priority
     */
    @AutoRegisterUrl(permissionName = "测试自动注册", isShow = true, priority = 1)
    public void menu() {

    }

    /**
     * 功能，需指定@requestMapper 、父节点
     */
    @AutoRegisterUrl(permissionName = "查询页面",
            isShow = false, parentName = "测试自动注册")
    @RequestMapping(value = "/view.do")
    public void view() {
    }

    /**
     * 功能，需指定@requestMapper 、父节点
     */
    @AutoRegisterUrl(permissionName = "修改", isShow = false, parentName = "查询页面")
    @RequestMapping(value = "/update.do")
    public void update() {
    }

}
