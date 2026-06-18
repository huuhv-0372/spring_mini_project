package com.huuhv.mini_project.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    // 1. Cấu hình chi tiết cho Caffeine
    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterWrite(1, TimeUnit.MINUTES) // Cache sống đúng 1 phút
                .recordStats(); // BẮT BUỘC CÓ: Để Actuator thu thập được số liệu (Metrics)
    }

    // 2. Tiêm Caffeine vào CacheManager của Spring
    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeine);
        // Không cần khai báo tên vùng cache cứng ở đây nữa, Spring sẽ tự tạo vùng cache
        // dựa theo tên bạn đặt trong nhãn @Cacheable ở Service.
        return cacheManager;
    }
}
