package com.aurora.base.common.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 公共结果model
 * @create 2020-04-29 23:18
 **/
@Getter
@Setter
public class ResultModel <T>  implements Serializable {

    private  int code;
    private  String message;
    private  T data;

    public ResultModel(int code,String message){
        this.code = code;
        this.message = message;
    }

    public ResultModel(int code,String message,T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 默认失败
     * @return
     */
    public static  ResultModel failure(){
        return new ResultModel(ResultCode.BAD_REQUEST.getCode(),ResultCode.BAD_REQUEST.getMsg()) ;
    }

    /**
     *
     * @param resultCode
     * @return
     */
    public static  ResultModel failure(ResultCode resultCode){
        return new ResultModel(resultCode.getCode(),resultCode.getMsg()) ;
    }

    /**
     * 失败返回 指定 code  自定义异常信息
     * @param resultCode
     * @param message
     * @return
     */
    public static  ResultModel failure(ResultCode resultCode, String message){
        return new ResultModel(resultCode.getCode(),message) ;
    }

    /**
     * 通用简单返回
     * @param resultCode
     * @return
     */
    public static  ResultModel result(ResultCode resultCode){
        return new ResultModel(resultCode.getCode(),resultCode.getMsg()) ;
    }

    /**
     * 成功  默认返回不带参数
     * @return
     */
    public static  ResultModel success(){
        return new ResultModel(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMsg()) ;
    }

    /**
     * 成功  默认返回不带参数
     * @return
     */
    public static  ResultModel success(int code,String msg){
        return new ResultModel(code,msg) ;
    }
    /**
     * 成功  并携带数据（针对单个对象或者集合 不带分页）
     * @param resultCode
     * @param data
     * @return
     */
    public static  ResultModel successData(ResultCode resultCode,Object data){
        return new ResultModel(
                  resultCode.getCode()
                , resultCode.getMsg()
                , data) ;
    }
    /**
     * 成功  针对分页数据
     * @param data
     * @param count
     * @return
     */
    public static  ResultModel successPage(Object data,long count){
        Map<String,Object> map = new  HashMap<String,Object>();
        map.put("count",count);
        map.put("list",data);
        return new ResultModel(ResultCode.SUCCESS.getCode()
                ,ResultCode.SUCCESS.getMsg(),
                map
              ) ;
    }


    @Override
    public String toString() {
        return "{" +
                "\"code\":" + code +
                ", \"message\":\"" + message + '\"' +
                ", \"data\":\"" + data + '\"'+
                '}';
    }
}
