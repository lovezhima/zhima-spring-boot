package com.lovezhima.boot.core.message;

import com.lovezhima.boot.core.constant.Constants;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author king on 2023/6/26
 * @since 1.0
 */
@Data
public class Message {
    public static final Type DEFAULT_TYPE;
    private String code;
    private String desc;
    private String type;

    public Message() {
    }

    public Message(String code, String desc) {
        this.code = code;
        this.desc = desc;
        String[] arr = StringUtils.split(code, Constants.Symbol.POINT);
        if (arr != null && arr.length > Constants.Digital.TWO) {
            Type t = Message.Type.match(arr[Constants.Digital.ONE]);
            if (t != null) {
                this.type = t.code;
            }
        }
        if (StringUtils.isBlank(this.type)) {
            this.type = DEFAULT_TYPE.getCode();
        }
    }

    public Message(String code, String desc, Type type) {
        this.code = code;
        this.desc = desc;
        this.type = type.getCode();
    }

    public Message setCode(String code) {
        this.code = code;
        return this;
    }

    public Message setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public Message setType(String type) {
        this.type = type;
        return this;
    }


    static {
        DEFAULT_TYPE = Message.Type.WARN;
    }

    /**
     * 异常类型
     *
     * @author king on 2023/6/26
     */
    @Getter
    public static enum Type {

        /**
         * INFO类型
         */
        INFO("info"),

        /**
         * 警告类型
         */
        WARN("warn"),

        /**
         * 错误类型
         */
        ERROR("error");

        private final String code;

        static final Map<String, Type> MAP = new HashMap<>();

        Type(String code) {
            this.code = code;
        }

        public static Type match(String code) {
            return MAP.get(StringUtils.lowerCase(code));
        }

        static {
            for (Type value : values()) {
                MAP.put(value.code, value);
            }
        }
    }
}
