/*
Navicat MySQL Data Transfer

Source Server         : 本地地址
Source Server Version : 50129
Source Host           : localhost:3306
Source Database       : aurora

Target Server Type    : MYSQL
Target Server Version : 50129
File Encoding         : 65001

Date: 2020-05-30 15:03:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `sys_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `log_id` int(22) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `log_user` varchar(255) DEFAULT NULL COMMENT '登录用户',
  `log_role` varchar(255) DEFAULT NULL COMMENT '登录用户的角色',
  `log_module` varchar(255) DEFAULT NULL COMMENT '访问模块名',
  `log_method` varchar(255) DEFAULT NULL COMMENT '访问的方法',
  `log_url` varchar(255) DEFAULT NULL COMMENT '被请求后台的URL',
  `log_ip` varchar(255) DEFAULT NULL COMMENT '请求的ip地址',
  `log_desc` varchar(1000) DEFAULT NULL COMMENT '日志描述',
  `log_create_time` datetime DEFAULT NULL COMMENT '记录时间',
  `log_type` int(11) DEFAULT NULL COMMENT '1 表示普通日志 2表示异常日志',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=357 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_resource`
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '资源主键',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '资源名称',
  `module` varchar(255) NOT NULL DEFAULT '' COMMENT '模块key 资源唯一标识',
  `url` varchar(100) DEFAULT NULL COMMENT '模块url',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `icon` varchar(32) DEFAULT NULL COMMENT '图标',
  `pid` bigint(19) DEFAULT NULL COMMENT '父级ID',
  `seq` tinyint(2) NOT NULL DEFAULT '0' COMMENT '排序',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态 0可用 1不可用',
  `type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '资源类型 0表示菜单 1表示按钮',
  `createdate` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=222 DEFAULT CHARSET=utf8 COMMENT='资源';

-- ----------------------------
-- Records of sys_resource
-- ----------------------------
INSERT INTO `sys_resource` VALUES ('1', '系统管理', 'system', '', '系统管理', 'icon-company', '0', '0', '0', '0', '2014-02-19 01:00:00');
INSERT INTO `sys_resource` VALUES ('2', '首页', 'home', '/home/HomeAll', '首页', 'home', '0', '0', '0', '0', '0000-00-00 00:00:00');
INSERT INTO `sys_resource` VALUES ('11', '资源管理', 'resourceManage', '/system/resourceManage', '资源管理', 'icon-folder', '1', '1', '0', '0', '2014-02-19 01:00:00');
INSERT INTO `sys_resource` VALUES ('12', '角色管理', 'roleManage', '/system/roleManage', '角色管理', 'icon-folder', '1', '2', '0', '0', '2014-02-19 01:00:00');
INSERT INTO `sys_resource` VALUES ('13', '用户管理', 'userManage', '/system/userManage', '用户管理', 'icon-folder', '1', '3', '0', '0', '2014-02-19 01:00:00');
INSERT INTO `sys_resource` VALUES ('14', '新增功能', 'user_add', null, '新增功能', 'icon-folder', '13', '1', '0', '1', '0000-00-00 00:00:00');
INSERT INTO `sys_resource` VALUES ('15', '编辑功能', 'user_update', null, '编辑功能', 'icon-folder', '13', '3', '0', '1', '0000-00-00 00:00:00');
INSERT INTO `sys_resource` VALUES ('16', '删除功能', 'user_delete', null, '删除功能', 'icon-folder', '13', '2', '0', '1', '0000-00-00 00:00:00');
INSERT INTO `sys_resource` VALUES ('17', '新增功能', 'role_add', null, '新增功能', 'icon-folder', '12', '1', '0', '1', '0000-00-00 00:00:00');
INSERT INTO `sys_resource` VALUES ('18', '编辑功能', 'role_update', null, '编辑功能', 'icon-folder', '12', '2', '0', '1', '0000-00-00 00:00:00');
INSERT INTO `sys_resource` VALUES ('19', '删除功能', 'role_delete', null, '删除功能', 'icon-folder', '12', '3', '0', '1', '0000-00-00 00:00:00');
INSERT INTO `sys_resource` VALUES ('221', '日志管理', 'logManage', '/system/logManage', '日志管理', 'icon-company', '1', '4', '0', '0', '2015-12-01 11:44:20');

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '角色名称',
  `seq` tinyint(2) NOT NULL DEFAULT '0' COMMENT '排序',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态 0可用 1不可用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '系統管理员', '0', '系統管理员', '0');
INSERT INTO `sys_role` VALUES ('16', '普通角色', '0', '普通角色', '0');

-- ----------------------------
-- Table structure for `sys_role_resource`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_resource`;
CREATE TABLE `sys_role_resource` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '角色资源表主键',
  `role_id` bigint(19) NOT NULL DEFAULT '0' COMMENT '角色主键',
  `resource_id` bigint(19) DEFAULT '0' COMMENT '资源主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1918 DEFAULT CHARSET=utf8 COMMENT='角色资源表';

-- ----------------------------
-- Records of sys_role_resource
-- ----------------------------
INSERT INTO `sys_role_resource` VALUES ('1', '1', '12');
INSERT INTO `sys_role_resource` VALUES ('2', '1', '18');
INSERT INTO `sys_role_resource` VALUES ('1902', '16', '221');
INSERT INTO `sys_role_resource` VALUES ('1903', '16', '14');
INSERT INTO `sys_role_resource` VALUES ('1904', '16', '2');
INSERT INTO `sys_role_resource` VALUES ('1905', '16', '18');
INSERT INTO `sys_role_resource` VALUES ('1906', '16', '12');
INSERT INTO `sys_role_resource` VALUES ('1907', '16', '19');
INSERT INTO `sys_role_resource` VALUES ('1908', '16', '17');
INSERT INTO `sys_role_resource` VALUES ('1909', '16', '16');
INSERT INTO `sys_role_resource` VALUES ('1910', '1', '221');
INSERT INTO `sys_role_resource` VALUES ('1911', '1', '13');
INSERT INTO `sys_role_resource` VALUES ('1912', '1', '15');
INSERT INTO `sys_role_resource` VALUES ('1913', '1', '16');
INSERT INTO `sys_role_resource` VALUES ('1914', '1', '14');
INSERT INTO `sys_role_resource` VALUES ('1915', '1', '11');
INSERT INTO `sys_role_resource` VALUES ('1916', '1', '2');
INSERT INTO `sys_role_resource` VALUES ('1917', '1', '17');

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '用户主键',
  `username` varchar(64) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(64) NOT NULL DEFAULT '' COMMENT '密码',
  `sex` tinyint(2) DEFAULT '0' COMMENT '性别',
  `age` tinyint(3) DEFAULT '0' COMMENT '年龄',
  `status` tinyint(2) DEFAULT '0' COMMENT '状态 0 可用 1不可用',
  `createdate` datetime DEFAULT NULL COMMENT '创建时间',
  `real_name` varchar(255) NOT NULL DEFAULT '' COMMENT '真实姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COMMENT='用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('39', 'panhuaqing', '$2a$10$zmPB0ZMbKjCwK.geZPTr5OXnRsGIZzx6.hDTbpubeBMxmQYdXcnWG', '0', '111', '0', '2020-05-11 13:53:21', '小盘盘');
INSERT INTO `sys_user` VALUES ('40', 'admin', '$2a$10$RkQ4tEN9SRyJJ.yVPSF.4uUYvDxy47gPeQBkjYZQTaPEjvTLozc0.', '0', '0', '0', '2020-05-11 13:53:21', '系统管理员');
INSERT INTO `sys_user` VALUES ('42', 'test', '$2a$10$BlHWCWkgRFjPHroxujEJ2eCvwf8zeA.1py9joGDQk7L0dk06V0Uc2', '1', '23', '0', '2020-05-20 10:31:48', '测试用户');

-- ----------------------------
-- Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '用户角色主键',
  `user_id` bigint(19) NOT NULL DEFAULT '0' COMMENT '用户主键',
  `role_id` bigint(19) NOT NULL DEFAULT '0' COMMENT '角色主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8 COMMENT='用户角色';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('65', '40', '1');
INSERT INTO `sys_user_role` VALUES ('69', '39', '16');
INSERT INTO `sys_user_role` VALUES ('70', '42', '16');
