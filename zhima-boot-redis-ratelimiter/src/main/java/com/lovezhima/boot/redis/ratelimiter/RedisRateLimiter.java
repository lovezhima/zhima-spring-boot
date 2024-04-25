package com.lovezhima.boot.redis.ratelimiter;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author king on 2024/3/26
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisRateLimiter {

    long DEFAULT_REQUEST = 10;

    /**
     * max 最大请求数
     */
    @AliasFor("max") long value() default DEFAULT_REQUEST;

    /**
     * max 最大请求数
     */
    @AliasFor("value") long max() default DEFAULT_REQUEST;

    /**
     * 限流key 默认limit:类路径.方法名:ip
     */
    String key() default "";

    /**
     * 滑动窗口时间，默认1s
     */
    long timeout() default 1;

    /**
     * 滑动窗口时间单位，默认s
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
