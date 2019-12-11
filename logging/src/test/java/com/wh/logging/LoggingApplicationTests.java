package com.wh.logging;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class LoggingApplicationTests {

    //记录器
    Logger logger = LoggerFactory.getLogger(LoggingApplicationTests.class);

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
