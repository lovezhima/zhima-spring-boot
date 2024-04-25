package com.lovezhima.boot.redis.delayqueue.config;

import com.lovezhima.boot.redis.delayqueue.core.RedisDelayQueueContainer;
import com.lovezhima.boot.redis.delayqueue.service.RedisCallback;
import com.lovezhima.boot.redis.delayqueue.service.RedisOperationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * @author king
 * @date 2023/8/13
 */
@Configuration
@ComponentScan(basePackages = {"com.lovezhima.boot.redis.delayqueue"})
public class RedisDelayQueueConfiguration {

    @Bean
    public RedisDelayQueueContainer redisDelayQueueContainer(List<RedisCallback> redisCallbacks,
                                                             RedisOperationService redisOperationService,
                                                             RedisDelayQueueConfig redisDelayQueueConfig) {
        RedisDelayQueueContainer container = new RedisDelayQueueContainer();
        container.setRedisOperationService(redisOperationService);
        container.setRedisDelayQueueConfig(redisDelayQueueConfig);
        redisCallbacks.forEach(container::registerCallback);
        return container;
    }

    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate redisTemplate() {
        return new StringRedisTemplate();
    }
}
