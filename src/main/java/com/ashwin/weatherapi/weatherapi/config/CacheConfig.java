package com.ashwin.weatherapi.weatherapi.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheConfig {

    private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);

    @Value("${weather.cache.ttl}")
    private int cacheTtlSeconds;

    @Bean
    public CacheManager cacheManager() {
        logger.info("Creating CacheManager with TTL: {} seconds", cacheTtlSeconds);
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("currentWeather", "forecast", "locationSearch");
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofSeconds(cacheTtlSeconds)));
        return cacheManager;
    }
}
