package com.aurora.base.config.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurora.base.api.auth.AuthService;
import com.aurora.base.api.system.SysLogService;
import com.aurora.base.common.util.IpUtil;
import com.aurora.base.common.util.JwtUtil;
import com.aurora.base.common.util.RedisUtil;
import com.aurora.base.common.util.StringUtil;
import com.aurora.base.config.annotation.PassJwtToken;
import com.aurora.base.config.annotation.SystemLog;
import com.aurora.base.model.auth.User;
import com.aurora.base.model.system.SysLog;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.sql.Timestamp;

/**
 * 日志AOP记录
 * @author PHQ
 * @create 2020-05-01 22:54
 **/
@Slf4j
@Configuration
@Aspect
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private AuthService authService;

    @Value("${jwt.header}")
    private String token_header;

    @Value("${jwt.tokenHead}")
    private String jwt_tokenHead;

    private  JwtUtil jwtUtil = new  JwtUtil();

    private String auth_token = "";

    @Autowired
    private RedisUtil redisUtil;

    //定义切点 针对类上注解含有SystemLog
    @Pointcut(value = "@annotation(com.aurora.base.config.annotation.SystemLog)")
    public void logPointCut(){}

    /**
     * 后置通知
     * @param joinPoint
     * @param ret
     */
    @AfterReturning(returning = "ret",pointcut = "logPointCut()")
    public void saveSysLog(JoinPoint joinPoint, Object ret){
        logger.info("日志记录开始.........");
        logger.info("redisUtil:"+redisUtil.get("admin"));
        this.saveSystemLog(joinPoint,"",false);
    }

    /**
     * 异常通知
     * @param joinPoint
     * @param ex
     */
    @AfterThrowing(pointcut = "logPointCut()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint,Exception ex) {
        if (ex instanceof Exception) {
            logger.error("异常信息："+ex);
            String logDesc =ex.getMessage().substring(0,ex.getMessage().length()>900?900:ex.getMessage().length());
            this.saveSystemLog(joinPoint,logDesc,true);
        }
    }

    /**
     * 保存行为日志
     * @param joinPoint
     * @param logDesc
     * @param isError
     */
    private void  saveSystemLog(JoinPoint joinPoint,String logDesc,boolean isError){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLog log = new  SysLog();
        SystemLog systemLog  =  method.getAnnotation(SystemLog.class);
        if (systemLog != null) {
            HttpServletRequest  request =   this.getRequest();
            logger.info("日志记录操作方法:"+systemLog.methods());
            //登录相关接口不携带token
            PassJwtToken passJwtToken  =  method.getAnnotation(PassJwtToken.class);
            if(passJwtToken != null) {
                //获取切点参数
                Object[] args = joinPoint.getArgs();
                if(args.length>0){
                    User userLogin = JSONObject.parseObject(JSON.toJSONString(args[0]), User.class);
                    //假如存在登录用户
                    if (StringUtil.isNotBlank(userLogin.getUsername())) {
                        User user = authService.getUserInfo(userLogin);
                        log.setLogUser(userLogin.getUsername());
                        if(user != null && user.getRole()!=null){
                            log.setLogRole(user.getRole().getName());
                        }else{
                            log.setLogRole("普通角色");
                        }

                    }
                }else{
                    log.setLogUser("test");
                    log.setLogRole("普通角色");
                }

            }else{
                auth_token  = request.getHeader(this.token_header);

                if (StringUtil.isNotBlank(auth_token) && auth_token.startsWith(jwt_tokenHead)) {
                    auth_token = auth_token.substring(jwt_tokenHead.length());
                } else {
                    // 不按规范,不允许通过验证
                    auth_token = null;
                }
                Claims claims = jwtUtil.getClaimsFromToken(auth_token);
                log.setLogUser(claims.get("sub").toString());
                log.setLogRole(claims.get("scope").toString());
          }
            log.setLogIp(IpUtil.getIpAddress(request));
            //log注解数据
            log.setLogModule(systemLog.module());
            log.setLogUrl(systemLog.url());
            if(isError){
                log.setLogDesc(logDesc);
            }else{
                log.setLogDesc(systemLog.desc());
            }
            log.setLogMothod(systemLog.methods());
            log.setLogCreateTime(new Timestamp(System.currentTimeMillis()));

            if(isError){
                log.setLogType(2);
            }else{
                log.setLogType(1);
            }
            sysLogService.saveLog(log);
        }
    }

    /**
     * 获取request对象
     * @return
     */
    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request;
    }

}
