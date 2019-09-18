# 自动装配 (Auto Configuration)
  Spring Boot auto-configuration attempts to automatically configure your Spring application based on the jar dependecies
that you have added.

## 什么是自动装配
所谓自动装配，就是 classpath 中的 @Configuration 标注的类， Spring Boot 会尝试加载成配置类。

## 如何激活自动装配
将 @SpringBootApplication 或者 @EnableAutoConfiguration 标注在 @Configuration 标注的类上，就可以激活自动装配。
实际上，@SpringBootApplication 标注了 @SpringBootConfiguration、 @EnableAutoConfiguration、 @ComponentScan 3 个注解，
是这 3 个注解的派生注解，因此，真正激活自动装配的应是 @EnableAutoConfiguration。

## 体验自动装配
### 看看 Spring Boot 默认会不会自动装配 @Configuration
我们先创建好 spring boot 的 启动类：
```java
package com.example.springboot.autoconfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutoConfigurationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoConfigurationApplication.class, args);
    }
}
```

然后新建一个配置类 MyAutoConfiguration
```java
package com.example.springboot.autoconfiguration;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyAutoConfiguration {
    
    @Bean
    public ApplicationRunner sayHello() {
        return args -> {
            System.out.println("Hello, Auto Configuration.");
        };
    }
}
```

以上配置类定义了一个 ApplicationRunner 类型的 bean，当 Spring 上下文启动后，会向控制台打印一句话。  
这时，我们执行 main 方法，查看控制台日志输出：
```text
部分内容省略...
2019-09-10 23:16:21.061  INFO 73260 --- [           main] c.e.s.a.AutoConfigurationApplication     : No active profile set, falling back to default profiles: default
2019-09-10 23:16:21.359  INFO 73260 --- [           main] c.e.s.a.AutoConfigurationApplication     : Started AutoConfigurationApplication in 15.504 seconds (JVM running for 21.128)
Hello, Auto Configuration.
```
事实说明，标注了 @SpringBootApplication 的 Spring Boot 启动类会自动装配 @Configuration 配置。

### 尝试将 MyAutoConfiguration 放到 AutoConfigurationApplication 的子包下：com.example.springboot.autoconfiguration.config
以上实验 AutoConfigurationApplication 和 MyAutoConfiguration 2 个类处于同一个包下。这令我猜想，假如它们处于不同包下时，
AutoConfigurationApplication 是否仍会自动装配 MyAutoConfiguration。
于是将 MyAutoConfiguration 放到 com.example.springboot.autoconfiguration.config 包下。
再次启动，观察日志输出：
```text
部分内容省略...
2019-09-10 23:16:21.061  INFO 73260 --- [           main] c.e.s.a.AutoConfigurationApplication     : No active profile set, falling back to default profiles: default
2019-09-10 23:16:21.359  INFO 73260 --- [           main] c.e.s.a.AutoConfigurationApplication     : Started AutoConfigurationApplication in 15.504 seconds (JVM running for 21.128)
Hello, Auto Configuration.
```
日志输出说明 @SpringBootApplication 标注的启动类 会扫描启动类所在的包及其子包中的 @Configuration。

### 尝试将 AutoConfigurationApplication 和 MyAutoConfiguration 放到不同包下
再次调整二者的包位置，将 AutoConfigurationApplication 放到 com.example.springboot.autoconfiguration.app 包下，
将 MyAutoConfiguration 放到 com.example.springboot.autoconfiguration.config 包下。
再次启动，观察日志输出：
```text
部分内容省略...
2019-09-10 23:26:43.069  INFO 73300 --- [           main] c.e.s.a.a.AutoConfigurationApplication   : No active profile set, falling back to default profiles: default
2019-09-10 23:26:43.350  INFO 73300 --- [           main] c.e.s.a.a.AutoConfigurationApplication   : Started AutoConfigurationApplication in 15.498 seconds (JVM running for 20.892)
```
这一次，"Hello, Auto Configuration." 没有出现在日志中了，说明 @SpringBootApplication 标注的启动类默认只扫描启动类所在的包及其子包。

### 配置 META-INF/spring.factories

创建 META-INF/spring.factories 资源文件，并添加以下配置:
```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  com.example.springboot.autoconfiguration.config.MyAutoConfiguration
```

再次启动，观察日志：
```text
部分内容省略...
2019-09-10 23:56:57.335  INFO 73363 --- [           main] c.e.s.a.a.AutoConfigurationApplication   : No active profile set, falling back to default profiles: default
2019-09-10 23:56:57.617  INFO 73363 --- [           main] c.e.s.a.a.AutoConfigurationApplication   : Started AutoConfigurationApplication in 15.494 seconds (JVM running for 20.897)
Hello, Auto Configuration.
```
万众期待的 "Hello, Auto Configuration." 又出现在日志中了。

### 小结
Spring Boot 默认装配 @SpringBootApplication 标注类所在包的子包，其原因其实是因为 @SpringBootApplication 标注了 @ComponentScan，
是 @ComponentScan 的派生注解，value/basePackages 默认值为 {}。只要改变扫描包路径，将所有 @Configuration 类所在包都包含，就会被
自动装配。

以上方式是一种方式，另一种方式，是将配置类配置到一个叫 META-INF/spring.factories 的属性文件中，key 是 
org.springframework.boot.autoconfigure.EnableAutoConfiguration，value 是要被装配的配置类，如这里的
com.example.springboot.autoconfiguration.config.MyAutoConfiguration。
这种方式的好处是，对于第三方 jar 包，其配置类所在包路径肯定和我们的不一样，配置类不在 @ComponentScan 的范围内，除非将扫描范围设置的很大，
但这样肯定会严重减慢应用的启动速度。

其实无论那种方式，都是为了让 Spring Boot 装配 @Configuration 而已。

此外，阅读源码的过程中，还发现了一个注解 @ImportAutoConfiguration，这个注解可以指定要导出哪些 @Configuration 类，算是第 3 种方式。

## 深入 & 原理
