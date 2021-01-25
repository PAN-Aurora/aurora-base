package com.aurora.base.controller.system;

import com.aurora.base.api.system.RoleService;
import com.aurora.base.common.model.ResultCode;
import com.aurora.base.common.model.ResultModel;
import com.aurora.base.config.annotation.SystemLog;
import com.aurora.base.model.system.Role;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 *  角色管理控制类
 * @author PHQ
 * @create 2020-05-14 22:01
 **/
@RestController
@RequestMapping("/api/role")
public class RoleController {
        @Autowired
        private RoleService roleService;

        /**
         * 获取角色列表
         * @param role
         * @return
         */
        @GetMapping(value = "/getRoleList")
        @SystemLog(module="角色管理模块",methods="获取角色列表",url="/api/role/getRoleList", desc="获取角色列表")
        @ApiOperation(value="获取角色列表", notes="通过获取角色列表 ",httpMethod = "GET")
        @ApiImplicitParams({
                @ApiImplicitParam(name = "role", value = "角色详情实体role", required = true, dataType = "Role")
        })
        public ResultModel getRoleList(Role role){
                return roleService.getRoleList(role);
        }



        /**
         * 保存角色
         * @param role
         * @return
         */
        @PostMapping(value = "/saveRole")
        @SystemLog(module="角色管理模块",methods="保存角色",url="/api/role/saveRole", desc="保存角色")
        @ApiOperation(value = "保存角色",notes = "保存角色",httpMethod = "POST")
        @ApiImplicitParams({
                @ApiImplicitParam(name = "role", value = "角色详情实体role，name不能为空", required = true, dataType = "com.aurora.model.auth.Role")
        })
        public ResultModel saveRole(@RequestBody Role role){
                if(role != null && StringUtils.isNotBlank(role.getName())){
                        return roleService.insertRole(role);
                }
                return ResultModel.failure(ResultCode.BAD_PARAMS);
        }

        /**
         * 修改角色
         * @param role
         * @return
         */
        @PostMapping(value = "/updateRole")
        @SystemLog(module="角色管理模块",methods="修改角色",url="/api/role/updateRole", desc="修改角色")
        @ApiOperation(value = "修改角色",notes = "修改角色",httpMethod = "POST")
        @ApiImplicitParams({
                @ApiImplicitParam(name = "role", value = "角色详情实体role，属性id不能为空，name名称不能为空", required = true, dataType = "com.aurora.model.auth.Role")
        })
        public ResultModel updateRole(@RequestBody Role role){
                if(role != null && StringUtils.isNotBlank(role.getName())){
                        return roleService.updateRole(role);
                }
                return ResultModel.failure(ResultCode.BAD_PARAMS);
        }
        /**
         * 刪除角色
         * @param role
         * @return
         */
        @PostMapping(value = "/deleteRole")
        @SystemLog(module="角色管理模块",methods="刪除角色",url="/api/role/deleteRole", desc="刪除角色")
        @ApiOperation(value = "刪除角色",notes = "刪除角色",httpMethod = "POST")
        @ApiImplicitParams({
                @ApiImplicitParam(name = "role", value = "角色详情实体role，并且属性ids不为空", required = true, dataType = "com.aurora.model.auth.Role")
        })
        public ResultModel deleteRole(@RequestBody Role role){
                if(role != null && role.getIds().length>0){
                        return roleService.deleteRole(role);
                }
                return ResultModel.failure(ResultCode.BAD_PARAMS);
        }
}
