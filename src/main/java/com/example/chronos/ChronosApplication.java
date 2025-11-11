package com.example.chronos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.chronos")
public class ChronosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChronosApplication.class, args);
    }

}
