package com.example.quickstart.mapper;

import com.example.quickstart.entity.SystemPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统权限Mapper 接口
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-07-13
 */
public interface SystemPermissionMapper extends BaseMapper<SystemPermission> {

    /**
     * 通过用户名查询拥有的权
     *
     * @param userName   用户名
     * @param exhibition 侧边栏是否显示
     * @return List<SystemPermission>
     */
    List<SystemPermission> findByUerName(@Param("userName") String userName, @Param("exhibition") String exhibition);

    /**
     * 通过角色ID 获取角色的所有拥有权限
     *
     * @param roleId 角色id
     * @return List<Integer>
     */
    List<Integer> findPermissionIdByRoleId(@Param("roleId") String roleId);

    /**
     * 查询所有的Url
     *
     * @return List<String>
     */
    List<String> findAllUrl();
}
