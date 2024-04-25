package com.lovezhima.boot.core.exception;

import java.util.Objects;

/**
 * 自定义异常断言
 *
 * @author king on 2023/6/25
 * @since 1.0
 */
public interface Assert {

    /**
     * 创建新的异常
     * @param errObject 异常对象
     * @return 基础异常类以及其子类
     */
    BusinessRuntimeException newException(Object... errObject);

    /**
     * 创建新的异常
     * @param e 异常链
     * @param errObject 异常对象
     * @return 基础异常类以及其子类
     */
    BusinessRuntimeException newException(Throwable e, Object... errObject);

    /**
     * 断言对象是否为空
     * @param errObject 异常对象
     */
    default void assertNotNull(Object errObject) {
        if (Objects.isNull(errObject)) {
            throw newException();
        }
    }
}
