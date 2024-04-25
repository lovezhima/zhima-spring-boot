package com.lovezhima.boot.core.helper;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * 多语言工具类
 *
 * @author king on 2023/6/26
 * @since 1.0
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LanguageHelper {

    /**
     * TODO 使用本地线程保证数据一致性
     *
     */
    private static volatile String defaultLanguage = "zh_CN";

    public static String language() {
        return defaultLanguage;
    }

    /**
     * 获取指定本地语言
     *
     * @param lang 传入一个语言
     * @return 本地语言
     */
    public static Locale locale(String lang) {
        return LocaleUtils.toLocale(StringUtils.isBlank(lang) ? language() : lang);
    }

    /**
     * 获取当前本地语言
     *
     * @return 本地语言
     */
    public static Locale locale() {
        return LocaleUtils.toLocale(language());
    }
}
