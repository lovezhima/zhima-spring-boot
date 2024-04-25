package com.lovezhima.boot.core.util.generator.impl;

import com.lovezhima.boot.core.util.generator.IdGenerator;
import com.lovezhima.core.util.generator.IdTypeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * UUID生产
 *
 * @author king
 * @since 2023.1
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UUIDGenerator implements IdGenerator<String> {

    @Override
    public String genId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public IdTypeEnum idType() {
        return IdTypeEnum.UUID;
    }


    private static class UUIDGeneratorHolder {
        private static final UUIDGenerator INSTANCE = new UUIDGenerator();
    }

    public static UUIDGenerator getInstance() {
        return UUIDGeneratorHolder.INSTANCE;
    }
}
