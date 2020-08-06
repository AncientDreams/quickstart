package com.example.quickstart.service.impl;

import com.example.quickstart.entity.SystemUserRole;
import com.example.quickstart.mapper.SystemUserRoleMapper;
import com.example.quickstart.service.ISystemUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  系统用户对应的角色服务类接口实现
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-07-13
 */
@Service
public class SystemUserRoleServiceImpl extends ServiceImpl<SystemUserRoleMapper, SystemUserRole> implements ISystemUserRoleService {

}
