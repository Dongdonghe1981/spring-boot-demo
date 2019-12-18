package com.wh.webrestful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= DataSourceAutoConfiguration.class)
public class WebRestfulApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebRestfulApplication.class, args);
    }

}
