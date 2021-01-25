package com.aurora.base.config.exception;

import com.aurora.base.common.model.ResultCode;
import com.aurora.base.common.model.ResultModel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ${DESCRIPTION}
 *
 * @author PHQ
 * @create 2020-05-03 13:46
 **/
@RestControllerAdvice
@Slf4j
public class DefaultExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    /**
     * 处理所有自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public ResultModel handleCustomException(CustomException e){
        logger.error(e.getResultModel().getMessage());
        return e.getResultModel();
    }

    /**
     * 处理参数校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResultModel handleMethodArgumentNotValidException(Exception e){
        log.error(e.getLocalizedMessage() );
        return ResultModel.failure(ResultCode.BAD_REQUEST, e.getMessage());
    }
}
