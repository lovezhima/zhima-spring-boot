package com.lovezhima.boot.web.response.advice;

import com.lovezhima.boot.core.constant.enums.CommonErrorCodeEnum;
import com.lovezhima.boot.core.exception.BusinessRuntimeException;
import com.lovezhima.boot.core.exception.CommonRuntimeException;
import com.lovezhima.boot.core.helper.ErrorCodeHelper;
import com.lovezhima.boot.web.response.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 通用全局异常处理
 *
 * @author king
 * @since 2023.1
 */
@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.OK)
public class GlobalExceptionHandlerAdvice {

    /**
     * 使用json提交参数后验证错误
     *
     * @param ex MethodArgumentNotValidException异常
     * @return ResponseEntity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException: ", ex);
        List<String> msg = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return Result.fail(CommonErrorCodeEnum.ERR_UN_PROCESSABLE_ENTITY, msg);
    }

    /**
     * 使用表单提交参数校验错误
     *
     * @param ex BindException 异常
     * @return ResponseEntity
     */
    @ExceptionHandler(BindException.class)
    public Result<?> bindExceptionHandler(BindException ex) {
        log.error("BindException: ", ex);
        List<String> msg = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return Result.fail(ErrorCodeHelper.get(ex, CommonErrorCodeEnum.ERR_UN_PROCESSABLE_ENTITY), msg);
    }

    /**
     * i18n message NoSuchMessageException
     *
     * @param ex NoSuchMessageException
     * @return ResponseEntity
     */
    @ExceptionHandler(value = NoSuchMessageException.class)
    public Result<?> noSuchMessageExceptionHandler(NoSuchMessageException ex) {
        log.error("NoSuchMessageException: ", ex);
        return Result.fail(CommonErrorCodeEnum.ERR_INTERNAL_SERVER_ERROR);
    }

    /**
     * 自定义异常超类
     *
     * @param ex GenericRuntimeException
     * @return ResponseEntity
     */
    @ExceptionHandler(BusinessRuntimeException.class)
    public Result<?> handleBusinessRuntimeException(BusinessRuntimeException ex) {
        log.error("BusinessRuntimeException:", ex);
        return Result.fail(ErrorCodeHelper.get(ex.getCode()));
    }

    @ExceptionHandler({CommonRuntimeException.class, RuntimeException.class})
    public Result<?> handleRuntimeException(RuntimeException ex) {
        log.error("RuntimeException: ", ex);
        return Result.fail(CommonErrorCodeEnum.COMMON_FAIL);
    }

    /**
     * Exception 类型的控制器，最终兜底处理
     *
     * @param ex Throwable
     * @return ResponseEntity
     */
    @ExceptionHandler({Exception.class, Throwable.class})
    public Result<?> handleException(Throwable ex) {
        log.error("Exception: ", ex);
        return Result.fail(CommonErrorCodeEnum.ERR_INTERNAL_SERVER_ERROR);
    }
}
