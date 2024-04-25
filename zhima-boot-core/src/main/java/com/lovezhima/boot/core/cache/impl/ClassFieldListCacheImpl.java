package com.lovezhima.boot.core.cache.impl;

import com.lovezhima.boot.core.cache.provider.ConcurrentHashMapCacheProvider;
import java.lang.reflect.Field;
import java.util.List;

/**
 *
 * @author king
 * @since 2023.1
 */
public class ClassFieldListCacheImpl extends AbstractCache<Class<?>, List<Field>> {

    /**
     * 新建单例
     *
     * @since 0.1.63
     */
    private static final ClassFieldListCacheImpl INSTANCE = new ClassFieldListCacheImpl();

    /**
     * 私有化构造器
     *
     * @since 0.1.63
     */
    private ClassFieldListCacheImpl() {
        super(new ConcurrentHashMapCacheProvider<>());
    }

    /**
     * 获取单例
     *
     * @return 单例
     * @since 0.1.63
     */
    public static ClassFieldListCacheImpl getInstance() {
        return INSTANCE;
    }

    @Override
    protected List<Field> buildCacheValue(Class<?> key) {
        return null;
    }
}