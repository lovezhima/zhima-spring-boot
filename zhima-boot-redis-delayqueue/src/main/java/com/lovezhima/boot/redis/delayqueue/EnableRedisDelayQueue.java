package com.lovezhima.boot.redis.delayqueue;

import com.lovezhima.boot.redis.delayqueue.config.RedisDelayQueueConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author king
 * @date 2023/8/13
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RedisDelayQueueConfiguration.class)
public @interface EnableRedisDelayQueue {
}
