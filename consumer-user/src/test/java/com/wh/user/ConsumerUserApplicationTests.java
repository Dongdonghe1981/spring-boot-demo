package com.wh.user;

import com.wh.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
class ConsumerUserApplicationTests {

    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
        userService.hello();
    }

}
