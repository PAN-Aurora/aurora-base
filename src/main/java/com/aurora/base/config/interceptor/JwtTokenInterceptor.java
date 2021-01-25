package com.aurora.base.config.interceptor;

import com.aurora.base.common.model.Global;
import com.aurora.base.common.model.ResponseModel;
import com.aurora.base.common.model.ResultCode;
import com.aurora.base.common.util.JwtUtil;
import com.aurora.base.common.util.RedisUtil;
import com.aurora.base.config.annotation.PassJwtToken;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 *  token 拦截器
 *    处理token存在和不存在的权限校验
 * @author PHQ
 * @create 2020-05-05 20:40
 **/
public class JwtTokenInterceptor extends HandlerInterceptorAdapter {

    private final static Logger logger = LoggerFactory.getLogger(JwtTokenInterceptor.class);

    @Value("${jwt.header}")
    private String token_header = Global.JWT_HEADER;

    @Value("${jwt.tokenHead}")
    private String jwt_tokenHead = Global.JWT_TOKENHEAD;

    private  JwtUtil jwtUtil = new JwtUtil() ;

    private RedisUtil redisUtil;

    public JwtTokenInterceptor(RedisUtil redisUtil ) {
        this.redisUtil =redisUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //获取注解是否过滤不校5token
        HandlerMethod method = (HandlerMethod) handler;
        PassJwtToken auth = method.getMethod().getAnnotation(PassJwtToken.class);
        //假如存在PassJwtToken注解就不进行token校验
        if (auth !=null && auth.required()) {
             return super.preHandle(request, response, handler);
        }

        if(StringUtils.isNotBlank(this.token_header)){
            //token header
            String auth_token = request.getHeader(this.token_header);
            final String auth_token_start = this.jwt_tokenHead;
            if (StringUtils.isNotBlank(auth_token) && auth_token.startsWith(auth_token_start)) {
                auth_token = auth_token.substring(auth_token_start.length());
            } else {
                // 不按规范,不允许通过验证
                auth_token = null;
            }
            logger.info("token："+auth_token);
            //假如不存在token
            if(StringUtils.isBlank(auth_token)){

                ResponseModel.responseResult(response, ResultCode.NOT_TOKEN_ERROR);
                return false;
            }else{
                 String username = jwtUtil.getUsernameFromToken(auth_token);
                //假如token过期或者找不到
                if(StringUtils.isBlank(username)){
                    ResponseModel.responseResult(response, ResultCode.TOKEN_ERROR);
                    return false;
                }else{
                    //更新redis token时间
                    redisUtil.expire(username,30L, TimeUnit.MINUTES);
                   // jwtUtil.refreshToken(auth_token);6
                }
            }
        }else{
            return false;
        }
        return super.preHandle(request, response, handler);
    }

}
