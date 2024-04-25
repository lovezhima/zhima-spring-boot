package com.lovezhima.boot.core.cache.provider;

import com.google.common.cache.Cache;
import com.lovezhima.core.cache.CacheProvider;

/**
 * GuavaCacheProvider
 *
 * @author king
 * @since 2023.1
 */
public class GuavaCacheProvider<K, V> implements CacheProvider<K, V> {

    private final Cache<K, V> cache;

    public GuavaCacheProvider(Cache<K, V> cache) {
        this.cache = cache;
    }

    @Override
    public V get(K key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void set(K key, V value) {
        cache.put(key, value);
    }
}
