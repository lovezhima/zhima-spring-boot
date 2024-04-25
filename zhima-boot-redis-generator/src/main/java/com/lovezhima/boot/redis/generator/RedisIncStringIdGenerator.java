package com.lovezhima.boot.redis.generator;

import com.lovezhima.boot.core.constant.Constants;
import com.lovezhima.boot.core.exception.CommonRuntimeException;
import com.lovezhima.boot.core.util.generator.IdGenerator;
import com.lovezhima.boot.core.util.generator.IdGeneratorConfig;
import com.lovezhima.boot.core.util.generator.PreIdGenerator;
import com.lovezhima.core.util.generator.IdTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * // TODO 做成配置式
 *
 * @author king on 2024/3/1
 */
@Slf4j
@Component
public class RedisIncStringIdGenerator extends PreIdGenerator<String> implements IdGenerator<String> {

    private static final String REDIS_INCREMENT_KEY = "id_counter";
    private static final String ID_FORMAT = "%s%s%s%s";
    private final StringRedisTemplate redisTemplate;
    private final IdGeneratorConfig config;


    public RedisIncStringIdGenerator(StringRedisTemplate redisTemplate, IdGeneratorConfig config) {
        this.redisTemplate = redisTemplate;
        this.config = config;
    }

    @Override
    protected String doGenId(String sceneCode) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.DateTimeFormatPattern.NONE_DATE));
        IdGeneratorConfig.RedisConditionGeneratorConfig.SensesConfig config =
                this.config.getRedis().getSenses().get(sceneCode);
        String prefix = config.getPrefix();
        int shard = config.getShard();
        long maxLimit = config.getMaxLimit();
        int serialLength = config.getSerialLength();
        int code = Math.abs(now.hashCode());
        int sharding = code % shard;
        long result = doIncrement(config);
        if (result > maxLimit) {
            throw new CommonRuntimeException("Max id is " + maxLimit);
        }
        // 十进制转八进制
        long octalId = Long.parseLong(Long.toOctalString(result));
        // 展示位
        String showId = String.format("%0" + serialLength + "d", octalId);

        String serialId = showId.substring(showId.length() - serialLength / 2)
                + showId.substring(0, showId.length() - serialLength / 2);
        return String.format(ID_FORMAT, prefix, now, String.format("%02d", sharding), serialId);
    }

    private long doIncrement(IdGeneratorConfig.RedisConditionGeneratorConfig.SensesConfig config) {
        long start = config.getMinLimit();
        BoundValueOperations<String, String> boundValueOps = redisTemplate.boundValueOps(REDIS_INCREMENT_KEY);
        Long incId = boundValueOps.increment();
        if (incId == null) {
            throw new CommonRuntimeException("Redis increment id return null");
        }
        if (incId == 1L) {
            boundValueOps.expire(1, TimeUnit.DAYS);
        } else {
            Long expireTime = boundValueOps.getExpire();
            if (expireTime != null && expireTime == -1L) {
                boundValueOps.expire(1, TimeUnit.DAYS);
            }
        }
        return start + incId;
    }

    @Override
    public IdTypeEnum idType() {
        return IdTypeEnum.REDIS;
    }

    @Override
    public IdGeneratorConfig getConfig() {
        return this.config;
    }
}
