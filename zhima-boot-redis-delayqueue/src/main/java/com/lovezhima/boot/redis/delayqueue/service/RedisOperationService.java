package com.lovezhima.boot.redis.delayqueue.service;

import com.lovezhima.boot.redis.delayqueue.entity.DelayMessage;
import java.util.List;

/**
 * redis延迟消息，提供了addJob、saveJob、popJob、commit、rollback的操作。
 *
 * @author king
 * @date 2023/8/13
 */
public interface RedisOperationService {

    /**
     * 添加一个job任务
     *
     * @param topic        主题
     * @param delayMessage 延迟消息
     * @return boolean
     */
    boolean addJob(String topic, DelayMessage delayMessage);

    /**
     * 批量添加job任务
     *
     * @param topic        主题
     * @param delayMessage 批量延迟消息
     * @return boolean
     */
    boolean batchAddJob(String topic, List<DelayMessage> delayMessage);


    /**
     * 保存job任务
     *
     * @param topic           主题
     * @param delayMessage    延迟消息
     * @param compareCoverage 比较id是否相等，如果为true，则相同的ID旧的值被覆盖
     * @return boolean
     */
    boolean saveJob(String topic, DelayMessage delayMessage, boolean compareCoverage);

    /**
     * 批量保存job任务
     *
     * @param topic           主题
     * @param delayMessages   批量延迟消息
     * @param compareCoverage 比较id是否相等，如果为true，则相同的ID旧的值被覆盖
     * @return boolean
     */
    boolean batchSaveJob(String topic, List<DelayMessage> delayMessages, boolean compareCoverage);

    /**
     * 强制提交消息
     *
     * @param topic 主题
     * @param id    消息id
     */
    void forceCommit(String topic, String id);

    /**
     * 提交消息
     *
     * @param topic        主题
     * @param delayMessage 延迟消息
     */
    void commit(String topic, DelayMessage delayMessage);


    /**
     * 提交消息
     *
     * @param topic         主题
     * @param delayMessages 延迟消息
     */
    void commit(String topic, List<DelayMessage> delayMessages);

    /**
     * 回滚任务
     *
     * @param topic 主题
     * @param id    消息id
     */
    void rollback(String topic, String id);

    /**
     * 回滚任务
     *
     * @param topic 主题
     * @param ids   消息id集合
     */
    void batchRollback(String topic, List<String> ids);

    /**
     * pop一个job任务
     *
     * @param topic 主题
     * @return List<DelayMessage>
     */
    List<DelayMessage> popJob(String topic);

    /**
     * 查询任务
     *
     * @param topic 主题
     * @param id    消息id
     * @return DelayMessage
     */
    DelayMessage queryJob(String topic, String id);

    /**
     * 是否存在一个job
     *
     * @param topic 主题
     * @param id    消息id
     * @return boolean
     */
    boolean existsJob(String topic, String id);
}
