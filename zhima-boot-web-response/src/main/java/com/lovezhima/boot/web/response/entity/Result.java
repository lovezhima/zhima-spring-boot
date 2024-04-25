package com.lovezhima.boot.web.response.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lovezhima.boot.core.exception.IErrorCode;
/**
 * 响应body的接口类
 * <br/>
 * 提供了code和message的返回
 *
 * @author king on 2023/6/25
 * @since 1.0
 */
public interface Result<T> {

    /**
     * 字段code
     */
    String FILED_CODE = "code";

    /**
     * 字段msg
     */
    String FILED_MSG = "msg";

    /**
     * 字段data
     */
    String FILED_DATA = "data";


    /**
     * 获取http status code
     *
     * @return http status code
     */
    String getCode();

    /**
     * http status code message
     *
     * @return 消息内容
     */
    String getMsg();


    /**
     * 获取数据内容
     *
     * @return T 数据内容
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    T getData();

    /**
     * 返回基础响应-不包含数据内容
     *
     * @param <T> BaseResponseEntity.class
     * @return BaseResponseEntity
     */
    static <T> Result<T> success() {
        return new BasicResult<>() {
        };
    }

    /**
     * 返回响应-包含数据内容
     *
     * @param data 响应数据内容
     * @param <T>  Result
     * @return Result
     */
    static <T> Result<T> success(T data) {
        return new BasicResult<>(data);
    }

    /**
     * 失败响应
     *
     * @param codeEnum code枚举
     * @param error    错误
     * @param <T>      错误
     * @return Result
     */
    static <T> Result<T> fail(IErrorCode codeEnum, T error) {
        return new ExceptionResult<>(codeEnum, error);
    }

    /**
     * 失败响应
     *
     * @param codeEnum code枚举
     * @param <T>      错误
     * @return Result
     */
    static <T> Result<T> fail(IErrorCode codeEnum) {
        return fail(codeEnum, null);
    }
}
