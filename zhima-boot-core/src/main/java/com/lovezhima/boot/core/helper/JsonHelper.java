package com.lovezhima.boot.core.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

/**
 * JSON辅助工具类
 * @author king
 * @since 2023.1
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonHelper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 对象转JSON
     * @param object 接受一个对象
     * @return 对象JSON
     */
    @SneakyThrows
    public static String toJson(Object object) {
        return OBJECT_MAPPER.writeValueAsString(object);
    }

    /**
     * JSON反序列化成对象
     * @param json 接受一个JSON
     * @param clazz 转化成Class类型
     * @return Class类型的对象
     * @param <T> Class对象类型
     */
    @SneakyThrows
    public static <T> T fromJson(String json, Class<T> clazz) {
        return OBJECT_MAPPER.readValue(json, clazz);
    }

    /**
     * JSON反序列化成对象
     * @param json 接受一个JSON
     * @param valueTypeRef 类型引用类型
     * @return T 指定引用类型
     * @param <T> TypeReference类型
     */
    @SneakyThrows
    public static <T> T fromJson(String json, TypeReference<T> valueTypeRef) {
        return OBJECT_MAPPER.readValue(json, valueTypeRef);
    }
}
