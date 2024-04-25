package com.lovezhima.boot.web.response.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lovezhima.boot.core.constant.Constants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 异常追踪
 *
 * @author king on 2023/6/27
 * @since 2023.1
 */
@Slf4j
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionTraceEntity {

    /**
     * 异常消息
     */
    private String exception;

    /**
     * 异常追踪
     */
    private String[] trace;

    /**
     * 抛出的具体错误
     */
    private String[] throwable;

    /**
     * 错误发生时间
     */
    @JsonFormat(pattern = Constants.DateTimeFormatPattern.DATETIME_SSS)
    private Date datetime;

//    public static ExceptionEntity buildExceptionEntity(String errCode) {
//        ExceptionEntity exceptionEntity = new ExceptionEntity();
//        IErrorCode errorCode = ErrorCodeHelper.get(errCode);
//        exceptionEntity.setErrNo(errorCode.getErrNo());
//        exceptionEntity.setErrCode(errorCode.getErrCode());
//        exceptionEntity.setErrMsg(errorCode.getErrMsg());
//        return exceptionEntity;
//    }
//
//    public static ExceptionEntity toExceptionEntity(@NonNull List<FieldError> fieldErrors) {
//        FieldError fieldError = fieldErrors.iterator().next();
//        ExceptionEntity exceptionEntity = new ExceptionEntity();
//        IErrorCode errorCode = ErrorCodeHelper.get(fieldError.getDefaultMessage());
//        exceptionEntity.setErrNo(errorCode.getErrNo());
//        exceptionEntity.setErrCode(errorCode.getErrCode());
//        exceptionEntity.setErrMsg(MessageAccessor.getLocalMessage(fieldError.getDefaultMessage(),
//                fieldError.getArguments()).getDesc());
//        return exceptionEntity;
//    }
//
//    public static ExceptionEntity toExceptionEntity(Throwable ex) {
//        return buildExceptionEntity(ex.getMessage());
//    }
}
