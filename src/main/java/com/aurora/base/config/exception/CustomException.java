package com.aurora.base.config.exception;


import com.aurora.base.common.model.ResultModel;

/**
 * 用户自定义异常处理
 * @author PHQ
 * @create 2020-05-03 13:45
 **/

public class CustomException extends RuntimeException{

    private ResultModel resultModel;

    public ResultModel getResultModel() {
        return resultModel;
    }

    public void setResultModel(ResultModel resultModel) {
        this.resultModel = resultModel;
    }

    public CustomException(ResultModel resultModel) {
        this.resultModel = resultModel;
    }
}