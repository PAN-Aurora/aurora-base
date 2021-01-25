package com.aurora.base.common.model;

/**
 *Global  全局静态变量
 * @author :PHQ
 * @date：2020/5/7
 **/
public class Global {
        //jwt全局静态变量
        public final  static  String  JWT_HEADER ="Authorization";
        public final  static  String  JWT_SECRET ="auroraWeb";
        public final  static  long    JWT_EXPIRATION =86400;
        public final  static  String  JWT_TOKENHEAD ="Bearer ";

        //默认
        public final  static  long  REDIS_LIMITER_SECONDE = 6000;
        public final  static  int  REDIS_LIMITER_MAXCOUNT = 100; //100次请求
}
