package com.lovezhima.boot.redis.delayqueue.config;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 延时队列参数
 *
 * @author king
 */
@Data
@Component
public class RedisDelayQueueConfig {

    public static final String PREFIX = "zhima.delayQueue";
    private static final String DEFAULT_REDIS_INSTANCE = "delay";
    private static final String DEFAULT_DELAY_HASH_TAG = "delay_tag";
    private static final Integer DEFAULT_MAX_RETRY_TIMES = 10;

    /**
     * 是否启用
     */
    private boolean enabled = true;

    /**
     * redis实例名称
     */
    private String redisInstance = DEFAULT_REDIS_INSTANCE;

    /**
     * 延时队列hash tag,用于做Redis HashTag,默认值delay_name
     */
    private String delayHashTag = DEFAULT_DELAY_HASH_TAG;

    /**
     * 最大重试次数, 默认10次, 如果消费 (1 + maxRetryTimes)次, 则进入死信队列
     */
    private Integer maxRetryTimes = DEFAULT_MAX_RETRY_TIMES;

    /**
     * 消费者配置
     */
    private Consumer consumer = new Consumer();

    @Data
    public static class Consumer {
        /**
         * 是否启用,默认启用
         */
        private boolean enabled = true;
        /**
         * 每次POP数量,默认100个
         */
        private int popCount = 100;
        /**
         * 如果无数据, 等待时间, 默认10s
         */
        private long stopMills = TimeUnit.SECONDS.toMillis(10);
    }

}
