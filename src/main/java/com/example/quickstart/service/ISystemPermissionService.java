package com.example.quickstart.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.quickstart.bo.MenuNode;
import com.example.quickstart.bo.PagingTool;
import com.example.quickstart.bo.R;
import com.example.quickstart.entity.SystemPermission;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 系统权限服务类接口
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-07-13
 */
public interface ISystemPermissionService extends IService<SystemPermission> {

    /**
     * 获取当前用户的权限
     *
     * @param userName   用户名
     * @param exhibition 侧边栏是否显示
     * @return List<SystemPermission>
     */
    List<SystemPermission> findByUerName(String userName, String exhibition);

    /**
     * 构建菜单节点
     *
     * @param systemPermissions 当前用户的权限
     * @param roleId            角色id
     * @return MenuNode
     */
    List<MenuNode> buildMenuNode(List<SystemPermission> systemPermissions, String roleId);

    /**
     * 为角色授权
     *
     * @param permissionIds 权限列表id数组
     * @param roleId        角色ID
     * @return R
     */
    @Transactional(rollbackFor = Exception.class)
    R<String> authorization(List<String> permissionIds, Integer roleId);

    /**
     * 分页查询权限列表
     *
     * @param page       分页对象
     * @param permission 筛选条件载体
     * @return PagingTool<SystemPermission>
     */
    PagingTool<SystemPermission> listPage(Page<SystemPermission> page, SystemPermission permission);

    /**
     * 构建TreeSelect 所需菜单节点
     *
     * @param systemPermissions 当前用户的权限
     * @return MenuNode
     */
    List<MenuNode> buildMenuNodeForTreeSelect(List<SystemPermission> systemPermissions);

    /**
     * 修改权限信息
     *
     * @param permission 参数载体
     * @param request    请求对象
     * @return R
     */
    R<String> updateSystemPermission(SystemPermission permission, HttpServletRequest request);

    /**
     * 删除权限信息，递归删除
     *
     * @param id 权限id
     * @return R
     */
    R<String> removeSystemPermissionById(Integer id);

    /**
     * 构建未注册的url，controller中指定了@requestMapper却没有在数据库中保存的Url
     *
     * @return R
     */
    R<List<String>> buildUrl();

    /**
     * 查询数据库中所有的权限Url
     *
     * @return List<String>
     */
    List<String> findAllUrl();
}
