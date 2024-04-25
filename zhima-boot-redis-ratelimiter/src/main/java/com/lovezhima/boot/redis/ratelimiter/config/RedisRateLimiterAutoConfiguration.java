package com.lovezhima.boot.redis.ratelimiter.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author king
 * @date 2023/8/13
 */
@Configuration
@ComponentScan(basePackages = {"com.lovezhima.boot.redis.ratelimiter"})
public class RedisRateLimiterAutoConfiguration {
}
