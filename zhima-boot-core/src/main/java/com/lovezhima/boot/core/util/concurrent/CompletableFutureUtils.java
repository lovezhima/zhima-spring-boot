package com.lovezhima.boot.core.util.concurrent;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * CompletableFuture 工具类
 * 多线程feign调用会导致RequestContext，SecurityContext丢失,在主线程传递
 *
 * @author king
 */
public final class CompletableFutureUtils implements ApplicationContextAware {

    private static Executor executor;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        executor = applicationContext.getBean("completableFutureThreadPool", Executor.class);
    }

    /**
     * 无返回值异步线程执行, 给定一个线程，默认使用Executor线程池
     *
     * @param runnable 线程
     * @return CompletableFuture
     */
    public static CompletableFuture<Void> runAsync(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, executor);
    }

    /**
     * 又返回值异步线程执行，给定一个supplier，默认使用Executor线程池
     *
     * @param supplier 无参有一个返回值的函数接口
     * @param <T>      返回参数类型
     * @return CompletableFuture
     */
    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, executor);
    }

    /**
     * 有返回值异步线程执行，并设置securityContext，和 requestAttributes
     *
     * @param supplier 无参有一个返回值的函数接口
     * @param executor 线程池
     * @param <T>      返回参数类型
     * @return CompletableFuture
     */
    public static <T> CompletableFuture<T> supplyAsyncWithContext(Supplier<T> supplier,
                                                                  Executor executor) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return CompletableFuture.supplyAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            return supplier.get();
        }, executor);
    }

    /**
     * 无返回值异步线程执行，并设置securityContext，和 requestAttributes
     *
     * @param runnable 运行任务
     * @param executor 线程池
     * @return CompletableFuture<Void>
     */
    public static CompletableFuture<Void> runAsyncWithContext(Runnable runnable,
                                                              Executor executor) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            runnable.run();
        }, executor);
    }
}
