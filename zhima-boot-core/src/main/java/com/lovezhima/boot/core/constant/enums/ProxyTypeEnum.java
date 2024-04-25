package com.lovezhima.boot.core.constant.enums;

/**
 * <p> 代理类型枚举 </p>
 *
 * @author king
 * @since 2023.1
 */
public enum ProxyTypeEnum {

    /**
     * 不执行任何代理
     */
    NONE,

    /**
     * JDK动态代理
     */
    JDK_DYNAMIC,

    /**
     * CGLIB 动态代理
     */
    CGLIB;
}