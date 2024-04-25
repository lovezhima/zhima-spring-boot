package com.lovezhima.boot.redis.ratelimiter.advice;

import com.lovezhima.boot.core.util.IpAddressUtils;
import com.lovezhima.boot.redis.ratelimiter.RedisRateLimiter;
import com.lovezhima.boot.redis.ratelimiter.exception.RedisRateLimiterException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author king on 2024/3/26
 */
@Slf4j
@Aspect
@Component
public class RedisRateLimiterAspect {

    private final StringRedisTemplate redisTemplate;

    private static final DefaultRedisScript<Long> LIMIT_REDIS_SCRIPT;

    private static final String DEFAULT_PREFIX = "limit";

    static {
        LIMIT_REDIS_SCRIPT = new DefaultRedisScript<>();
        LIMIT_REDIS_SCRIPT.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/lua/limit.lua")));
        LIMIT_REDIS_SCRIPT.setResultType(Long.class);
    }

    @SuppressWarnings("all")
    public RedisRateLimiterAspect(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Pointcut("@annotation(com.lovezhima.boot.redis.ratelimiter.RedisRateLimiter)")
    private void redisRateLimiterPointcut() {
    }

    @Around(value = "redisRateLimiterPointcut()")
    public Object handleRedisRateLimiter(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method targetMethod = methodSignature.getMethod();
        if (targetMethod.isAnnotationPresent(RedisRateLimiter.class)) {
            RedisRateLimiter mata = targetMethod.getAnnotation(RedisRateLimiter.class);
            String key = StringUtils.defaultIfBlank(mata.key(),
                    targetMethod.getDeclaringClass().getName() + "." + targetMethod.getName());
            key = String.format("%s:%s:%s", DEFAULT_PREFIX, key, IpAddressUtils.getRealIp());
            TimeUnit timeUnit = mata.timeUnit();
            if (shouldLimited(key, mata.max(), mata.timeout(), timeUnit)) {
                throw new RedisRateLimiterException();
            }
        }
        return pjp.proceed();
    }


    private boolean shouldLimited(String key, long max, long timeout, TimeUnit timeUnit) {
        long ttl = timeUnit.toMillis(timeout);
        long now = Instant.now().toEpochMilli();
        long expired = now - ttl;
        Long executeTimes = redisTemplate.execute(LIMIT_REDIS_SCRIPT,
                Collections.singletonList(key),
                String.valueOf(now),
                String.valueOf(ttl),
                String.valueOf(expired),
                String.valueOf(max));
        if (executeTimes != null) {
            if (executeTimes == 0) {
                log.error("【{}】在单位时间 {} 毫秒内已达到访问上限，当前接口上限 {}", key, ttl, max);
                return true;
            }
            log.info("【{}】在单位时间 {} 毫秒内访问 {} 次", key, ttl, executeTimes);
        }
        return false;
    }
}
