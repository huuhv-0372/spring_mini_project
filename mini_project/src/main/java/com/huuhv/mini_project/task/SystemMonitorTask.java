package com.huuhv.mini_project.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SystemMonitorTask {

    // fixedRate = 30000: Chạy lặp lại chính xác mỗi 30 giây (30.000 ms)
    @Scheduled(fixedRate = 30000)
    public void monitorSystem() {
        // Ghi log thông tin hệ thống
        log.info("=== System running: The system is still operating stably. ===");
        // Thêm logic giám sát hệ thống ở đây (ví dụ: kiểm tra CPU, bộ nhớ, ổ đĩa, v.v.)
    }

    @Scheduled(fixedRate = 60000)
    @CacheEvict(value = "employeeCount", allEntries = true) // Clear memory of employeeCount
    public void clearEmployeeCountCache() {
        log.info("=== Clearing employee count cache after 1 minute. ===");
    }
}
