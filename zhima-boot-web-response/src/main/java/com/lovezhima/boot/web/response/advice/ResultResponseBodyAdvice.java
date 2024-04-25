package com.lovezhima.boot.web.response.advice;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lovezhima.boot.web.response.ResponseResult;
import com.lovezhima.boot.web.response.entity.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 响应内容JSON处理
 *
 * @author king
 * @since 2023.1
 */
@RestControllerAdvice
public class ResultResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final static int INIT_CACHE_COUNT = 1000;

    private final Cache<MethodParameter, Boolean>
            methodParamCache = CacheBuilder.newBuilder().maximumSize(INIT_CACHE_COUNT).build();

    /**
     * 支持的类型，返回为true则调用beforeBodyWrite，否者不进行处理
     *
     * @param returnType 方法返回类型
     * @param aClass     HttpMessageConverter
     * @return true/false
     */
    @SuppressWarnings("all")
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
        Boolean methodCache = methodParamCache.getIfPresent(returnType);
        if (Boolean.TRUE.equals(methodCache)) {
            return true;
        }
        // 如果使用解析了注解，或者Result类型返回，则不需要json
        if (Boolean.FALSE.equals(methodCache) || Result.class.isAssignableFrom(returnType.getParameterType())) {
            return false;
        }
        ResponseResult responseResult = returnType.getMethodAnnotation(ResponseResult.class);
        // 先判断method上是否有@ResponseResult注解
        if (responseResult != null) {
            methodParamCache.put(returnType, responseResult.useResult());
            return responseResult.useResult();
        }
        // 在判断类上有没有使用@ResponseResult注解
        responseResult = returnType.getContainingClass().getAnnotation(ResponseResult.class);
        if (responseResult != null) {
            methodParamCache.put(returnType, responseResult.useResult());
            return responseResult.useResult();
        }
        methodParamCache.put(returnType, false);
        return false;
    }

    /**
     * 执行supports 返回为true，则调用beforeBodyWrite
     * <p>
     * 执行返回body前相关写操作
     *
     * @param data       数据内容
     * @param returnType 返回类型
     * @param mediaType  媒介类型
     * @param aClass     消息转换
     * @param request    请求
     * @param response   响应
     * @return 经过处理后的对象
     */
    @SuppressWarnings("all")
    @Override
    public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (returnType.getParameterType().equals(Void.class)) {
            return data;
        }
        return Result.success(data);
    }
}
