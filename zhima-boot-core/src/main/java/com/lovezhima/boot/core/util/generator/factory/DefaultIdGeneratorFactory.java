package com.lovezhima.boot.core.util.generator.factory;

import com.lovezhima.boot.core.util.generator.IdGenerator;
import com.lovezhima.core.util.generator.IdTypeEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 默认的ID生成
 *
 * @author king
 * @since 2023.1
 */
public class DefaultIdGeneratorFactory<T> implements IdGeneratorFactory<T> {
    private final Map<IdTypeEnum, IdGenerator<T>> idGenerators;

    public DefaultIdGeneratorFactory() {
        this.idGenerators = new HashMap<>(8);
    }

    public DefaultIdGeneratorFactory(List<IdGenerator<T>> idGenerators) {
        this.idGenerators = idGenerators.stream()
                .collect(Collectors.toMap(IdGenerator<T>::idType, Function.identity()));
    }

    @Override
    public IdGenerator<T> creation(IdTypeEnum type) {
        if (!idGenerators.containsKey(type)) {
            throw new IllegalArgumentException("[IdGenerator] " +
                    "The [" + type.toString() + "] type id generator can not found.");
        }
        return idGenerators.get(type);
    }

    @Override
    public void register(IdGenerator<T> registry) {
        idGenerators.put(registry.idType(), registry);
    }
}
