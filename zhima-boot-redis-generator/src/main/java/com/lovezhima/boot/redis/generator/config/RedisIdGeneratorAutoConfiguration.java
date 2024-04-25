package com.lovezhima.boot.redis.generator.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author king on 2024/4/22
 */
@Configuration
@ComponentScan({"com.lovezhima.boot.redis.generator", "com.lovezhima.boot.core"})
public class RedisIdGeneratorAutoConfiguration {
}
