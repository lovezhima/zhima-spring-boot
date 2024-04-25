package com.lovezhima.boot.web.response;

import com.lovezhima.boot.web.response.advice.ResultResponseBodyAdvice;

import java.lang.annotation.*;

/**
 * 响应自定义返回JSON体
 *
 * @see ResultResponseBodyAdvice
 *
 * @author king on 2023/7/2
 * @since 2023.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface ResponseResult {

    /**
     * 使用Result响应
     * @return boolean
     */
    boolean useResult() default true;
}
