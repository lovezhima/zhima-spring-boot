package com.lovezhima.boot.web.response.entity;

import com.lovezhima.boot.core.exception.IErrorCode;

/**
 * @author king on 2024/3/27
 */
public class ExceptionResponse extends ExceptionResult<Object> {
    public ExceptionResponse(IErrorCode codeEnum, Object data) {
        super(codeEnum, data);
    }
}
