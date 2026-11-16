package com.example.employee_management.config;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public Clock applicationClock() {
        return Clock.systemDefaultZone();
    }
}
