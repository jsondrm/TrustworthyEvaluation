package com.soft.eva;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.soft.eva.*"})
public class EvaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EvaApplication.class, args);
    }
}
