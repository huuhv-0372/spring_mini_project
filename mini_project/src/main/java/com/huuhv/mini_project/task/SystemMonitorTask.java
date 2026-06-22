package com.huuhv.mini_project.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SystemMonitorTask {

    // fixedRate = 30000: Runs exactly every 30 seconds (30,000 ms)
    @Scheduled(fixedRate = 30000)
    public void monitorSystem() {
        // Log system information
        log.debug("=== System running: The system is still operating stably. ===");
        // Add system monitoring logic here (e.g., check CPU, memory, disk, etc.)
    }

    @Scheduled(fixedRate = 60000)
    @CacheEvict(value = "employeeCount", allEntries = true) // Clear employeeCount cache
    public void clearEmployeeCountCache() {
        log.debug("=== Clearing employee count cache after 1 minute. ===");
    }
}
