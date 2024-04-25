package com.lovezhima.boot.core.util.generator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static com.lovezhima.boot.core.util.generator.IdGeneratorConfig.GENERATOR_PREFIX;


/**
 * @author king on 2024/3/2
 */
@Configuration
@ConfigurationProperties(prefix = GENERATOR_PREFIX)
@Data
public class IdGeneratorConfig {

    public static final String GENERATOR_PREFIX = "zhima.generator";
    public static final String UUID_TYPE = "UUID";
    public static final String SNOWFLAKE_TYPE = "SNOWFLAKE";
    public static final String REDIS_TYPE = "REDIS";

    private Boolean enabled = false;

    private PreGeneratorConfig uuid = new PreGeneratorConfig();

    private PreGeneratorConfig snowflake = new PreGeneratorConfig();

    private RedisConditionGeneratorConfig redis = new RedisConditionGeneratorConfig();

    @Data
    public static class PreGeneratorConfig {
        /**
         * 预生成ID开关
         */
        protected Boolean preEnabled = false;

        /**
         * 预生成ID个数
         */
        protected Integer preCount = 500;
    }


    @Data
    public static class RedisConditionGeneratorConfig {

        /**
         * 场景
         */
        private Map<String, SensesConfig> senses;

        @EqualsAndHashCode(callSuper = true)
        @Data
        public static class SensesConfig extends PreGeneratorConfig {

            private String prefix = "";

            private Long minLimit = 100000000L;

            private Long maxLimit = 1799999999L;

            private Integer shard = 1;

            private Integer serialLength = 8;

            private Boolean confuse = false;
        }
    }
}