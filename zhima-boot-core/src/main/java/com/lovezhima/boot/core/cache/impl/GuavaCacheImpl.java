package com.lovezhima.boot.core.cache.impl;

import com.google.common.cache.Cache;
import com.lovezhima.boot.core.cache.provider.GuavaCacheProvider;

/**
 * GuavaCacheImpl
 *
 * @author king
 * @since 2023.1
 */
public class GuavaCacheImpl<K, V> extends AbstractCache<K, V> {

    public GuavaCacheImpl(Cache<K, V> cache) {
        super(new GuavaCacheProvider<>(cache));
    }

    @Override
    protected V buildCacheValue(K key) {
        return null;
    }
}
