package com.lovezhima.boot.core.util.convert;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 集合转换
 *
 * @author jin.yuan02@hand-china.com on 2023/5/20
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionConverter {

    /**
     * 集合转集合
     *
     * @param sourceCollection 源集合
     * @param function 执行转换逻辑函数
     * @param supplier 转成目标集合
     * @return Collection<T>
     */
    public static <S, T> Collection<T> toCollection(Collection<S> sourceCollection, Function<S, T> function,
                                                    Supplier<Collection<T>> supplier) {
        if (sourceCollection == null) {
            return Collections.emptyList();
        }
        Collection<T> collection = supplier.get();
        for (S source : sourceCollection) {
            collection.add(function.apply(source));
        }
        return collection;
    }

    /**
     * 转换为List集合
     * @param sourceCollection 源集合
     * @param function 执行转换逻辑函数
     * @return ArrayList<T>
     */
    public static <S, T> List<T> toArrayList(Collection<S> sourceCollection, Function<S, T> function) {
        return (List<T>) toCollection(sourceCollection, function, ArrayList::new);
    }

    /**
     * 转换LinkedList集合
     * @param sourceCollection 源集合
     * @param function 执行转换逻辑函数
     * @return LinkedList<T>
     */
    public static <S, T> List<T> toLinkedList(Collection<S> sourceCollection, Function<S, T> function) {
        return (List<T>) toCollection(sourceCollection, function, LinkedList::new);
    }
}
