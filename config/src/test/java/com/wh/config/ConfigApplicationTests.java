package com.wh.config;

import com.wh.config.bean.Film;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConfigApplicationTests {

    @Autowired
    Film film;

    @Test
    void contextLoads() {
        System.out.println(film);
    }

}
