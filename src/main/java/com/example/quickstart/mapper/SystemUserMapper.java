package com.example.quickstart.mapper;

import com.example.quickstart.entity.SystemUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.quickstart.vo.SystemUserAndRoleVo;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 * 系统用户Mapper 接口
 * </p>
 *
 * @author ZhangXianYu
 * @since 2020-07-13
 */
public interface SystemUserMapper extends BaseMapper<SystemUser> {

    SystemUserAndRoleVo queryUserRole(@Param("userId") String userId);

}
