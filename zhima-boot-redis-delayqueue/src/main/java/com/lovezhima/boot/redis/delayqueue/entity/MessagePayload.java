package com.lovezhima.boot.redis.delayqueue.entity;

import lombok.*;

import java.util.concurrent.TimeUnit;

/**
 * 消息体
 *
 * @author king
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public final class MessagePayload {

    /**
     * 消息ID
     */
    private String id;

    /**
     * 消息体 不能为空
     */
    private String message;

    /**
     * 延迟时间 不能为空
     */
    private Long delayTime;

    /**
     * 时间单位 不能为空
     */
    private TimeUnit timeUnit;

    public MessagePayload(@NonNull String message, @NonNull Long delayTime, @NonNull TimeUnit timeUnit) {
        this.message = message;
        this.delayTime = delayTime;
        this.timeUnit = timeUnit;
    }

    public MessagePayload(String id, @NonNull String message, @NonNull Long delayTime, @NonNull TimeUnit timeUnit) {
        this.id = id;
        this.message = message;
        this.delayTime = delayTime;
        this.timeUnit = timeUnit;
    }
}

