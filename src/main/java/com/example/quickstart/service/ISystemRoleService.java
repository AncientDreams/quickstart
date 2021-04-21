package com.example.quickstart.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.quickstart.bo.PagingTool;
import com.example.quickstart.bo.R;
import com.example.quickstart.entity.SystemRole;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统角色实体类服务接口
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-07-13
 */
public interface ISystemRoleService extends IService<SystemRole> {


    /**
     * 分页查询系统所有角色信息
     *
     * @param page 分页对象
     * @return PagingTool<SystemRole>
     */
    PagingTool<SystemRole> init(Page<SystemRole> page);

    /**
     * 新增角色信息
     *
     * @param systemRole 角色信息
     * @return R
     */
    R<String> saveSystemRole(SystemRole systemRole);

    /**
     * 删除角色信息，那么也要清除对应的于用户的关系和与权限的关系
     *
     * @param systemRole systemRole
     * @return R
     */
    @Transactional(rollbackFor = Exception.class)
    R<String> deleteSystemRole(SystemRole systemRole);

}
