package com.lovezhima.boot.core.context;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring 应用上下文
 *
 * @author king
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class SpringApplicationContextHolder implements ApplicationContextAware {

    /**
     *  提供获取上下文
     */
    @Getter
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}