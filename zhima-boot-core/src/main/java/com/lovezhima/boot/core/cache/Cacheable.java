package com.lovezhima.core.cache;

/**
 * 缓存接口
 *
 * @author king
 * @since 2023.1
 */
public interface Cacheable<K, V> {

    /**
     * 根据 key 获取对应的结果
     *
     * @param key key
     * @return 结果
     */
    V get(K key);

    /**
     * 设置内容
     *
     * @param key   key
     * @param value value
     */
    void set(K key, V value);
}