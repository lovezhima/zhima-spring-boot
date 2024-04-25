package com.lovezhima.core.util.generator;

/**
 * ID类型
 *
 * @author king
 * @since 2023.1
 */
public enum IdTypeEnum {

    /**
     * 自增
     */
    MYSQL,

    /**
     * Redis自增
     */
    REDIS,

    /**
     * UUID
     */
    UUID,

    /**
     * 无中线
     */
    UUID_UN_CENTER_LINE,

    /**
     * 雪花算法
     */
    SNOWFLAKE,

    /**
     * 随机（包含字母数字）
     */
    RANDOM,

    /**
     * 随机数字
     */
    RANDOM_NUM,

    /**
     * md5
     */
    MD5,

}
