package com.lovezhima.boot.core.exception;

import java.io.Serial;

/**
 * 通用的运行时异常
 *
 * @author king
 * @since 2023.1
 */
public class CommonRuntimeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public CommonRuntimeException() {
    }

    public CommonRuntimeException(String message) {
        super(message);
    }

    public CommonRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonRuntimeException(Throwable cause) {
        super(cause);
    }

    public CommonRuntimeException(String message,
                                  Throwable cause,
                                  boolean enableSuppression,
                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}