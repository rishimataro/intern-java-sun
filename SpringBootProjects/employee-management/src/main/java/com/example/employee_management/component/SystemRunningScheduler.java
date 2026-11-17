package com.example.employee_management.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SystemRunningScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemRunningScheduler.class);

    @Scheduled(fixedRate = 30_000)
    public void logSystemRunning() {
        LOGGER.info("System running");
    }
}
