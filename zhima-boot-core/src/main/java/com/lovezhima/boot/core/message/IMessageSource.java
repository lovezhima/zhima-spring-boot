package com.lovezhima.boot.core.message;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;

/**
 * 消息资源
 *
 * @author king on 2023/6/26
 */
public interface IMessageSource {

    Logger log = LoggerFactory.getLogger(IMessageSource.class);

    /**
     * 设置parent资源
     *
     * @param messageSource 消息资源
     */
    default void setParent(MessageSource messageSource) {
    }

    /**
     * 解析消息
     *
     * @param parentMessageSource parent资源
     * @param code                编码
     * @param args                参数
     * @param locale              本地语言
     * @return Message消息对象
     */
    default Message resolveMessage(ReloadableResourceBundleMessageSource parentMessageSource, String code,
                                   Object[] args, Locale locale) {
        return this.resolveMessage(parentMessageSource, code, args, null, locale);
    }

    /**
     * 解析消息
     *
     * @param parentMessageSource parent资源
     * @param code                编码
     * @param args                参数
     * @param defaultMessage      默认本地语言
     * @param locale              Message消息对象
     * @return Message消息对象
     */
    default Message resolveMessage(ReloadableResourceBundleMessageSource parentMessageSource, String code,
                                   Object[] args, String defaultMessage, Locale locale) {
        Message message = new Message();
        String desc = null;

        try {
            desc = parentMessageSource.getMessage(code, null, locale);
        } catch (NoSuchMessageException var9) {
            log.warn("resolveMessage not found message for code={}", code);
        }

        if (StringUtils.isBlank(desc) && StringUtils.isNotBlank(defaultMessage)) {
            desc = defaultMessage;
        }

        if (StringUtils.isNotBlank(desc) && ArrayUtils.isNotEmpty(args)) {
            desc = (new MessageFormat(desc, locale)).format(args);
        }

        if (StringUtils.isBlank(desc)) {
            desc = code;
        }

        final String tempDesc = desc;
        message = Optional.of(message).map(m -> m.setDesc(tempDesc)).orElse(new Message(code, desc));
        log.debug("resolve message: code={}, message={}, language={}", code, message, locale);
        return message;
    }
}
