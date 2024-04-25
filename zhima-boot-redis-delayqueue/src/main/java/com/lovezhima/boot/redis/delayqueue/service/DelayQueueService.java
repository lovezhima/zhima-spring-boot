package com.lovezhima.boot.redis.delayqueue.service;

import com.lovezhima.boot.redis.delayqueue.entity.MessagePayload;
import java.util.List;
/**
 * 延迟队列
 *
 * @author king
 * @date 2023/7/29
 */
public interface DelayQueueService {

    boolean addDelayMessage(String topic, MessagePayload messagePayload);

    boolean addDelayMessage(String topic, List<MessagePayload> messagePayloads);

    boolean saveDelayMessage(String topic, MessagePayload messagePayload, boolean compareCoverage);

    boolean saveDelayMessage(String topic, List<MessagePayload> messagePayloads, boolean compareCoverage);
}
