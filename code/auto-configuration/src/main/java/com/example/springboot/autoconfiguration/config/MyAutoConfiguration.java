package com.example.springboot.autoconfiguration.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Configuration
public class MyAutoConfiguration {

    @Bean
    @ConditionalOnClass(ApplicationRunner.class)
    public ApplicationRunner sayHello(ApplicationContext ac) {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                System.out.println("Hello, Auto Configuration.");
                System.out.println(ac.getBean("sayHello"));
                System.out.println(this);
                System.out.println(MyAutoConfiguration.this.sayHello(ac));
                System.out.println(MyAutoConfiguration.this.sayHello(ac));
                System.out.println("=========");
                System.out.println(ac.getBean(MyAutoConfiguration.class));
                System.out.println(MyAutoConfiguration.this);
            }
        };
    }
}
