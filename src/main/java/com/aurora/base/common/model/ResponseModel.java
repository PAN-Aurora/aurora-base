package com.aurora.base.common.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * todo..
 *
 * @author :PHQ
 * @date：2020/5/14
 **/
public class ResponseModel {

    private final static Logger logger = LoggerFactory.getLogger(ResponseModel.class);

    /**
     * 输出到浏览器
     * @param response
     * @param resultCode
     */
    public static void responseResult(HttpServletResponse response,ResultCode resultCode){
        response.resetBuffer();
        response.setStatus(200);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String body = "";
            if(resultCode!= null){
                body = ResultModel.failure(resultCode).toString();
            }else{
                body = ResultModel.failure(ResultCode.SERVER_ERROR).toString();
            }
            writer.println(body);

            response.flushBuffer();
        } catch (IOException e) {
            logger.error(" 请求响应出错 e = {}", e.getMessage(), e);
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }

    /**
     * 往浏览器输出
     * @param response
     * @param code
     * @param msg
     */
    public static void responseResult(HttpServletResponse response,int code,String msg){
        response.resetBuffer();
        response.setStatus(200);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String body = ResultModel.success(code,msg).toString();
            writer.println(body);
            response.flushBuffer();
        } catch (IOException e) {
            logger.error(" 请求响应出错 e = {}", e.getMessage(), e);
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
}
