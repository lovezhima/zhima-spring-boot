package com.lovezhima.boot.redis.delayqueue.service.impl;

import com.lovezhima.boot.core.helper.JsonHelper;
import com.lovezhima.boot.redis.delayqueue.config.RedisDelayQueueConfig;
import com.lovezhima.boot.redis.delayqueue.entity.DelayMessage;
import com.lovezhima.boot.redis.delayqueue.service.RedisOperationService;
import com.lovezhima.boot.redis.delayqueue.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author king
 * @date 2023/8/13
 */
@Service
@RequiredArgsConstructor
public class RedisOperationServiceImpl implements RedisOperationService {
    // Redis Lua Script Path;
    private static final DefaultRedisScript<String> BATCH_ADD_JOB_REDIS_SCRIPT;
    private static final DefaultRedisScript<String> BATCH_SAVE_JOB_REDIS_SCRIPT;
    private static final DefaultRedisScript<String> FORCE_COMMIT_JOB_REDIS_SCRIPT;
    private static final DefaultRedisScript<String> BATCH_COMMIT_JOB_REDIS_SCRIPT;
    private static final DefaultRedisScript<String> BATCH_ROLLBACK_JOB_REDIS_SCRIPT;
    private static final DefaultRedisScript<String> POP_JOB_REDIS_SCRIPT;

    private final StringRedisTemplate redisTemplate;

    private final RedisDelayQueueConfig redisDelayQueueConfig;

    @Override
    public boolean addJob(String topic, DelayMessage delayMessage) {
        return batchAddJob(topic, Collections.singletonList(delayMessage));
    }

    @Override
    public boolean batchAddJob(String topic, List<DelayMessage> delayMessages) {
        List<String> keys = Arrays.asList(
                RedisDelayQueueConstant.DelayQueue.getDqJobDetailKey(topic),
                RedisDelayQueueConstant.DelayQueue.getDqJobIndexKey(topic));
        String boolResult = redisTemplate.execute(
                BATCH_ADD_JOB_REDIS_SCRIPT,
                keys,
                JsonHelper.toJson(delayMessages));
        return Boolean.parseBoolean(boolResult);
    }

    @Override
    public boolean saveJob(String topic, DelayMessage delayMessage, boolean compareCoverage) {
        return batchSaveJob(topic, Collections.singletonList(delayMessage), compareCoverage);
    }

    @Override
    public boolean batchSaveJob(String topic, List<DelayMessage> delayMessages, boolean compareCoverage) {
        List<String> keys = Arrays.asList(
                RedisDelayQueueConstant.DelayQueue.getDqJobDetailKey(topic),
                RedisDelayQueueConstant.DelayQueue.getDqJobIndexKey(topic)
        );
        String result = redisTemplate.execute(BATCH_SAVE_JOB_REDIS_SCRIPT, keys,
                JsonHelper.toJson(delayMessages), compareCoverage);
        return Boolean.parseBoolean(result);
    }

    @Override
    public void forceCommit(String topic, String id) {
        List<String> keys = Arrays.asList(
                RedisDelayQueueConstant.DelayQueue.getDqJobDetailKey(topic),
                RedisDelayQueueConstant.DelayQueue.getDqJobIndexKey(topic),
                RedisDelayQueueConstant.DelayQueue.getDqJobBackupKey(topic)
        );
        redisTemplate.execute(FORCE_COMMIT_JOB_REDIS_SCRIPT, keys, id);
    }

    @Override
    public void commit(String topic, DelayMessage delayMessage) {
        commit(topic, Collections.singletonList(delayMessage));
    }

    @Override
    public void commit(String topic, List<DelayMessage> delayMessages) {
        List<String> keys = Arrays.asList(
                RedisDelayQueueConstant.DelayQueue.getDqJobDetailKey(topic),
                RedisDelayQueueConstant.DelayQueue.getDqJobIndexKey(topic),
                RedisDelayQueueConstant.DelayQueue.getDqJobBackupKey(topic)
        );
        redisTemplate.execute(BATCH_COMMIT_JOB_REDIS_SCRIPT, keys, JsonHelper.toJson(delayMessages));
    }

    @Override
    public void rollback(String topic, String id) {
        this.batchRollback(topic, Collections.singletonList(id));
    }

    @Override
    public void batchRollback(String topic, List<String> ids) {
        List<String> keys = Arrays.asList(
                RedisDelayQueueConstant.DelayQueue.getDqJobDetailKey(topic),
                RedisDelayQueueConstant.DelayQueue.getDqJobIndexKey(topic),
                RedisDelayQueueConstant.DelayQueue.getDqJobBackupKey(topic),
                RedisDelayQueueConstant.DelayQueue.getDqJobDead(topic)

        );
        redisTemplate.execute(BATCH_ROLLBACK_JOB_REDIS_SCRIPT, keys,
                JsonHelper.toJson(ids), String.valueOf(redisDelayQueueConfig.getMaxRetryTimes()));
    }

    @Override
    public List<DelayMessage> popJob(String topic) {
        List<String> keys = Arrays.asList(
                RedisDelayQueueConstant.DelayQueue.getDqJobIndexKey(topic),
                RedisDelayQueueConstant.DelayQueue.getDqJobBackupKey(topic)
        );
        String result = redisTemplate.execute(POP_JOB_REDIS_SCRIPT,
                keys,
                String.valueOf(redisDelayQueueConfig.getConsumer().getPopCount()),
                String.valueOf(System.currentTimeMillis()));
        if (StringUtils.isBlank(result)) {
            return Collections.emptyList();
        }
        Collection<String> topics = Arrays.stream(JsonHelper.fromJson(result, String[].class))
                .filter(Objects::nonNull)
                .toList();
        if (CollectionUtils.isEmpty(topics)) {
            return Collections.emptyList();
        }
        List<String> delayMessageJson = redisTemplate.<String, String>opsForHash()
                .multiGet(RedisDelayQueueConstant.DelayQueue.getDqJobDetailKey(topic), topics);
        return delayMessageJson.stream()
                .filter(StringUtils::isNotBlank)
                .map(json -> JsonHelper.fromJson(json, DelayMessage.class))
                .toList();
    }

    @Override
    public DelayMessage queryJob(String topic, String id) {
        String result = redisTemplate.<String, String>opsForHash()
                .get(RedisDelayQueueConstant.DelayQueue.getDqJobDetailKey(topic), id);
        if (StringUtils.isBlank(result)) {
            return null;
        }
        return JsonHelper.fromJson(result, DelayMessage.class);
    }

    @Override
    public boolean existsJob(String topic, String id) {
        String result = redisTemplate.<String, String>opsForHash()
                .get(RedisDelayQueueConstant.DelayQueue.getDqJobDetailKey(topic), id);
        return StringUtils.isNotBlank(result);
    }

    static {
        BATCH_ADD_JOB_REDIS_SCRIPT = new DefaultRedisScript<>();
        BATCH_ADD_JOB_REDIS_SCRIPT.setLocation(
                new ClassPathResource(RedisDelayQueueConstant.LuaScript.BATCH_ADD_JOB_REDIS_SCRIPT_LUA_PATH));
        BATCH_ADD_JOB_REDIS_SCRIPT.setResultType(String.class);


        BATCH_SAVE_JOB_REDIS_SCRIPT = new DefaultRedisScript<>();
        BATCH_SAVE_JOB_REDIS_SCRIPT.setLocation(
                new ClassPathResource(RedisDelayQueueConstant.LuaScript.BATCH_SAVE_JOB_REDIS_SCRIPT_LUA_PATH));
        BATCH_SAVE_JOB_REDIS_SCRIPT.setResultType(String.class);

        FORCE_COMMIT_JOB_REDIS_SCRIPT = new DefaultRedisScript<>();
        FORCE_COMMIT_JOB_REDIS_SCRIPT.setLocation(
                new ClassPathResource(RedisDelayQueueConstant.LuaScript.FORCE_COMMIT_JOB_REDIS_SCRIPT_LUA_PATH));
        FORCE_COMMIT_JOB_REDIS_SCRIPT.setResultType(String.class);

        BATCH_COMMIT_JOB_REDIS_SCRIPT = new DefaultRedisScript<>();
        BATCH_COMMIT_JOB_REDIS_SCRIPT.setLocation(
                new ClassPathResource(RedisDelayQueueConstant.LuaScript.BATCH_COMMIT_JOB_REDIS_SCRIPT_LUA_PATH));
        BATCH_COMMIT_JOB_REDIS_SCRIPT.setResultType(String.class);

        BATCH_ROLLBACK_JOB_REDIS_SCRIPT = new DefaultRedisScript<>();
        BATCH_ROLLBACK_JOB_REDIS_SCRIPT.setLocation(
                new ClassPathResource(RedisDelayQueueConstant.LuaScript.BATCH_ROLLBACK_JOB_REDIS_SCRIPT_LUA_PATH));
        BATCH_ROLLBACK_JOB_REDIS_SCRIPT.setResultType(String.class);

        POP_JOB_REDIS_SCRIPT = new DefaultRedisScript<>();
        POP_JOB_REDIS_SCRIPT.setLocation(
                new ClassPathResource(RedisDelayQueueConstant.LuaScript.POP_JOB_REDIS_SCRIPT_LUA_PATH));
        POP_JOB_REDIS_SCRIPT.setResultType(String.class);
    }
}