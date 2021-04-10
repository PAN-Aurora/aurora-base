package com.aurora.base.controller.system;

import com.aurora.base.api.system.UserService;
import com.aurora.base.common.model.ResultCode;
import com.aurora.base.common.model.ResultModel;
import com.aurora.base.common.util.StringUtil;
import com.aurora.base.config.annotation.SystemLog;
import com.aurora.base.model.auth.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 用户管理
 * @author :PHQ
 * @date：2020/5/7
 **/
@Api(tags = "用户管理",description="用户管理")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户列表
     * @param user
     * @return
     */
    @GetMapping(value = "/getUserList")
    @SystemLog(module="用户管理模块",methods="获取用户列表",url="/api/user/getUserList", desc="获取用户分页列表数据")
    @ApiOperation(value = "获取用户列表",notes = "获取用户分页列表数据",httpMethod = "GET")
    public ResultModel getUserList(@ApiIgnore User user){
        return userService.getUserList(user);
    }


    /**
     * 保存用户
     * @param user
     * @return
     */
    @PostMapping(value = "/saveUser")
    @SystemLog(module="用户管理模块",methods="保存用户",url="/api/user/saveUser", desc="保存用户")
    @ApiOperation(value = "保存用户",notes = "保存用户",httpMethod = "POST")
    public ResultModel saveUser(@RequestBody User user){
        if(user != null && StringUtil.isNotBlank(user.getUsername())){
            return userService.insertUser(user);
        }
        return ResultModel.failure(ResultCode.BAD_PARAMS);
    }

    /**
     * 修改用户
     * @param user
     * @return
     */
    @PostMapping(value = "/updateUser")
    @SystemLog(module="用户管理模块",methods="修改用户",url="/api/user/updateUser", desc="修改用户")
    @ApiOperation(value = "修改用户",notes = "修改用户",httpMethod = "POST")
    public ResultModel updateUser(@RequestBody User user){
        if(user != null && StringUtil.isNotBlank(user.getUsername())){
            return userService.updateUser(user);
        }
        return ResultModel.failure(ResultCode.BAD_PARAMS);
    }

    /**
     * 删除用户
     * @param user
     * @return
     */
    @PostMapping(value = "/deleteUser")
    @SystemLog(module="用户管理模块",methods="删除用户",url="/api/user/deleteUser", desc="删除用户")
    @ApiOperation(value = "删除用户",notes = "删除用户",httpMethod = "POST")
    public ResultModel deleteUser(@RequestBody User user){
        if(user != null && user.getIds().length>0){
            return userService.deletetUser(user);
        }
        return ResultModel.failure(ResultCode.BAD_PARAMS);
    }
}
