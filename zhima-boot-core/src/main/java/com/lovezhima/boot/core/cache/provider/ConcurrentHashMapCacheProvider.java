package com.lovezhima.boot.core.cache.provider;

import com.lovezhima.core.cache.CacheProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author king
 * @since 2023.1
 */
public class ConcurrentHashMapCacheProvider<K, V> implements CacheProvider<K, V> {

    private final Map<K, V> cache = new ConcurrentHashMap<>(64);

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void set(K key, V value) {
        cache.put(key, value);
    }
}
