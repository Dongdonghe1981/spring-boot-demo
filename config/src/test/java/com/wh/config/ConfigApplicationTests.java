package com.wh.config;

import com.wh.config.bean.Film;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConfigApplicationTests {

    @Autowired
    Film film;

    //记录器
    Logger logger = LoggerFactory.getLogger(ConfigApplicationTests.class);

    @Test
    void contextLoads() {

        //日志级别，由低到高，可以调整日志的输出级别
        // trace < debug < info < warn < error
        logger.trace("This is trace log");
        logger.debug("This is debug log");
        //SpringBoot默认的是info级别
        logger.info("This is info log");
        logger.warn("This is warn log");
        logger.error("This is error log");
    }

}
