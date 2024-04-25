package com.lovezhima.boot.core.util.generator.impl;

import com.lovezhima.boot.core.util.generator.IdGenerator;
import com.lovezhima.core.util.generator.IdTypeEnum;

/**
 * //TODO 类描述信息与实现思路
 *
 * @author king
 * @date 2023/8/4
 */
public class SnowflakeIdGenerator implements IdGenerator<Long> {

    /**
     * 起始的时间戳
     */
    private final static long START_STAMP = 1480166465631L;
    /**
     * 序列号占用的位数,每一部分占用的位数
     */
    private final static long SEQUENCE_BIT = 12;
    /**
     * 机器标识占用的位数
     */
    private final static long MACHINE_BIT = 5;
    /**
     * 数据中心占用的位数
     */
    private final static long DATACENTER_BIT = 5;
    /**
     * 数据中心
     */
    private final static long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);

    /**
     * 机器数量
     */
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);

    /**
     * 序列位
     */
    final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;

    /**
     * 每一部分向左的位移
     */
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;

    /**
     * 每一部分向左的位移
     */
    private final static long TIMESTAMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    /**
     * 数据中心
     */
    private final long datacenterId;

    /**
     * 机器标识
     */
    private final long machineId;

    /**
     *  序列号
     */
    private long sequence = 0L;

    /**
     * 上一次时间戳
     */
    private long lastStamp = -1L;

    public SnowflakeIdGenerator(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }


    @Override
    public Long genId() {
        return nextId();
    }

    @Override
    public IdTypeEnum idType() {
        return IdTypeEnum.SNOWFLAKE;
    }

    public synchronized long nextId() {
        long currStmp = getNewStamp();
        if (currStmp < lastStamp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStmp == lastStamp) {
            // 相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            // 不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStamp = currStmp;

        // 时间戳部分 | 数据中心部分 | 机器标识部分 | 序列号部分
        return (currStmp - START_STAMP) << TIMESTAMP_LEFT
                | datacenterId << DATACENTER_LEFT
                | machineId << MACHINE_LEFT
                | sequence;
    }

    private long getNextMill() {
        long mill = getNewStamp();
        while (mill <= lastStamp) {
            mill = getNewStamp();
        }
        return mill;
    }

    private long getNewStamp() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        SnowflakeIdGenerator snowflake = new SnowflakeIdGenerator(2, 3);
        for (int i = 0; i < 10; i++) {
            System.out.println(snowflake.genId());
        }
    }
}
