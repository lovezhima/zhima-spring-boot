package com.lovezhima.boot.web.response.entity;

import com.lovezhima.boot.core.exception.IErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
  * @author king on 2023/6/27
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExceptionResult<T> extends BasicResult<T> implements Result<T> {

    public ExceptionResult(IErrorCode codeEnum, T data) {
        this.code = codeEnum.getCode();
        this.msg = codeEnum.getMsg();
        this.data = data;
    }
}
