package com.lovezhima.boot.core.util.generator.factory;

import com.lovezhima.boot.core.util.generator.IdGenerator;
import com.lovezhima.boot.core.util.generator.IdGeneratorRegistry;
import com.lovezhima.core.util.generator.IdTypeEnum;

/**
 * ID生成器工厂
 *
 * @author king
 * @since 2023.1
 */
public interface IdGeneratorFactory<T> extends IdGeneratorRegistry<T> {

    /**
     * 依据ID类型获取IDGenerator
     *
     * @param type ID type
     * @return IdGenerator
     */
    IdGenerator<T> creation(IdTypeEnum type);
}
