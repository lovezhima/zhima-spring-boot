package com.lovezhima.boot.core.helper;

import com.lovezhima.boot.core.exception.IErrorCode;
import com.lovezhima.boot.core.message.MessageAccessor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 错误码帮助类，全局缓存了唯一code
 *
 * @author king on 2023/6/29
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorCodeHelper {

    private static final Map<String, IErrorCode> ERR_CODE_CACHE_MAP = new ConcurrentHashMap<>(256);

    /**
     * 获取错误信息
     *
     * @param code 编码
     * @return 错误信息
     */
    public static IErrorCode get(String code) {
        return get(code, IErrorCode.getDefault());
    }

    public static IErrorCode get(Exception ex, IErrorCode errorCode) {
        return Optional.ofNullable(ERR_CODE_CACHE_MAP.get(ex.getMessage()))
                .orElseGet(() -> new IErrorCode() {
                    @Serial
                    private static final long serialVersionUID = 1L;

                    @Override
                    public String getCode() {
                        return errorCode.getCode();
                    }
                    @Override
                    public String getMsg() {
                        return MessageAccessor.getLocalMessage(ex.getMessage()).getDesc();
                    }
                });
    }

    public static IErrorCode get(String code, IErrorCode defaultCode) {
        return Optional.ofNullable(ERR_CODE_CACHE_MAP.get(code)).orElse(defaultCode);
    }

    /**
     * 缓存一个错误信息
     *
     * @param errorCode 错误信息
     */
    public static void put(IErrorCode errorCode) {
        if (ERR_CODE_CACHE_MAP.get(errorCode.getCode()) != null) {
            throw new IllegalArgumentException("Exist [" + errorCode.getCode() + " ] has same error code");
        }
        ERR_CODE_CACHE_MAP.put(errorCode.getCode(), errorCode);
    }
}
