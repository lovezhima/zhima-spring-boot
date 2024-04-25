package com.lovezhima.boot.web.response.exception;

import com.lovezhima.boot.core.constant.enums.CommonErrorCodeEnum;
import com.lovezhima.boot.core.exception.BusinessRuntimeException;

import java.io.Serial;

/**
 * @author king on 2024/3/27
 */
public class RpcFailException extends BusinessRuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public RpcFailException() {
        super(CommonErrorCodeEnum.RPC_FAIL);
    }
}
