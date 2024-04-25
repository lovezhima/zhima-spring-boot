package com.lovezhima.boot.redis.delayqueue.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lovezhima.boot.core.constant.Constants;
import com.lovezhima.boot.core.util.convert.CastUtils;
import com.lovezhima.boot.redis.delayqueue.config.RedisDelayQueueConfig;
import com.lovezhima.boot.redis.delayqueue.entity.DelayMessage;
import com.lovezhima.boot.redis.delayqueue.service.RedisCallback;
import com.lovezhima.boot.redis.delayqueue.service.RedisOperationService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * redis延迟队列容器
 * <p>
 * 提供一个延迟队列的容器实现，基于Spring的应用，Bean的初始化，销毁，BeanName的包装和Bean的生命周期
 *
 * @author king
 * @date 2023/7/29
 */
@Slf4j
public class RedisDelayQueueContainer
        implements InitializingBean, DisposableBean, BeanNameAware, SmartLifecycle {

    /**
     * 定义一个monitor的监视器用于获取锁
     */
    private final Object monitor = new Object();

    /**
     * 所有RedisTopic
     **/
    private final Map<String, RedisCallback> callbackRegistry = new ConcurrentHashMap<>(255);

    /**
     * 任务线程池, 分配给topic
     */
    private ExecutorService taskExecutor;

    /**
     * 线程池标识位
     */
    private volatile boolean manageExecutor = false;

    /**
     * 初始化等待时间
     */
    private final long initWait = TimeUnit.SECONDS.toMillis(5);

    /**
     * Redis延迟队列容器启动标识
     */
    private volatile boolean running = false;

    /**
     * 错误标识
     */
    private volatile boolean errorFlag = false;

    /**
     * 容器初始化标识
     */
    private volatile boolean initialized = false;


    private String beanName;

    public static final String DEFAULT_THREAD_NAME_PREFIX = RedisDelayQueueContainer.class.getSimpleName() + "-";

    @Setter
    private RedisOperationService redisOperationService;

    @Setter
    private RedisDelayQueueConfig redisDelayQueueConfig;

    /**
     * 容器启动调用start, 将注册的RedisCallback分发到taskExecutor
     */
    @Override
    public void start() {
        if (!isRunning()) {
            log.info("RedisDelayQueueContainer starting...");
            running = true;
            synchronized (monitor) {
                if (isRunning()) {
                    if (!callbackRegistry.isEmpty()) {
                        log.info("Redis delay queue topic runTopicsThreads topic.size:{}", callbackRegistry.size());
                        assert taskExecutor != null;
                        for (Map.Entry<String, RedisCallback> entry : callbackRegistry.entrySet()) {
                            RedisCallback callback = entry.getValue();
                            taskExecutor.execute(() -> handleCallback(callback));
                        }
                    } else {
                        try {
                            // 等待5s
                            monitor.wait(initWait);
                        } catch (InterruptedException e) {
                            // 中断线程，启动失败
                            Thread.currentThread().interrupt();
                            running = false;
                            return;
                        }
                    }
                }
            }
            log.info("RedisDelayQueueContainer started, the topic size [{}]", callbackRegistry.size());
        }
    }

    @Override
    public void stop() {
        if (isRunning()) {
            running = false;
        }
        log.info("Stopped RedisDelayQueueContainer");
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }


    @Override
    public void destroy() throws Exception {
        initialized = false;
        stop();
        if (manageExecutor && taskExecutor instanceof DisposableBean) {
            ((DisposableBean) taskExecutor).destroy();
            log.info("Stopped internally-managed task executor");
        }
    }

    /**
     * Bean实例初始化操作
     */
    @Override
    public void afterPropertiesSet() {
        if (ObjectUtils.isEmpty(taskExecutor)) {
            manageExecutor = true;
            taskExecutor = initTaskExecutor();

        }
        initialized = true;
    }

    @Override
    public void setBeanName(@NonNull String name) {
        this.beanName = name;
    }


    /**
     * 初始化TaskExecutor
     * <p>
     * 自定义线程池prefix名称、同步队列，拒绝策略、和hookShutdown的钩子在容器销毁时候关闭
     * <p>
     * 线程池大小为为topicRegister的大小
     *
     * @return ExecutorService
     */
    protected ExecutorService initTaskExecutor() {
        String threadNamePrefix = (beanName != null ? beanName + "-" : DEFAULT_THREAD_NAME_PREFIX);
        int callbackSize = callbackRegistry.size();
        if (callbackSize == 0) {
            log.error("Redis callback size is zero, can not running redis delay queue task");
            this.errorFlag = true;
            return null;
        }
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(callbackSize,
                callbackSize, Constants.Digital.ZERO, TimeUnit.MILLISECONDS, new SynchronousQueue<>(),
                new ThreadFactoryBuilder()
                        .setNameFormat(threadNamePrefix + "-%d")
                        .setUncaughtExceptionHandler((thread, throwable) ->
                                log.error("{} catching the uncaught exception, ThreadName: [{}]",
                                        threadNamePrefix,
                                        thread.toString(),
                                        throwable))
                        .build(), new ThreadPoolExecutor.AbortPolicy());
        //CommonExecutor.hookShutdownThreadPool(threadPoolExecutor, threadNamePrefix);
        return threadPoolExecutor;
    }

    protected void handleCallback(RedisCallback callback) {
        log.info("[handleRedisTopic] {}, callback redis topic [{}]",
                Thread.currentThread().getName(), callback.getTopic());
        while (running) {
            List<DelayMessage> delayMessages = redisOperationService.popJob(callback.getTopic());
            if (CollectionUtils.isEmpty(delayMessages)) {
                sleep();
                continue;
            }
            invokeCallback(callback, delayMessages);
        }
    }

    protected void invokeCallback(RedisCallback topic, List<DelayMessage> delayMessages) {
        for (DelayMessage delayMessage : delayMessages) {
            boolean errorFlag = false;
            if (ObjectUtils.isNotEmpty(delayMessage)) {
                try {
                    RedisCallback callback = CastUtils.cast(topic);
                    callback.onMessage(delayMessage);
                } catch (Throwable ex) {
                    log.warn("InvokeCallback onMessage: {} has been exception: {}", delayMessage, ex.getMessage());
                    errorFlag = true;
                } finally {
                    if (errorFlag) {
                        redisOperationService.rollback(topic.getTopic(), delayMessage.getId());
                    } else {
                        redisOperationService.commit(topic.getTopic(), delayMessage);
                    }
                }
            }
        }
    }

    protected void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(redisDelayQueueConfig.getConsumer().getStopMills());
        } catch (InterruptedException e) {
            log.error("PopJob is empty, sleep fail {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public void registerCallback(RedisCallback callback) {
        callbackRegistry.put(callback.getTopic(), callback);
    }
}
