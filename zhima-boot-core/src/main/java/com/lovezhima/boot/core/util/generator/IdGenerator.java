package com.lovezhima.boot.core.util.generator;

import com.lovezhima.core.util.generator.IdTypeEnum;

import java.util.function.Function;

/**
 * ID生成器
 *
 * @author king
 * @since 2023.1
 */
public interface IdGenerator<T> {

    /**
     * 生成ID
     *
     * @return ID
     */
    T genId();

    /**
     * 生成ID，对ID做后续处理
     *
     * @param processing 后续处理
     * @return ID
     */
    default <R> R genId(Function<T, R> processing) {
       return processing.apply(genId());
    }

    /**
     * 类型
     *
     * @return IdTypeEnum
     */
    IdTypeEnum idType();
}
