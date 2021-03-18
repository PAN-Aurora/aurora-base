package com.aurora;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 启动类
 * @author :PHQ
 **/
@SpringBootApplication
@ServletComponentScan
@MapperScan("com.aurora.*.mapper")
@EnableSwagger2
public class AuroraBaseApplication {
    private static final Logger logger = LoggerFactory.getLogger(AuroraBaseApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(AuroraBaseApplication.class, args);
        logger.info("#############AuroraBaseApplication 启动完成#############");
    }
}
