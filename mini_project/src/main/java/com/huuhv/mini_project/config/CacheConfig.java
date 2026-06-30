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

    // 1. Detailed configuration for Caffeine
    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterWrite(1, TimeUnit.MINUTES) // Cache expires after 1 minute
                .recordStats(); // REQUIRED: Enables Actuator to collect cache metrics
    }

    // 2. Inject Caffeine into Spring's CacheManager
    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeine);
        // No need to declare cache region names here; Spring will automatically create them
        // based on the names defined in @Cacheable annotations in Services.
        return cacheManager;
    }
}
