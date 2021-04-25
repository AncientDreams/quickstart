/*
Navicat MySQL Data Transfer

Target Server Type    : MYSQL
Target Server Version : 50647
File Encoding         : 65001

Date: 2020-08-06 14:45:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for system_log
-- ----------------------------
DROP TABLE IF EXISTS `system_log`;
CREATE TABLE `system_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `request_ip` varchar(50) NOT NULL COMMENT '请求ip',
  `server_ip` varchar(50) NOT NULL COMMENT '服务器ip',
  `url` varchar(50) NOT NULL COMMENT '请求地址',
  `request_type` varchar(10) NOT NULL COMMENT '请求类型，如：post、get',
  `request_parameter` varchar(2000) DEFAULT NULL,
  `is_ajax` tinyint(4) NOT NULL COMMENT '是否是ajax请求 ',
  `request_time` datetime NOT NULL COMMENT '请求时间',
  `request_by` varchar(10) NOT NULL COMMENT '请求创建人',
  `flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
  PRIMARY KEY (`log_id`,`is_ajax`)
) ENGINE=InnoDB AUTO_INCREMENT=723 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for system_parameter
-- ----------------------------
DROP TABLE IF EXISTS `system_parameter`;
CREATE TABLE `system_parameter` (
  `parameter_id` int(11) NOT NULL AUTO_INCREMENT,
  `parameter_name` varchar(32) NOT NULL COMMENT '参数名称',
  `parameter_key` varchar(32) NOT NULL COMMENT '参数键名',
  `parameter_value` varchar(200) NOT NULL COMMENT '参数键值',
  `creation_time` datetime NOT NULL COMMENT '创建时间',
  `flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
  PRIMARY KEY (`parameter_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for system_permission
-- ----------------------------
DROP TABLE IF EXISTS `system_permission`;
CREATE TABLE `system_permission` (
  `permission_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限编号',
  `permission_name` varchar(20) NOT NULL COMMENT '权限名',
  `url` varchar(255) DEFAULT NULL COMMENT '请求地址',
  `icon` varchar(30) DEFAULT NULL COMMENT '显示菜单图标',
  `exhibition` tinyint(1) DEFAULT NULL COMMENT '是否在菜单中显示',
  `parentno` int(11) DEFAULT NULL COMMENT '父级权限编号',
  PRIMARY KEY (`permission_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=159 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of system_permission
-- ----------------------------
INSERT INTO `system_permission` VALUES ('1', '系统管理', '', 'layui-icon-set-sm', '1', null);
INSERT INTO `system_permission` VALUES ('2', '用户列表分页查询', '/user/list', 'layui-icon-login-weibo', '0', '5');
INSERT INTO `system_permission` VALUES ('5', '用户管理', '/user/view', 'layui-icon-user', '1', '1');
INSERT INTO `system_permission` VALUES ('6', '用户添加页面', '/user/addPage', 'layui-icon-left', '0', '5');
INSERT INTO `system_permission` VALUES ('7', '权限列表', '/permission/list', 'layui-icon-left', '0', '5');
INSERT INTO `system_permission` VALUES ('8', '角色列表', '/role/init', 'layui-icon-left', '0', '9');
INSERT INTO `system_permission` VALUES ('9', '角色管理', '/role/view', 'layui-icon-username', '1', '1');
INSERT INTO `system_permission` VALUES ('10', '权限管理', '/permission/view', 'layui-icon-auz', '1', '1');
INSERT INTO `system_permission` VALUES ('11', '权限分页列表', '/permission/listPage', 'layui-icon-left', '0', '10');
INSERT INTO `system_permission` VALUES ('12', '保存权限', '/permission/save', 'layui-icon-left', '0', '10');
INSERT INTO `system_permission` VALUES ('13', '查询角色', '/role/list', 'layui-icon-left', '0', '9');
INSERT INTO `system_permission` VALUES ('14', '添加角色页面', '/role/addRolePage', 'layui-icon-left', '0', '9');
INSERT INTO `system_permission` VALUES ('15', '权限列表', '/permission/permissionList', 'layui-icon-left', '0', '10');
INSERT INTO `system_permission` VALUES ('16', '角色授权', '/permission/authorization', 'layui-icon-left', '0', '9');
INSERT INTO `system_permission` VALUES ('128', '修改权限', '/permission/update', 'layui-icon-right', '0', '10');
INSERT INTO `system_permission` VALUES ('129', '删除权限', '/permission/remove', 'layui-icon-right', '0', '10');
INSERT INTO `system_permission` VALUES ('130', '角色保存', '/role/save', 'layui-icon-right', '0', '9');
INSERT INTO `system_permission` VALUES ('131', '角色修改', '/role/update', 'layui-icon-right', '0', '9');
INSERT INTO `system_permission` VALUES ('132', '角色删除', '/role/remove', 'layui-icon-right', '0', '9');
INSERT INTO `system_permission` VALUES ('133', '用户保存', '/user/save', 'layui-icon-right', '0', '5');
INSERT INTO `system_permission` VALUES ('136', '重置密码', '/user/resetPassword', 'layui-icon-right', '0', '5');
INSERT INTO `system_permission` VALUES ('138', '构建Url', '/permission/buildUrl', 'layui-icon-right', '0', '10');
INSERT INTO `system_permission` VALUES ('139', '修改用户', '/user/update', 'layui-icon-right', '0', '5');
INSERT INTO `system_permission` VALUES ('140', '修改用户页面', '/user/updatePage', 'layui-icon-right', '0', '5');
INSERT INTO `system_permission` VALUES ('141', '删除用户', '/user/remove', 'layui-icon-right', '0', '5');
INSERT INTO `system_permission` VALUES ('142', '系统日志', '/log/view', 'layui-icon-tabs', '1', '145');
INSERT INTO `system_permission` VALUES ('143', '查询日志', '/log/list', 'layui-icon-right', '0', '142');
INSERT INTO `system_permission` VALUES ('144', '查询日志文件', '/log/queryLog', 'layui-icon-right', '0', '142');
INSERT INTO `system_permission` VALUES ('145', '日志监控', '', 'layui-icon-chart-screen', '1', null);
INSERT INTO `system_permission` VALUES ('146', '操作日志', '/systemLog/view', 'layui-icon-template', '1', '145');
INSERT INTO `system_permission` VALUES ('147', '操作日志查询', '/systemLog/list', 'layui-icon-right', '0', '146');
INSERT INTO `system_permission` VALUES ('148', '删除操作记录', '/systemLog/remove', 'layui-icon-right', '0', '146');
INSERT INTO `system_permission` VALUES ('149', '开发工具', '', 'layui-icon-set-fill', '1', null);
INSERT INTO `system_permission` VALUES ('150', '数据库控制台', '/tool/view', 'layui-icon-template-1', '1', '149');
INSERT INTO `system_permission` VALUES ('151', '查询', '/tool/executeQuery.do', 'layui-icon-right', '0', '150');
INSERT INTO `system_permission` VALUES ('152', '参数管理', '/systemParameter/view', 'layui-icon-tabs', '1', '1');
INSERT INTO `system_permission` VALUES ('153', '参数分页查询', '/systemParameter/list', 'layui-icon-right', '0', '152');
INSERT INTO `system_permission` VALUES ('154', '添加参数', '/systemParameter/save', 'layui-icon-right', '0', '152');
INSERT INTO `system_permission` VALUES ('155', '删除参数', '/systemParameter/remove', 'layui-icon-right', '0', '152');
INSERT INTO `system_permission` VALUES ('156', '修改参数信息', '/systemParameter/update', 'layui-icon-right', '0', '152');
INSERT INTO `system_permission` VALUES ('157', '数据库查询', '/tool/executeQuery', 'layui-icon-right', '0', '150');
INSERT INTO `system_permission` VALUES ('158', 'Druid Monitor', '/druid/login', 'layui-icon-unlink', '1', '145');

-- ----------------------------
-- Table structure for system_role
-- ----------------------------
DROP TABLE IF EXISTS `system_role`;
CREATE TABLE `system_role` (
  `role_no` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色编号',
  `role_name` varchar(10) NOT NULL COMMENT '角色名称',
  `role_describe` varchar(255) DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (`role_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of system_role
-- ----------------------------
INSERT INTO `system_role` VALUES ('1', '管理员', '系统最高级管理员');
INSERT INTO `system_role` VALUES ('2', '测试', '测试人员');
INSERT INTO `system_role` VALUES ('3', '客服', '客服');
INSERT INTO `system_role` VALUES ('4', '开发', '系统开发人员');
INSERT INTO `system_role` VALUES ('40', '游客', '只能访问简单功能');

-- ----------------------------
-- Table structure for system_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `system_role_permission`;
CREATE TABLE `system_role_permission` (
  `role_permission_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`role_permission_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=991 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of system_role_permission
-- ----------------------------
INSERT INTO `system_role_permission` VALUES ('1', '1', '1');
INSERT INTO `system_role_permission` VALUES ('864', '4', '1');
INSERT INTO `system_role_permission` VALUES ('870', '4', '9');
INSERT INTO `system_role_permission` VALUES ('874', '2', '1');
INSERT INTO `system_role_permission` VALUES ('875', '2', '5');
INSERT INTO `system_role_permission` VALUES ('878', '2', '7');
INSERT INTO `system_role_permission` VALUES ('886', '2', '2');
INSERT INTO `system_role_permission` VALUES ('887', '2', '6');
INSERT INTO `system_role_permission` VALUES ('899', '4', '5');
INSERT INTO `system_role_permission` VALUES ('900', '4', '2');
INSERT INTO `system_role_permission` VALUES ('901', '4', '6');
INSERT INTO `system_role_permission` VALUES ('902', '4', '7');
INSERT INTO `system_role_permission` VALUES ('903', '4', '8');
INSERT INTO `system_role_permission` VALUES ('904', '4', '10');
INSERT INTO `system_role_permission` VALUES ('933', '1', '142');
INSERT INTO `system_role_permission` VALUES ('934', '1', '143');
INSERT INTO `system_role_permission` VALUES ('935', '1', '144');
INSERT INTO `system_role_permission` VALUES ('936', '1', '145');
INSERT INTO `system_role_permission` VALUES ('940', '1', '146');
INSERT INTO `system_role_permission` VALUES ('941', '1', '147');
INSERT INTO `system_role_permission` VALUES ('943', '2', '10');
INSERT INTO `system_role_permission` VALUES ('944', '2', '128');
INSERT INTO `system_role_permission` VALUES ('945', '2', '145');
INSERT INTO `system_role_permission` VALUES ('946', '2', '142');
INSERT INTO `system_role_permission` VALUES ('947', '2', '144');
INSERT INTO `system_role_permission` VALUES ('950', '1', '149');
INSERT INTO `system_role_permission` VALUES ('951', '1', '150');
INSERT INTO `system_role_permission` VALUES ('952', '1', '151');
INSERT INTO `system_role_permission` VALUES ('960', '1', '9');
INSERT INTO `system_role_permission` VALUES ('961', '1', '10');
INSERT INTO `system_role_permission` VALUES ('962', '1', '16');
INSERT INTO `system_role_permission` VALUES ('963', '1', '130');
INSERT INTO `system_role_permission` VALUES ('964', '1', '131');
INSERT INTO `system_role_permission` VALUES ('966', '1', '13');
INSERT INTO `system_role_permission` VALUES ('967', '1', '8');
INSERT INTO `system_role_permission` VALUES ('968', '1', '15');
INSERT INTO `system_role_permission` VALUES ('969', '1', '5');
INSERT INTO `system_role_permission` VALUES ('970', '1', '2');
INSERT INTO `system_role_permission` VALUES ('971', '1', '6');
INSERT INTO `system_role_permission` VALUES ('972', '1', '7');
INSERT INTO `system_role_permission` VALUES ('973', '1', '133');
INSERT INTO `system_role_permission` VALUES ('974', '1', '136');
INSERT INTO `system_role_permission` VALUES ('975', '1', '139');
INSERT INTO `system_role_permission` VALUES ('976', '1', '140');
INSERT INTO `system_role_permission` VALUES ('978', '1', '14');
INSERT INTO `system_role_permission` VALUES ('979', '1', '11');
INSERT INTO `system_role_permission` VALUES ('980', '1', '12');
INSERT INTO `system_role_permission` VALUES ('981', '1', '128');
INSERT INTO `system_role_permission` VALUES ('983', '1', '138');
INSERT INTO `system_role_permission` VALUES ('984', '1', '152');
INSERT INTO `system_role_permission` VALUES ('985', '1', '153');
INSERT INTO `system_role_permission` VALUES ('986', '1', '154');
INSERT INTO `system_role_permission` VALUES ('988', '1', '156');
INSERT INTO `system_role_permission` VALUES ('989', '1', '158');
INSERT INTO `system_role_permission` VALUES ('990', '1', '157');

-- ----------------------------
-- Table structure for system_user
-- ----------------------------
DROP TABLE IF EXISTS `system_user`;
CREATE TABLE `system_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `user_name` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(128) NOT NULL COMMENT '加密密码',
  `salt` varchar(32) NOT NULL COMMENT '密码盐，32位随机数，修改密码时重置',
  `real_name` varchar(20) DEFAULT NULL COMMENT '真实姓名',
  `email` varchar(30) DEFAULT NULL COMMENT '邮件',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `status` char(1) NOT NULL COMMENT '0-正常',
  `flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of system_user
-- ----------------------------
INSERT INTO `system_user` VALUES (1, 'admin', 'D05A6A0CB27619DD88083ACA4F9C1C8E', '2CF10FBF4F3E11B6E2F78DE31C845102', '小张', '1600501744@qq.com', '18545552525', '0', 0);
INSERT INTO `system_user` VALUES (14, 'kaiho', 'A320FE5AC16D514A7FE6B55CB08A1FF0', '2D812927405C1536E0085774CCB164D6', '蒋某', '2849256844@qq.com', '15211104364', '0', 0);
INSERT INTO `system_user` VALUES (15, 'karl', '8CA4FF2EFD8C5EFABE0596730FB1F29B', '19FA67149F57447F16C7B36C8F0B1061', '凯', '1611818698@qq.com', '17674701248', '0', 0);

-- ----------------------------
-- Table structure for system_user_role
-- ----------------------------
DROP TABLE IF EXISTS `system_user_role`;
CREATE TABLE `system_user_role` (
  `user_role_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of system_user_role
-- ----------------------------
INSERT INTO `system_user_role` VALUES ('97', '9', '1');
INSERT INTO `system_user_role` VALUES ('142', '1', '1');
INSERT INTO `system_user_role` VALUES ('147', '15', '40');
INSERT INTO `system_user_role` VALUES ('148', '14', '40');


-- ----------------------------
-- Table structure for ec_ip_white_list
-- ----------------------------
DROP TABLE IF EXISTS `ec_ip_white_list`;
CREATE TABLE `ec_ip_white_list`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公司名称',
  `ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `create_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `modify_date_time` datetime(0) NULL DEFAULT NULL COMMENT '上次修改时间',
  `modify_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上次修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
