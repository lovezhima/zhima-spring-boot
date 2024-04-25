package com.lovezhima.boot.core.exception;

import com.lovezhima.boot.core.constant.enums.CommonErrorCodeEnum;
import com.lovezhima.boot.core.helper.ErrorCodeHelper;
import com.lovezhima.boot.core.message.MessageAccessor;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * 错误码
 *
 * @author king
 * @since 2023.1
 */
public interface IErrorCode extends java.io.Serializable {

    /**
     * 获取编码
     *
     * @return String
     */
    String getCode();

    /**
     * 获取信息 ，依据i18n显示
     *
     * @return String
     */
    default String getMsg() {
        return Optional.ofNullable(MessageAccessor.getLocalMessage(this.getCode()).getDesc())
                .filter(code -> StringUtils.equals(this.getCode(), code)).orElse(this.getDefaultMsg());
    }

    /**
     * 默认显示消息
     *
     * @return defaultMsg
     */
    default String getDefaultMsg() {
        return this.getMsg();
    }

    /**
     * 返回默认错误
     *
     * @return IErrorCode
     */
    static IErrorCode getDefault() {
        return CommonErrorCodeEnum.BUSINESS_FAIL;
    }

    /**
     * 装配数据，并检查数据是否重复, 需要再初始化调用load方法
     */
    default void load() {
        if (StringUtils.isNotBlank(this.getCode()) && !(this instanceof BusinessRuntimeException)) {
            ErrorCodeHelper.put(this);
        }
    }
}
