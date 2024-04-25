package com.lovezhima.boot.core.proxy;

import com.lovezhima.boot.core.constant.enums.ProxyTypeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * 代理工厂
 *
 * @author binbin.hou
 * @since 0.1.3
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProxyFactory {
    
    /**
     * 获取代理类型
     *
     * @param object 对象
     * @return 代理枚举
     */
    public static ProxyTypeEnum getProxyType(final Object object) {
        if (Objects.isNull(object)) {
            return ProxyTypeEnum.NONE;
        }
        final Class<?> clazz = object.getClass();
        /*
         * 如果targetClass本身是个接口或者targetClass是JDK Proxy生成的, 则使用JDK动态代理。
         * 参考 SpringAOP 判断
         */
        if (clazz.isInterface() || Proxy.isProxyClass(clazz)) {
            return ProxyTypeEnum.JDK_DYNAMIC;
        }
        return ProxyTypeEnum.CGLIB;
    }
}