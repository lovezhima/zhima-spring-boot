package com.lovezhima.boot.core.exception;

import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * //TODO 类描述信息与实现思路
 *
 * @author king on 2023/6/29
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
public class DuplicateErrorCodeException extends BusinessRuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateErrorCodeException(IErrorCode errorCode) {
        super(errorCode);
    }

    public DuplicateErrorCodeException(IErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
