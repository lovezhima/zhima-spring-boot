package com.lovezhima.boot.core.cache.impl;

import com.lovezhima.core.cache.CacheProvider;
import com.lovezhima.core.cache.Cacheable;

import java.util.Objects;

/**
 * 抽象 cache 实现
 *
 * @author king
 */
public abstract class AbstractCache<K, V> implements Cacheable<K, V> {

    /**
     * CacheProvider
     */
    private final CacheProvider<K, V> cacheProvider;


    public AbstractCache(CacheProvider<K, V> cacheProvider) {
        this.cacheProvider = cacheProvider;
    }

    /**
     * 构建值
     *
     * @param key key
     * @return 结果
     */
    protected abstract V buildCacheValue(final K key);

    @Override
    public V get(K key) {
        V result = cacheProvider.get(key);
        if (!Objects.isNull(result)) {
            return result;
        }
        // 构建
        result = buildCacheValue(key);
        // 设置
        set(key, result);
        return result;
    }

    @Override
    public void set(K key, V value) {
        cacheProvider.set(key, value);
    }
}
