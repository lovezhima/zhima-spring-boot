package com.lovezhima.core.proxy;

/**
 * 代理接口
 * @author king
 * @since 2023.1
 */
public interface IProxy {

    /**
     * 获取代理对象
     * 1. 如果是实现了接口，默认使用 JdkDynamicProxy 即可。
     * 2. 如果没有实现接口，默认使用 CGLIB 实现代理。
     * @return 代理对象
     */
    Object getProxy();
}