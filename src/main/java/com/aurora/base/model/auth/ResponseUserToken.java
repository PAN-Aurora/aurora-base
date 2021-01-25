package com.aurora.base.model.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户登录返回结果model
 * @author PHQ
 * @create 2020-05-03 12:26
 **/
@Data
@AllArgsConstructor
public class ResponseUserToken {
    private String token;
    private User user;
}
