package com.luismateoh.gymcrm;

import com.luismateoh.gymcrm.ui.MainConsoleUI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
public class GymCrmApplication {

    public static void main(String[] args) {

        log.info("Starting Gym CRM Application");
        log.info("Spring version: {}", org.springframework.core.SpringVersion.getVersion());
        ApplicationContext context = new AnnotationConfigApplicationContext(GymCrmApplication.class);

        log.info("Application context loaded");
        MainConsoleUI mainConsoleUI = context.getBean(MainConsoleUI.class);
        mainConsoleUI.start();
    }
}
