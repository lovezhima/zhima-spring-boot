package com.lovezhima.boot.core.proxy.none;

import com.lovezhima.core.proxy.IProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 不执行任何代理的实现方式
 *
 * @author binbin.hou
 * @since 0.1.3
 */
public class NoneProxy implements InvocationHandler, IProxy {

    /**
     * 代理对象
     */
    private final Object target;

    public NoneProxy(Object target) {
        this.target = target;
    }

    /**
     * 返回原始对象，没有代理
     *
     * @return 原始对象
     */
    @Override
    public Object getProxy() {
        return this.target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(proxy, args);
    }
}