package com.example.quickstart.service.impl;

import com.example.quickstart.entity.SystemRolePermission;
import com.example.quickstart.mapper.SystemRolePermissionMapper;
import com.example.quickstart.service.ISystemRolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统角色对应的权限服务类接口实现类
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-07-13
 */
@Service
public class SystemRolePermissionServiceImpl extends ServiceImpl<SystemRolePermissionMapper, SystemRolePermission> implements ISystemRolePermissionService {

}
