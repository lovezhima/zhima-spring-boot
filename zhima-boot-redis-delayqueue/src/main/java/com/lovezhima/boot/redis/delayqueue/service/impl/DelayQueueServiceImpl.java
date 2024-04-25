package com.lovezhima.boot.redis.delayqueue.service.impl;

import com.lovezhima.boot.core.helper.JsonHelper;
import com.lovezhima.boot.core.util.convert.CollectionConverter;
import com.lovezhima.boot.core.util.generator.impl.UUIDGenerator;
import com.lovezhima.boot.redis.delayqueue.config.RedisDelayQueueConfig;
import com.lovezhima.boot.redis.delayqueue.entity.DelayMessage;
import com.lovezhima.boot.redis.delayqueue.entity.MessagePayload;
import com.lovezhima.boot.redis.delayqueue.service.DelayQueueService;
import com.lovezhima.boot.redis.delayqueue.service.RedisOperationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author king
 * @date 2023/8/13
 */
@Service
@RequiredArgsConstructor
public class DelayQueueServiceImpl implements DelayQueueService {

    private final RedisOperationService redisOperationService;

    private final RedisDelayQueueConfig redisDelayQueueConfig;

    @Override
    public boolean addDelayMessage(String topic, MessagePayload messagePayload) {
        DelayMessage delayMessage = buildDelayMessage(topic, messagePayload);
        return redisOperationService.addJob(delayMessage.getTopic(), delayMessage);
    }

    @Override
    public boolean addDelayMessage(String topic, List<MessagePayload> messagePayloads) {
        List<DelayMessage> delayMessages = CollectionConverter.toArrayList(messagePayloads,
                messagePayload -> buildDelayMessage(topic, messagePayload));
        return redisOperationService.batchAddJob(topic, delayMessages);
    }

    @Override
    public boolean saveDelayMessage(String topic, MessagePayload messagePayload, boolean compareCoverage) {
        DelayMessage delayMessage = buildDelayMessage(topic, messagePayload);
        return redisOperationService.saveJob(topic, delayMessage, compareCoverage);
    }

    @Override
    public boolean saveDelayMessage(String topic, List<MessagePayload> messagePayloads, boolean compareCoverage) {
        List<DelayMessage> delayMessages = CollectionConverter.toArrayList(messagePayloads,
                messagePayload -> buildDelayMessage(topic, messagePayload));
        return redisOperationService.batchSaveJob(topic, delayMessages, compareCoverage);
    }

    protected DelayMessage buildDelayMessage(String topic, MessagePayload messagePayload) {
        long putTimeMillis = System.currentTimeMillis();
        long delayTimeMillis = messagePayload.getTimeUnit().toMillis(messagePayload.getDelayTime());
        DelayMessage delayMessage = new DelayMessage();
        delayMessage.setTopic(topic);
        delayMessage.setId(StringUtils.isBlank(messagePayload.getId()) ?
                UUIDGenerator.getInstance().genId() : messagePayload.getId());
        delayMessage.setMessage(JsonHelper.toJson(messagePayload.getMessage()));
        delayMessage.setPutTimeMillis(putTimeMillis);
        delayMessage.setDelayTimeMillis(delayTimeMillis);
        delayMessage.setShouldRunTime(putTimeMillis + delayTimeMillis);
        delayMessage.setRetryTimes(redisDelayQueueConfig.getMaxRetryTimes());
        return delayMessage;
    }
}
