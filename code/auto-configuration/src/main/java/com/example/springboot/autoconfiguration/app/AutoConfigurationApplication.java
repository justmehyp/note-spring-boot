package com.example.springboot.autoconfiguration.app;

import com.example.springboot.autoconfiguration.config.MyAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.example.springboot.autoconfiguration.config")
//@ImportAutoConfiguration(MyAutoConfiguration.class)
//@SpringBootApplication
public class AutoConfigurationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoConfigurationApplication.class, args);
    }

}
