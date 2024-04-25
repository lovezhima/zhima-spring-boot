package com.lovezhima.boot.core.util.convert;

/**
 * 类型转换，去除代码检查警告
 *
 * @author jin.yuan02@hand-china.com on 2023/5/20
 */
public interface CastUtils {

    /**
     * 类型转换
     *
     * @param object 转换前对象
     * @param <T>    转换对象类型
     * @return 转换后对象
     */
    @SuppressWarnings("all")
    public static <T> T cast(Object object) {
        return (T) object;
    }
}
