package com.lovezhima.boot.web.response.entity;

import com.lovezhima.boot.core.constant.enums.CommonErrorCodeEnum;
import lombok.Data;

/**
 * //TODO 类描述信息与实现思路
 *
 * @author king on 2023/6/27
 * @since 1.0
 */
@Data
public class BasicResult<T> implements Result<T> {

    /**
     * code
     */
    protected String code;

    /**
     * msg
     */
    protected String msg;

    /**
     * 数据
     */
    protected T data;

    public BasicResult() {
        this(null);
    }

    public BasicResult(T data) {
        this.code = CommonErrorCodeEnum.SUCCESS.getCode();
        this.msg = CommonErrorCodeEnum.SUCCESS.getMsg();
        this.data = data;
    }
}
