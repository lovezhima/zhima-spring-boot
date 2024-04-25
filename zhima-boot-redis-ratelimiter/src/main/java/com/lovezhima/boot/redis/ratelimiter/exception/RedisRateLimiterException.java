package com.lovezhima.boot.redis.ratelimiter.exception;

import com.lovezhima.boot.core.constant.enums.CommonErrorCodeEnum;
import com.lovezhima.boot.core.exception.BusinessRuntimeException;

import java.io.Serial;

/**
 * @author king on 2024/4/22
 */
public class RedisRateLimiterException extends BusinessRuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public RedisRateLimiterException() {
        super(CommonErrorCodeEnum.RATE_LIMIT);
    }
}
