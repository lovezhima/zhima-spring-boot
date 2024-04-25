package com.lovezhima.boot.core.util.generator;

/**
 * IdGenerator 注册
 *
 * @author king
 * @since 2023.1
 */
public interface IdGeneratorRegistry<T> {

    /**
     * 注册一个IdGenerator
     *
     * @param registry IdGenerator
     */
    void register(IdGenerator<T> registry);
}
