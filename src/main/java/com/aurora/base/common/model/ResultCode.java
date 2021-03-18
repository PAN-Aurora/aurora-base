package com.aurora.base.common.model;

/**
 *请求返回状态码和说明信息
 * @author PHQ
 * @create 2020-05-02 20:58
 **/
public enum ResultCode {

    SUCCESS(200, "成功"),
    BAD_REQUEST(400, "请求失败"),
    UNAUTHORIZED(401, "认证失败"),
    BAD_PARAMS(402, "参数不合法"),
    LOGIN_ERROR(401, "登陆失败，用户名或密码无效"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "请求的资源不存在"),
    OPERATE_ERROR(405, "操作失败，请求操作的资源不存在"),
    TIME_OUT(408, "请求超时"),
    TOKEN_ERROR(410,"token失效"),
    NOT_TOKEN_ERROR(411,"token不正确"),
    SERVER_ERROR(500, "服务器内部错误"),
    LIMITER_SERVER_ERROR(500, "服务器内部繁忙"),
    LIMITER_REDIS_ERROR(412, "请求频繁，请稍后请求"),
    REDIS_ERROR(414, "Redis服务异常"),
    ;
    private int code;
    private String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
