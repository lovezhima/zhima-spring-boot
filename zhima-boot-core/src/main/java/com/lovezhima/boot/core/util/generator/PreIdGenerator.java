package com.lovezhima.boot.core.util.generator;

import com.lovezhima.boot.core.exception.CommonRuntimeException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * 抽象实现一个预生成ID，放入到队列中，每次调用genId时先去队列中获取ID，后台线程会检查队列容量，容量不足时自动批量获取
 *
 * @author king on 2024/3/2
 */
@Slf4j
public abstract class PreIdGenerator<T> implements IdGenerator<T>, InitializingBean, SmartLifecycle {

    private final Map<String, ArrayBlockingQueue<T>> scenesMapping = new ConcurrentHashMap<>();

    @Getter
    private final ThreadLocal<String> scenes = new ThreadLocal<>();

    private ExecutorService executorService;

    private volatile LocalDateTime currentNow;

    private volatile boolean running = false;

    private final Object monitor = new Object();

    @PostConstruct
    public void init() {
        IdGeneratorConfig config = getConfig();
        /*
        分别获取场景
         */
        if (Objects.nonNull(config.getUuid()) && config.getUuid().getPreEnabled()) {
            scenesMapping.put(IdGeneratorConfig.UUID_TYPE,
                    new ArrayBlockingQueue<>(config.getUuid().getPreCount()));
        }
        if (Objects.nonNull(config.getSnowflake()) && config.getSnowflake().getPreEnabled()) {
            scenesMapping.put(IdGeneratorConfig.REDIS_TYPE,
                    new ArrayBlockingQueue<>(config.getSnowflake().getPreCount()));
        }
        if (Objects.nonNull(config.getRedis()) && !CollectionUtils.isEmpty(config.getRedis().getSenses())) {
            config.getRedis().getSenses()
                    .forEach((key, value) -> scenesMapping.put(key, new ArrayBlockingQueue<>(value.getPreCount())));
        }
    }

    @Override
    public final T genId() {
        return switch (this.idType()) {
            case UUID, SNOWFLAKE -> getIdInBlockingQueue(this.idType().name());
            case REDIS -> getIdInBlockingQueue(scenes.get());
            default -> throw new CommonRuntimeException("Not supported idType [ " + this.idType().name() + "]");
        };
    }

    private T getIdInBlockingQueue(String sceneCode) {
        BlockingQueue<T> queue = scenesMapping.get(sceneCode);
        try {
            return queue.take();
        } catch (InterruptedException e) {
            log.warn("genId from queue interruptedException.", e);
            Thread.currentThread().interrupt();
        } finally {
            scenes.remove();
        }
        return doGenId(sceneCode);
    }

    /**
     * 子类实现Id生成策略
     *
     * @return T 生成的ID
     */
    protected abstract T doGenId(String sceneCode);

    /**
     * 注入ExecutorService
     */
    @Override
    public void afterPropertiesSet() {
        executorService = new ThreadPoolExecutor(8,
                8, 0, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 获取Id生成配置
     *
     * @return IdGeneratorConfig
     */
    public abstract IdGeneratorConfig getConfig();

    @Override
    @SuppressWarnings("all")
    public void start() {
        if (!isRunning()) {
            log.info("PreIdGenerator thread starting...");
            running = true;
            synchronized (monitor) {
                if (isRunning()) {
                    for (final Map.Entry<String, ArrayBlockingQueue<T>> entry : scenesMapping.entrySet()) {
                        executorService.execute(() -> {
                            BlockingQueue<T> v = entry.getValue();
                            while (true) {
                                if (!v.offer(doGenId(entry.getKey()))) {
                                    try {
                                        TimeUnit.SECONDS.sleep(1);
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                    }
                                }
                            }
                        });
                    }
                    log.info("PreIdGenerator thread started.");
                } else {
                    try {
                        // 等待5s
                        monitor.wait(5000);
                    } catch (InterruptedException e) {
                        // 中断线程，启动失败
                        Thread.currentThread().interrupt();
                        running = false;
                    }
                }
            }
        }
    }


    @Override
    public void stop() {
        if (isRunning()) {
            running = false;
        }
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }
}
