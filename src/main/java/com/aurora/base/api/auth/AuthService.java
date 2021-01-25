package com.aurora.base.api.auth;


import com.aurora.base.common.model.ResultModel;
import com.aurora.base.model.auth.ResponseUserToken;
import com.aurora.base.model.auth.User;

/**
 * 权限登录接口定义
 * @author PHQ
 * @create 2020-05-03 12:20
 **/
public interface AuthService {

    /**
     * 注册用户
     * @param user
     * @return
     */
    User register(User user);

    /**
     * 登陆
     * @param username
     * @param password
     * @return
     */
    ResultModel login(String username, String password);

    /**
     * 登出
     * @param token
     */
    void logout(String token);

    /**
     * 刷新Token
     * @param oldToken
     * @return
     */
    ResponseUserToken refresh(String oldToken);

    /**
     * 根据Token获取用户信息
     * @param token
     * @return
     */
    User getUserByToken(String token);

    /**
     * 获取用户详情信息
     * @param userLogin
     * @return
     */
    public User getUserInfo(User userLogin);
}
