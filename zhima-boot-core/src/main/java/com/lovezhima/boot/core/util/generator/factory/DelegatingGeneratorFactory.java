package com.lovezhima.boot.core.util.generator.factory;

import com.lovezhima.boot.core.util.generator.IdGenerator;
import com.lovezhima.core.util.generator.IdTypeEnum;

/**
 * //TODO 类描述信息与实现思路
 *
 * @author king
 * @since 2023.1
 */
public class DelegatingGeneratorFactory<T> implements IdGeneratorFactory<T> {

    private final IdGeneratorFactory<T> delegate;

    public DelegatingGeneratorFactory(IdGeneratorFactory<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public IdGenerator<T> creation(IdTypeEnum type) {
        return delegate.creation(type);
    }

    @Override
    public void register(IdGenerator<T> registry) {
        delegate.register(registry);
    }
}
