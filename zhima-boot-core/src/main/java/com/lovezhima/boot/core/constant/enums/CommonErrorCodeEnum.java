package com.lovezhima.boot.core.constant.enums;

import com.lovezhima.boot.core.exception.IErrorCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * 常用的错误响应码
 *
 * @author king on 2023/6/25
 * @since 2023.1
 */
@Getter
@Slf4j
@ToString
public enum CommonErrorCodeEnum implements IErrorCode {

    /**
     * 操作成功
     */
    SUCCESS("0", "处理成功"),

    /**
     * 处理失败
     * @see RuntimeException
     * @see com.lovezhima.boot.core.exception.CommonRuntimeException
     */
    COMMON_FAIL("-1", "处理失败"),

    /**
     * @see com.lovezhima.boot.core.exception.BusinessRuntimeException
     *
     */
    BUSINESS_FAIL("-2", "业务异常"),

    /**
     * RPC
     */
    RPC_FAIL("-3", "调用失败"),


    RATE_LIMIT("-4", "访问频次过快，请稍后再试"),

    /**
     * 请求失败
     */
    ERR_BAD_REQUEST("400", "请求失败"),

    /**
     * 401 UNAUTHORIZED
     */
    ERR_UNAUTHORIZED("401", "用户未登录"),

    /**
     * 403 FORBIDDEN
     */
    ERR_FORBIDDEN("403", "拒绝访问"),

    /**
     * 404 NOT_FOUND
     */
    ERR_NOT_FOUND("404", "资源不存在"),

    /**
     * 422 UN_PROCESSABLE_ENTITY
     */
    ERR_UN_PROCESSABLE_ENTITY("422", "客户端请求参数错误"),

    /**
     * 500 INTERNAL_SERVER_ERROR
     */
    ERR_INTERNAL_SERVER_ERROR("500", "服务异常"),
    ;

    /**
     * code
     */
    private final String code;

    /**
     * msg
     */
    private final String msg;

    CommonErrorCodeEnum(String code, String defaultMsg) {
        this.code = code;
        this.msg = defaultMsg;
        load();
    }
}
