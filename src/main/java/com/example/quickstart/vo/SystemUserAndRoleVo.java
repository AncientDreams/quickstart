package com.example.quickstart.vo;


import com.example.quickstart.entity.SystemRole;
import lombok.Data;


import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 系统用户和角色一对多关系
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-07-16 15:28
 */
@Data
public class SystemUserAndRoleVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 邮件
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 0-正常
     */
    private String status;

    private List<SystemRole> roleList;

}
