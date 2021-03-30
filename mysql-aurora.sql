/*
 Navicat Premium Data Transfer

 Source Server         : 本地Mysql
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : localhost:3306
 Source Schema         : aurora

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 30/03/2021 14:36:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `log_id` int(22) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `log_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录用户',
  `log_role` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录用户的角色',
  `log_module` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '访问模块名',
  `log_method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '访问的方法',
  `log_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被请求后台的URL',
  `log_ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求的ip地址',
  `log_desc` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志描述',
  `log_create_time` datetime(0) NULL DEFAULT NULL COMMENT '记录时间',
  `log_type` int(11) NULL DEFAULT NULL COMMENT '1 表示普通日志 2表示异常日志',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 652 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource`  (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '资源主键',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '资源名称',
  `module` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '模块key 资源唯一标识',
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块url',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `icon` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `pid` bigint(19) NULL DEFAULT NULL COMMENT '父级ID',
  `seq` tinyint(2) NOT NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint(2) NOT NULL DEFAULT 0 COMMENT '状态 0可用 1不可用',
  `type` tinyint(2) NOT NULL DEFAULT 0 COMMENT '资源类型 0表示菜单 1表示按钮',
  `createdate` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 302 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资源' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_resource
-- ----------------------------
INSERT INTO `sys_resource` VALUES (1, '系统管理', 'system', '', '系统管理', 'icon-company', 0, 0, 0, 0, '2014-02-19 01:00:00');
INSERT INTO `sys_resource` VALUES (2, '首页', 'home', '/home/HomeAll', '首页', 'home', 0, 0, 0, 0, '0000-00-00 00:00:00');
INSERT INTO `sys_resource` VALUES (11, '资源管理', 'resourceManage', '/system/resourceManage', '资源管理', 'icon-folder', 1, 1, 0, 0, '2014-02-19 01:00:00');
INSERT INTO `sys_resource` VALUES (12, '角色管理', 'roleManage', '/system/roleManage', '角色管理', 'icon-folder', 1, 2, 0, 0, '2014-02-19 01:00:00');
INSERT INTO `sys_resource` VALUES (13, '用户管理', 'userManage', '/system/userManage', '用户管理', 'icon-folder', 1, 3, 0, 0, '2014-02-19 01:00:00');
INSERT INTO `sys_resource` VALUES (14, '新增功能', 'user_add', NULL, '新增功能', 'icon-folder', 13, 1, 0, 1, '0000-00-00 00:00:00');
INSERT INTO `sys_resource` VALUES (15, '编辑功能', 'user_update', NULL, '编辑功能', 'icon-folder', 13, 3, 0, 1, '0000-00-00 00:00:00');
INSERT INTO `sys_resource` VALUES (16, '删除功能', 'user_delete', NULL, '删除功能', 'icon-folder', 13, 2, 0, 1, '0000-00-00 00:00:00');
INSERT INTO `sys_resource` VALUES (17, '新增功能', 'role_add', NULL, '新增功能', 'icon-folder', 12, 1, 0, 1, '0000-00-00 00:00:00');
INSERT INTO `sys_resource` VALUES (18, '编辑功能', 'role_update', NULL, '编辑功能', 'icon-folder', 12, 2, 0, 1, '0000-00-00 00:00:00');
INSERT INTO `sys_resource` VALUES (19, '删除功能', 'role_delete', NULL, '删除功能', 'icon-folder', 12, 3, 0, 1, '0000-00-00 00:00:00');
INSERT INTO `sys_resource` VALUES (221, '日志管理', 'logManage', '/system/logManage', '日志管理', 'icon-company', 1, 4, 0, 0, '2015-12-01 11:44:20');
INSERT INTO `sys_resource` VALUES (300, '基础数据', 'dataBase', '', '基础数据', 'icon-company', 1, 0, 0, 0, '0000-00-00 00:00:00');
INSERT INTO `sys_resource` VALUES (301, '文件库管理', 'FileLibList', '/modules/filelib/FileLibList', '文件库管理', 'icon-folder', 300, 0, 0, 0, '0000-00-00 00:00:00');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '角色名称',
  `seq` tinyint(2) NOT NULL DEFAULT 0 COMMENT '排序',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `status` tinyint(2) NOT NULL DEFAULT 0 COMMENT '状态 0可用 1不可用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '系統管理员', 0, '系統管理员', 0);
INSERT INTO `sys_role` VALUES (16, '普通角色', 0, '普通角色', 0);

-- ----------------------------
-- Table structure for sys_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_resource`;
CREATE TABLE `sys_role_resource`  (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '角色资源表主键',
  `role_id` bigint(19) NOT NULL DEFAULT 0 COMMENT '角色主键',
  `resource_id` bigint(19) NULL DEFAULT 0 COMMENT '资源主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1932 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_resource
-- ----------------------------
INSERT INTO `sys_role_resource` VALUES (1902, 16, 221);
INSERT INTO `sys_role_resource` VALUES (1903, 16, 14);
INSERT INTO `sys_role_resource` VALUES (1904, 16, 2);
INSERT INTO `sys_role_resource` VALUES (1905, 16, 18);
INSERT INTO `sys_role_resource` VALUES (1906, 16, 12);
INSERT INTO `sys_role_resource` VALUES (1907, 16, 19);
INSERT INTO `sys_role_resource` VALUES (1908, 16, 17);
INSERT INTO `sys_role_resource` VALUES (1909, 16, 16);
INSERT INTO `sys_role_resource` VALUES (1918, 1, 12);
INSERT INTO `sys_role_resource` VALUES (1919, 1, 19);
INSERT INTO `sys_role_resource` VALUES (1920, 1, 18);
INSERT INTO `sys_role_resource` VALUES (1921, 1, 17);
INSERT INTO `sys_role_resource` VALUES (1922, 1, 221);
INSERT INTO `sys_role_resource` VALUES (1923, 1, 13);
INSERT INTO `sys_role_resource` VALUES (1924, 1, 15);
INSERT INTO `sys_role_resource` VALUES (1925, 1, 16);
INSERT INTO `sys_role_resource` VALUES (1926, 1, 14);
INSERT INTO `sys_role_resource` VALUES (1927, 1, 11);
INSERT INTO `sys_role_resource` VALUES (1928, 1, 2);
INSERT INTO `sys_role_resource` VALUES (1929, 1, 300);
INSERT INTO `sys_role_resource` VALUES (1930, 1, 301);
INSERT INTO `sys_role_resource` VALUES (1931, 1, 1);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '用户主键',
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '密码',
  `sex` tinyint(2) NULL DEFAULT 0 COMMENT '性别',
  `age` tinyint(3) NULL DEFAULT 0 COMMENT '年龄',
  `status` tinyint(2) NULL DEFAULT 0 COMMENT '状态 0 可用 1不可用',
  `createdate` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `real_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '真实姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (39, 'panhuaqing', '$2a$10$z8o93sghH57x3Jiyta4YZOi3ozpGBreF78UmdulNxILCBaMfJF4M.', 0, 111, 0, '2020-05-11 13:53:21', '小盘盘');
INSERT INTO `sys_user` VALUES (40, 'admin', '$2a$10$z8o93sghH57x3Jiyta4YZOi3ozpGBreF78UmdulNxILCBaMfJF4M.', 0, 0, 0, '2020-05-11 13:53:21', '系统管理员');
INSERT INTO `sys_user` VALUES (42, 'test', '$2a$10$z8o93sghH57x3Jiyta4YZOi3ozpGBreF78UmdulNxILCBaMfJF4M.', 1, 23, 0, '2020-05-20 10:31:48', '测试用户');
INSERT INTO `sys_user` VALUES (43, '11', '$2a$10$4SFl7VXz796I8syoxFOGvONgn6z2zXGiP6EuEtv5o4gHMbTDUAoNW', 0, 0, 0, '2021-01-21 15:57:30', '测试');
INSERT INTO `sys_user` VALUES (44, '11222', '$2a$10$UBSz.bnaPTub4uMP3c354OHt3i1ydvPf.iMBCbk6t8umjZYCA.yLm', 0, 22, 0, '2021-01-21 15:57:41', '小盘盘');
INSERT INTO `sys_user` VALUES (45, '11232323', '$2a$10$kgUJrCSSZd8nUooqB2ZYJuy2qbIF9LyrViFBU.YMBhnKXs/5.Nc1q', 0, 11, 0, '2021-01-21 15:57:56', '测试');
INSERT INTO `sys_user` VALUES (46, '23233434', '$2a$10$Y.VJxCRDkoUCe7B1bamvW.s7IouidqYRELizm5Wvi.LCYSgvk6rj6', 0, 0, 0, '2021-01-21 15:58:03', '测试用户');
INSERT INTO `sys_user` VALUES (47, 'admin22222', '$2a$10$hnSbbHqNy90e6Ia2c/NDAO/tonHwQuWuzQRrjnYwL3HRWNNzNsBNC', 0, 22, 0, '2021-01-21 15:58:15', '测试');
INSERT INTO `sys_user` VALUES (48, 'admin123111333', '$2a$10$.Wc0OxocU/aGADcB5QfbB./L17vhxXxjmrrA.uSlpChIxv7XX27Ke', 0, 0, 0, '2021-01-21 15:58:25', '测试');
INSERT INTO `sys_user` VALUES (49, 'test2323', '$2a$10$XHLS0F3Jgtvnnvo9M7p..OtvI8HezgsYPTR7xMcfkGKH3gRwcn3Ku', 0, 0, 0, '2021-01-21 15:58:33', '小盘盘');
INSERT INTO `sys_user` VALUES (50, '2323', '$2a$10$LQj.vjj0OwaMsg/YyEYd/ual3ITNuG/r/cj4xYsOfjuJR5h6ErAbq', 0, 23, 0, '2021-01-21 15:58:41', '2222');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '用户角色主键',
  `user_id` bigint(19) NOT NULL DEFAULT 0 COMMENT '用户主键',
  `role_id` bigint(19) NOT NULL DEFAULT 0 COMMENT '角色主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 79 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (65, 40, 1);
INSERT INTO `sys_user_role` VALUES (69, 39, 16);
INSERT INTO `sys_user_role` VALUES (70, 42, 16);
INSERT INTO `sys_user_role` VALUES (71, 43, 16);
INSERT INTO `sys_user_role` VALUES (72, 44, 1);
INSERT INTO `sys_user_role` VALUES (73, 45, 1);
INSERT INTO `sys_user_role` VALUES (74, 46, 16);
INSERT INTO `sys_user_role` VALUES (75, 47, 16);
INSERT INTO `sys_user_role` VALUES (76, 48, 16);
INSERT INTO `sys_user_role` VALUES (77, 49, 16);
INSERT INTO `sys_user_role` VALUES (78, 50, 16);

-- ----------------------------
-- Table structure for tbl_filelib
-- ----------------------------
DROP TABLE IF EXISTS `tbl_filelib`;
CREATE TABLE `tbl_filelib`  (
  `FILE_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `FILE_NO` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '文件编号',
  `FILE_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '文件名称',
  `FILE_VERSION` int(2) NULL DEFAULT NULL COMMENT '文件名称',
  `FILE_CATEGORY` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '文件类别',
  `FILE_PATH` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '文件存储路径',
  `FILE_TYPE` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '文件类型 pdf doc ',
  `FILE_DESC` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '文件内容描述',
  `STATUS` int(1) NULL DEFAULT 1 COMMENT '文件状态 0 删除 1在用',
  `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `USER_ID` int(11) NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`FILE_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '文件库表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tbl_filelib
-- ----------------------------
INSERT INTO `tbl_filelib` VALUES (1, NULL, '每特教育SpringCloudAlibaba技术文档.docx', NULL, NULL, 'D:\\\\10_downfile\\\\1616121098440每特教育SpringCloudAlibaba技术文档.docx', 'Word', NULL, 1, '2021-03-19 10:31:39', NULL);
INSERT INTO `tbl_filelib` VALUES (2, NULL, '每特教育SpringCloudAlibaba技术文档.docx', NULL, NULL, '20210319/1616125462550.docx', 'Word', NULL, 1, '2021-03-19 11:44:23', NULL);
INSERT INTO `tbl_filelib` VALUES (3, NULL, '每特教育SpringCloudAlibaba技术文档.docx', NULL, NULL, 'D:/10_downfile/uploadFiles/file/20210319/1616125559628.docx', 'Word', NULL, 1, '2021-03-19 11:46:00', NULL);

-- ----------------------------
-- Table structure for tbl_project
-- ----------------------------
DROP TABLE IF EXISTS `tbl_project`;
CREATE TABLE `tbl_project`  (
  `PROJECT_ID` int(11) UNSIGNED NOT NULL COMMENT '项目主键',
  `PROJECT_NO` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '项目编号',
  `PROJECT_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '项目名称',
  `PROJECT_THEME` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '项目主题',
  `PROJECT_DESC` varchar(1000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '项目描述',
  `PROJECT_SPONSOR` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '项目发起者或者发起部门机构',
  `START_TIME` date NULL DEFAULT NULL COMMENT '项目开始时间',
  `END_TIME` date NULL DEFAULT NULL COMMENT '项目结束时间',
  `STATUS` int(1) NULL DEFAULT 0 COMMENT '项目状态 ：0 已创建未开始  1已开始 2结束  3撤销',
  `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `USER_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `FILE_PATH` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '路线图相对路径',
  `IS_MAKER` int(255) NULL DEFAULT 0 COMMENT '是否创建路线图 0 否 1 是',
  PRIMARY KEY (`PROJECT_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '项目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tbl_project
-- ----------------------------
INSERT INTO `tbl_project` VALUES (1, '121212', '222222222', '22222222', '2', '22', '2021-03-16', '2021-03-16', 0, '2021-03-16 16:31:20', '1', '', 0);

SET FOREIGN_KEY_CHECKS = 1;
