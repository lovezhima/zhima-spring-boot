package com.lovezhima.boot.redis.delayqueue.entity;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lovezhima.boot.core.config.UnescapeStringJsonDeserializerConfig;
import lombok.Data;
import lombok.NonNull;

/**
 * @author king
 * @date 2023/8/13
 */
@Data
@NonNull
public final class DelayMessage {

    /**
     * topic
     */
    private String topic;

    /**
     * 唯一键 不能为空
     **/
    private String id;

    /**
     * 当前消息执行时间 = delayTimeMillis + putTime
     */
    private long shouldRunTime;

    /**
     * 当前消息延时时间
     */
    private long delayTimeMillis;

    /**
     * 当前消息放入时间
     */
    private long putTimeMillis;

    /**
     * 重试次数
     */
    private int retryTimes;

    /**
     * 实际消息体(JSON)
     */
    @JsonDeserialize(using = UnescapeStringJsonDeserializerConfig.class)
    @JsonRawValue
    private String message;
}
