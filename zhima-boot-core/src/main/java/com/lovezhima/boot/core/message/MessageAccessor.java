package com.lovezhima.boot.core.message;

import com.lovezhima.boot.core.constant.Constants;
import com.lovezhima.boot.core.helper.LanguageHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.List;
import java.util.Locale;

/**
 * @author king on 2023/6/26
 * @since 1.0
 */
public class MessageAccessor {
    private static final ReloadableResourceBundleMessageSource PARENT_MESSAGE_SOURCE;
    private static final List<String> BASENAMES = List.of("classpath*:i18n/basic_message");

    static {
        PARENT_MESSAGE_SOURCE = new ReloadableResourceBundleMessageSource();
        PARENT_MESSAGE_SOURCE.setBasenames(getBasenames());
        PARENT_MESSAGE_SOURCE.setDefaultEncoding(Constants.DEFAULT_CHARSET);
        PARENT_MESSAGE_SOURCE.setUseCodeAsDefaultMessage(true);
    }

    private MessageAccessor() throws IllegalAccessException {
        throw new IllegalAccessException();
    }


    public static String[] getBasenames() {
        return ArrayUtils.toStringArray(BASENAMES.toArray());
    }

    public static void addBasenames(String... names) {
        PARENT_MESSAGE_SOURCE.addBasenames(names);
        PARENT_MESSAGE_SOURCE.clearCache();
    }

    public static void setBasenames(String... names) {
        PARENT_MESSAGE_SOURCE.setBasenames(names);
        PARENT_MESSAGE_SOURCE.clearCache();
    }

    public static Message getLocalMessage(String code) {
        return new Message(code, PARENT_MESSAGE_SOURCE.getMessage(code, null, LanguageHelper.locale()));
    }

    public static Message getLocalMessage(String code, Locale locale) {
        return new Message(code, PARENT_MESSAGE_SOURCE.getMessage(code, null, locale));
    }

    public static Message getLocalMessage(String code, Object[] args) {
        return new Message(code, PARENT_MESSAGE_SOURCE.getMessage(code, args, LanguageHelper.locale()));
    }

    public static Message getLocalMessage(String code, Object[] args, Locale locale) {
        return new Message(code, PARENT_MESSAGE_SOURCE.getMessage(code, args, locale));
    }
}
