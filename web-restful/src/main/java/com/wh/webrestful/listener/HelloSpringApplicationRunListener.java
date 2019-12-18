package com.wh.webrestful.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class HelloSpringApplicationRunListener implements SpringApplicationRunListener {
    public HelloSpringApplicationRunListener(SpringApplication application, String[] args) {
    }

    public void starting() {
        System.out.println("SpringApplicationRunListener...starting");
    }

    public void environmentPrepared(ConfigurableEnvironment environment) {
        System.out.println("SpringApplicationRunListener...environmentPrepared");
    }

    public void contextPrepared(ConfigurableApplicationContext context) {
        System.out.println("SpringApplicationRunListener...contextPrepared");
    }

    public void contextLoaded(ConfigurableApplicationContext context) {
        System.out.println("SpringApplicationRunListener...contextLoaded");
    }

    public void started(ConfigurableApplicationContext context) {
        System.out.println("SpringApplicationRunListener...started");
    }

    public void running(ConfigurableApplicationContext context) {
        System.out.println("SpringApplicationRunListener...running");
    }

    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        System.out.println("SpringApplicationRunListener...failed");
    }
}
