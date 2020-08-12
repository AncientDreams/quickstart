package com.example.quickstart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.quickstart.bo.MenuNode;
import com.example.quickstart.bo.PagingTool;
import com.example.quickstart.bo.ResultBody;
import com.example.quickstart.config.shiro.ShiroConfig;
import com.example.quickstart.constant.MessageConstant;
import com.example.quickstart.entity.SystemPermission;
import com.example.quickstart.entity.SystemRolePermission;
import com.example.quickstart.mapper.SystemPermissionMapper;
import com.example.quickstart.service.ISystemPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.quickstart.service.ISystemRolePermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统权限服务类接口实现
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-07-13
 */
@Service
public class SystemPermissionServiceImpl extends ServiceImpl<SystemPermissionMapper, SystemPermission> implements ISystemPermissionService {

    private Logger logger = LoggerFactory.getLogger(SystemPermissionServiceImpl.class);

    private SystemPermissionMapper systemPermissionMapper;

    private ISystemRolePermissionService iSystemRolePermissionService;

    public SystemPermissionServiceImpl(SystemPermissionMapper systemPermissionMapper,
                                       ISystemRolePermissionService iSystemRolePermissionService) {
        this.systemPermissionMapper = systemPermissionMapper;
        this.iSystemRolePermissionService = iSystemRolePermissionService;
    }

    @Override
    public List<SystemPermission> findByUerName(String userName, String exhibition) {
        return systemPermissionMapper.findByUerName(userName, exhibition);
    }

    @Override
    public List<MenuNode> buildMenuNode(List<SystemPermission> systemPermissions, String roleId) {
        return buildMenu(systemPermissions, roleId, true);
    }

    @Override
    public ResultBody authorization(List<String> permissionIds, Integer roleId) {
        try {
            //获取角色的所有拥有权限id
            List<Integer> ids = systemPermissionMapper.findPermissionIdByRoleId(roleId.toString());
            List<SystemRolePermission> systemRolePermissions = new ArrayList<>();
            if (permissionIds != null) {
                for (String permissionId : permissionIds) {
                    if (ids.contains(Integer.parseInt(permissionId))) {
                        //角色已拥有此权限
                        continue;
                    }
                    SystemRolePermission systemRolePermission = new SystemRolePermission();
                    systemRolePermission.setRoleId(roleId);
                    systemRolePermission.setPermissionId(Integer.parseInt(permissionId));
                    systemRolePermissions.add(systemRolePermission);
                }
            }

            //mybatis-plus批量新增是原子性的
            if (!systemRolePermissions.isEmpty()) {
                if (!iSystemRolePermissionService.saveBatch(systemRolePermissions)) {
                    logger.error("授权失败，批量插入异常。permissionIds：{}，roleId：{}", permissionIds, roleId);
                    //失败直接返回
                    return new ResultBody(false, MessageConstant.GRANT_FAIL);
                }
            }

            if (permissionIds == null) {
                //如果为空，代表去除角色所有的权限
                if (!iSystemRolePermissionService.remove(new LambdaQueryWrapper<SystemRolePermission>().
                        eq(SystemRolePermission::getRoleId, roleId))) {
                    logger.error("授权失败，批量删除异常。roleId：{}", roleId);
                    throw new Exception(MessageConstant.GRANT_FAIL);
                }
            } else {
                //角色需要删除的权限id
                List<Integer> removePermissionIds = new ArrayList<>();
                //检查此次授权 移除了哪些权限
                ids.forEach(permissionId -> {
                    if (!permissionIds.contains(permissionId.toString())) {
                        removePermissionIds.add(permissionId);
                    }
                });

                //批量删除
                if (!removePermissionIds.isEmpty()) {
                    LambdaQueryWrapper<SystemRolePermission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper.eq(SystemRolePermission::getRoleId, roleId)
                            .in(SystemRolePermission::getPermissionId, removePermissionIds);
                    if (!iSystemRolePermissionService.remove(lambdaQueryWrapper)) {
                        logger.error("授权失败，批量删除异常。removePermissionIds：{}，roleId：{}", removePermissionIds, roleId);
                        throw new Exception(MessageConstant.GRANT_FAIL);
                    }
                }
            }

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return new ResultBody(false, e.getMessage());
        }
        return new ResultBody(true, MessageConstant.GRANT_SUCCESS);
    }

    @Override
    public PagingTool<SystemPermission> listPage(Page<SystemPermission> page, SystemPermission permission) {
        PagingTool<SystemPermission> systemPermissionPagingTool = new PagingTool<>();
        LambdaQueryWrapper<SystemPermission> systemPermissionLambdaQueryWrapper =
                new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(permission.getPermissionName())) {
            systemPermissionLambdaQueryWrapper.like(SystemPermission::getPermissionName, permission.getPermissionName());
        }
        if (!StringUtils.isEmpty(permission.getParentno())) {
            systemPermissionLambdaQueryWrapper.eq(SystemPermission::getParentno, permission.getParentno());
        }
        IPage<SystemPermission> systemPermissionPage = systemPermissionMapper.
                selectPage(page, systemPermissionLambdaQueryWrapper);
        systemPermissionPagingTool.setData(systemPermissionPage.getRecords());
        systemPermissionPagingTool.setCount(systemPermissionPage.getTotal());
        return systemPermissionPagingTool;
    }

    @Override
    public List<MenuNode> buildMenuNodeForTreeSelect(List<SystemPermission> systemPermissions) {
        return buildMenu(systemPermissions, null, false);
    }

    @Override
    public ResultBody updateSystemPermission(SystemPermission permission, HttpServletRequest request) {
        try {
            String exhibition = request.getParameter("exhibition");
            permission.setExhibition(!StringUtils.isEmpty(exhibition));
            if (systemPermissionMapper.updateById(permission) < 1) {
                return new ResultBody(false, MessageConstant.UPDATE_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultBody(false, MessageConstant.UPDATE_FAIL);
        }
        return new ResultBody(true, MessageConstant.UPDATE_SUCCESS);
    }

    @Override
    public ResultBody removeSystemPermissionById(Integer id) {
        List<SystemPermission> systemPermissions = systemPermissionMapper.selectList(null);
        List<Integer> ids = findAllChildrenId(systemPermissions, id);
        ids.add(id);
        if (removeByIds(ids)) {
            return new ResultBody(true, MessageConstant.REMOVE_SUCCESS);
        }
        return new ResultBody(false, MessageConstant.REMOVE_FAIL);
    }

    @Override
    public ResultBody buildUrl() {
        List<String> controllerUrl = ShiroConfig.urlList;
        List<String> urlList = new ArrayList<>(controllerUrl.size());
        try {
            List<String> list = systemPermissionMapper.findAllUrl();
            controllerUrl.forEach(url -> {
                if (!list.contains(url)) {
                    urlList.add(url);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage(), e);
            return new ResultBody(false, MessageConstant.EXCEPTION);
        }
        return new ResultBody<>(true, MessageConstant.QUERY_SUCCESS, urlList);
    }

    @Override
    public List<String> findAllUrl() {
        return systemPermissionMapper.findAllUrl();
    }


    /**
     * 构建菜单
     *
     * @param systemPermissions 权限列表
     * @param roleId            角色id
     * @param icon              是否需要icon，与TreeSelect做区分
     * @return List<MenuNode>
     */
    private List<MenuNode> buildMenu(List<SystemPermission> systemPermissions, String roleId, boolean icon) {
        List<Integer> permissionIds = systemPermissionMapper.findPermissionIdByRoleId(roleId);
        List<MenuNode> menuNodes = new ArrayList<>();
        for (SystemPermission permission : systemPermissions) {
            if (permission.getParentno() == null) {
                MenuNode menuNode = new MenuNode(permission);
                if (icon) {
                    menuNode.setIcon(permission.getIcon());
                }
                List<MenuNode> menuNodeList = buildMenuNode(systemPermissions, permission.getPermissionId(), permissionIds, icon);
                menuNode.setChildren(menuNodeList);
                menuNodes.add(menuNode);
            }
        }
        return menuNodes;
    }

    /**
     * 递归读取子节点
     *
     * @param systemPermissions 对象拥有权限的所有节点
     * @param parentNo          父节点
     * @param permissionIds     角色拥有的节点id
     * @param icon              是否需要图标属性
     * @return List<MenuNode>
     */
    private List<MenuNode> buildMenuNode(List<SystemPermission> systemPermissions, Integer parentNo, List<Integer> permissionIds, boolean icon) {
        List<MenuNode> menuNodes = new ArrayList<>();
        for (SystemPermission permission : systemPermissions) {
            if (permission.getParentno() != null && permission.getParentno().intValue() == parentNo.intValue()) {
                MenuNode menuNode = new MenuNode(permission);
                if (icon) {
                    menuNode.setIcon(permission.getIcon());
                }
                List<MenuNode> menuNodeList = buildMenuNode(systemPermissions, permission.getPermissionId(), permissionIds, icon);
                if (menuNodeList.isEmpty() && permissionIds.contains(permission.getPermissionId())) {
                    menuNode.setChecked(true);
                }
                menuNode.setChildren(menuNodeList);
                menuNodes.add(menuNode);
            }
        }
        return menuNodes;
    }

    /**
     * 查询所有的子节点ID
     *
     * @param systemPermissions 所有权限
     * @param id                父节点id
     * @return List<Integer>
     */
    private List<Integer> findAllChildrenId(List<SystemPermission> systemPermissions, Integer id) {
        List<Integer> ids = new ArrayList<>();
        systemPermissions.forEach(permission -> {
            if (permission.getParentno() != null && permission.getParentno().equals(id)) {
                ids.add(permission.getPermissionId());
                ids.addAll(findAllChildrenId(systemPermissions, permission.getPermissionId()));
            }
        });
        return ids;
    }

}
