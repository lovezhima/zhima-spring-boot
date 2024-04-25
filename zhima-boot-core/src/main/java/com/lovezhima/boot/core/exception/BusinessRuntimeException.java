package com.lovezhima.boot.core.exception;

import com.lovezhima.boot.core.helper.ErrorCodeHelper;

import java.io.Serial;

/**
 * 业务异常类
 *
 * @author jin.yuan02@hand-china.com on 2023/5/19
 */
public class BusinessRuntimeException extends CommonRuntimeException implements IErrorCode {

    @Serial
    private static final long serialVersionUID = 1L;

    private final IErrorCode errorCode;

    /**
     * 有参构造器-传入错误消息
     *
     * @param errorCode 错误消息
     */
    public BusinessRuntimeException(IErrorCode errorCode) {
        super(ErrorCodeHelper.get(errorCode.getCode()).toString());
        this.errorCode = errorCode;
    }

    /**
     * 有参构造器-传入堆栈异常信息
     *
     * @param errorCode code
     * @param cause     堆栈异常类
     */
    public BusinessRuntimeException(IErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    @Override
    public String getCode() {
        return this.errorCode.getCode();
    }
}
