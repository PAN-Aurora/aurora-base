package com.aurora.base.service.system;

import com.alibaba.fastjson.JSON;
import com.aurora.base.api.system.UserService;
import com.aurora.base.common.model.ResultCode;
import com.aurora.base.common.model.ResultModel;
import com.aurora.base.common.util.StringUtil;
import com.aurora.base.mapper.auth.AuthMapper;
import com.aurora.base.model.auth.User;
import com.aurora.base.model.system.Role;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

/**
 * 用户业务实现类
 * @author :PHQ
 * @date：2020/5/7
 **/
@Service
public class UserServiceImpl implements UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    private AuthMapper authMapper;

    public ResultModel getUserList(User user){
        //分页参数
        Page<User> page = new Page<>(user.getCurrent(), user.getLimit());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //查询参数
        if(StringUtil.isNotBlank(user.getUsername())){
            queryWrapper.eq("username",user.getUsername());
        }
        if(StringUtil.isNotBlank(user.getRealName())){
            queryWrapper.eq("real_name",user.getRealName());
        }
        IPage<User> userIPage =  authMapper.selectPage(page,queryWrapper);
        List<User> userList = userIPage.getRecords();
        userList.forEach(user1 -> {
            Role role =  authMapper.findRoleByUserId(user1);
            user1.setRole(role);
        });

        logger.info(userIPage.getTotal()+"");
        logger.info(JSON.toJSONString(userIPage.getRecords()));
        return ResultModel.successPage(userIPage.getRecords(),userIPage.getTotal());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultModel insertUser(User user) {
        //先查询是否有相同用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();

        if(StringUtil.isNotBlank(user.getUsername())){
            queryWrapper.eq("username",user.getUsername());
        }
        if(authMapper.selectCount(queryWrapper)>0){
            return ResultModel.success(ResultCode.BAD_PARAMS.getCode(),"用户名重复！");
        }
        if(StringUtil.isBlank(user.getPassword())){
            user.setPassword("123456");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = user.getPassword();

        user.setPassword(encoder.encode(rawPassword));
        user.setLastPasswordResetDate(new Date(System.currentTimeMillis()));
        //先新增用户
        authMapper.insertUser(user);
        //然后判断是否授权角色
        if(user.getRole()!= null ){
             authMapper.insertRole(user.getId(),user.getRole().getId());
        }
        return ResultModel.success(ResultCode.SUCCESS.getCode(),"保存用户成功！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultModel updateUser(User user) {

        if(StringUtil.isNotBlank(user.getPassword())){
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            final String rawPassword = user.getPassword();
            user.setPassword(encoder.encode(rawPassword));
            user.setLastPasswordResetDate(new Date(System.currentTimeMillis()));
        }
        authMapper.updateById(user);
        //然后判断是否授权角色
        if(user.getRole()!= null ){
            //先刪除用户对应角色
            authMapper.deleteUserRoleById(user.getId());
            //再增加用户角色
            authMapper.insertRole(user.getId(),user.getRole().getId());
        }
        return ResultModel.success(ResultCode.SUCCESS.getCode(),"更新用户成功！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultModel deletetUser(User user) {


        int[] ids =  user.getIds();
        for(int i=0;i<ids.length;i++){
            //先刪除用户对应角色
            authMapper.deleteUserRoleById(ids[i]);
            //然后删除用户
            authMapper.deleteById(ids[i]);
        }

        return ResultModel.success(ResultCode.SUCCESS.getCode(),"刪除用户成功！");
    }
}
