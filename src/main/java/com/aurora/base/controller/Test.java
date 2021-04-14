package com.aurora.base.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author :PHQ
 * @date：2021/1/25
 **/
public class Test {
    private static final Logger logger = LoggerFactory.getLogger(Test.class);
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        logger.info(encoder.encode("123456"));
    }
}
