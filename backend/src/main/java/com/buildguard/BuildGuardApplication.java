package com.buildguard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BuildGuardApplication {
    public static void main(String[] args) {
        SpringApplication.run(BuildGuardApplication.class, args);
    }
}
