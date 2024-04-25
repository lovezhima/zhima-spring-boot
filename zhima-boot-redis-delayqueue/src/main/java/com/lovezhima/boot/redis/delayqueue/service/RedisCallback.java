package com.lovezhima.boot.redis.delayqueue.service;

import com.lovezhima.boot.redis.delayqueue.entity.DelayMessage;
/**
 * @author king
 * @date 2023/8/13
 */
public interface RedisCallback {

    /**
     * 初始化的时候自动注册
     * 注册Topic
     *
     * @return 消息
     */
    String getTopic();

    /**
     * 延迟消息回调
     *
     * @param message 消息
     */
    void onMessage(DelayMessage message);
}
