package com.wenba.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Slf4j
public class ExampleDubboWebApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ExampleDubboWebApplication.class, args);
        log.info("Example api start successful.");
    }

}

