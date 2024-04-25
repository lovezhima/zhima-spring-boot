package com.lovezhima.boot.web.response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lovezhima.boot.core.constant.Constants;
import com.lovezhima.boot.core.constant.enums.CommonErrorCodeEnum;
import com.lovezhima.boot.core.context.SpringApplicationContextHolder;
import com.lovezhima.boot.core.exception.CommonRuntimeException;
import com.lovezhima.boot.web.response.entity.ExceptionResponse;
import com.lovezhima.boot.web.response.entity.Result;
import com.lovezhima.boot.web.response.exception.RpcFailException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * http响应接口返回 适配http response类型
 *
 * <p>
 * 第一种，Restful风格，
 * 正常情况响应 200  {"username": "lisi"}  或者NO_CONTENT
 * 异常情况响应 400  {"msg": "客户端参数不对"}
 *
 * <p>
 * 第二种，自定义风格
 * 正常情况响应 200  {"code": "0", "msg": "操作成功", "data":[]}
 * 异常情况响应 400  {"code": "-1","msg": "处理异常"}
 *
 * @author king on 2024/3/27
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseUtils {
    private static volatile ObjectMapper objectMapper;

    /**
     * 常用对象序列化
     */
    private static final List<String> SIMPLE_CLASS = Arrays.asList(
            Byte.class.getName(), Short.class.getName(), Integer.class.getName(), Long.class.getName(),
            Float.class.getName(), Double.class.getName(),
            Boolean.class.getName(),
            String.class.getName(),
            Date.class.getName(), java.sql.Date.class.getName(), LocalDate.class.getName(),
            BigDecimal.class.getName());

    /**
     * 非200默认异常响应
     */
    public static final Not2xxSuccessful DEFAULT_NOT_2XX_SUCCESSFUL = (httpStatus, response) -> {
        throw new RpcFailException();
    };

    /**
     * 失败默认异常响应
     */
    public static final FailedResponse DEFAULT_FAILED_RESPONSE = (exceptionResponse) -> {
        throw new RpcFailException();
    };

    /**
     * 获取Jackson实例
     *
     * @return ObjectMapper
     */
    private static ObjectMapper getObjectMapper() {
        if (objectMapper != null) {
            return objectMapper;
        }
        synchronized (ResponseUtils.class) {
            objectMapper = SpringApplicationContextHolder.getContext().getBean(ObjectMapper.class);
            if (objectMapper == null) {
                objectMapper = new ObjectMapper();
            }
        }
        return objectMapper;
    }

    public static <T> T getResponse(ResponseEntity<String> response, TypeReference<T> responseType) {
        return getResponse(response, responseType, DEFAULT_NOT_2XX_SUCCESSFUL, DEFAULT_FAILED_RESPONSE);
    }

    /**
     * 获取反序列化后响应内容
     *
     * @param response         响应内容
     * @param responseType     反序列化类型，<strong>不支持简单类型</strong>
     * @param not2xxSuccessful 非2xx错误处理
     * @param failedResponse   200，但是failed=true处理
     * @param <T>              反序列化类型泛型
     * @return 反序列化后的响应内容
     */
    @Nullable
    public static <T> T getResponse(ResponseEntity<String> response,
                                    TypeReference<T> responseType,
                                    Not2xxSuccessful not2xxSuccessful,
                                    FailedResponse failedResponse) {
        if (failedOrNoContent(response, not2xxSuccessful)) {
            return null;
        }
        String content = response.getBody();
        if (StringUtils.isBlank(content)) {
            return null;
        }
        try {
            Triple<Boolean, Boolean, JsonNode> triple = successContent(content);
            JsonNode jsonNode = triple.getRight();
            if (!triple.getLeft()) {
                failedResponse.failedResponse(getObjectMapper().readValue(jsonNode.traverse(), ExceptionResponse.class));
                return null;
            }
            return isSimpleClass(responseType.getType().getTypeName()) ?
                    getSimpleValue(responseType.getType().getTypeName(), getJsonNodeContent(jsonNode, triple.getMiddle())) :
                    getObjectMapper().readValue(getJsonNodeContent(jsonNode, triple.getMiddle()), responseType);
        } catch (IOException e) {
            if (isSimpleClass(responseType.getType().getTypeName())) {
                return getSimpleValue(responseType.getType().getTypeName(), content);
            }
            throw new CommonRuntimeException(e);
        }
    }

    public static <T> T getResponse(ResponseEntity<String> response, Class<T> responseType) {
        return getResponse(response, responseType, DEFAULT_NOT_2XX_SUCCESSFUL, DEFAULT_FAILED_RESPONSE);
    }


    /**
     * 获取反序列化后响应内容
     *
     * @param response         响应内容
     * @param responseType     反序列化类型，支持简单类型
     * @param not2xxSuccessful 非2xx错误处理
     * @param failedResponse   返回200，但是code!=0的情况
     * @param <T>              反序列化类型泛型
     * @return 反序列化后的响应内容
     */
    public static <T> T getResponse(ResponseEntity<String> response,
                                    Class<T> responseType,
                                    Not2xxSuccessful not2xxSuccessful,
                                    FailedResponse failedResponse) {
        if (failedOrNoContent(response, not2xxSuccessful)) {
            return null;
        }
        String content = response.getBody();
        if (StringUtils.isBlank(content)) {
            return null;
        }
        try {
            Triple<Boolean, Boolean, JsonNode> triple = successContent(content);
            JsonNode jsonNode = triple.getRight();
            if (!triple.getLeft()) {
                failedResponse.failedResponse(getObjectMapper().readValue(jsonNode.traverse(), ExceptionResponse.class));
                return null;
            }
            return isSimpleClass(responseType) ?
                    getSimpleValue(responseType.getTypeName(), getJsonNodeContent(jsonNode, triple.getMiddle())) :
                    getObjectMapper().readValue(getJsonNodeContent(jsonNode, triple.getMiddle()), responseType);
        } catch (IOException e) {
            if (isSimpleClass(responseType)) {
                return getSimpleValue(responseType.getTypeName(), content);
            }
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 请求是否失败
     *
     * @param response 响应内容
     * @return 是否失败
     */
    public static boolean isFailed(ResponseEntity<String> response) {
        if (failed(response, null)) {
            return true;
        }
        if (noContent(response)) {
            return false;
        }
        return successContent(response.getBody()).getLeft();
    }


    private static String getJsonNodeContent(JsonNode jsonNode, boolean haveData) {
        JsonNode data = jsonNode;
        if (haveData) {
            data = jsonNode.get(Result.FILED_DATA);
            if (data == null) {
                return null;
            }
        }
        return data.toString();
    }

    private static boolean isSimpleClass(Class<?> responseType) {
        return SIMPLE_CLASS.contains(responseType.getTypeName());
    }

    private static boolean isSimpleClass(String responseType) {
        return SIMPLE_CLASS.contains(responseType);
    }

    @SuppressWarnings("unchecked")
    private static <T> T getSimpleValue(String responseType, String value) {
        if (Byte.class.getName().equals(responseType)) {
            return (T) Byte.valueOf(value);
        }
        if (Short.class.getName().equals(responseType)) {
            return (T) Short.valueOf(value);
        }
        if (Integer.class.getName().equals(responseType)) {
            return (T) Integer.valueOf(value);
        }
        if (Long.class.getName().equals(responseType)) {
            return (T) Long.valueOf(value);
        }
        if (Float.class.getName().equals(responseType)) {
            return (T) Float.valueOf(value);
        }
        if (Double.class.getName().equals(responseType)) {
            return (T) Double.valueOf(value);
        }
        if (Boolean.class.getName().equals(responseType)) {
            return (T) Boolean.valueOf(value);
        }
        if (String.class.getName().equals(responseType)) {
            return (T) value;
        }
        if (Date.class.getName().equals(responseType)) {
            try {
                return (T) DateUtils.parseDate(value, Constants.DateTimeFormatPattern.DATETIME);
            } catch (ParseException e) {
                throw new IllegalArgumentException(e);
            }
        }
        if (java.sql.Date.class.getName().equals(responseType)) {
            return (T) java.sql.Date.valueOf(value);
        }
        if (BigDecimal.class.getName().equals(responseType)) {
            return (T) new BigDecimal(value);
        }
        if (LocalDate.class.getName().equals(responseType)) {
            return (T) LocalDate.parse(value);
        }

        throw new IllegalArgumentException("Not supported responseType [" + responseType + "]");
    }

    private static boolean failed(ResponseEntity<String> response, @Nullable Not2xxSuccessful not2xxSuccessful) {
        Assert.notNull(response, "Http response must be not null.");
        if (response.getStatusCode().is2xxSuccessful()) {
            return false;
        }
        if (not2xxSuccessful != null) {
            not2xxSuccessful.not2xxSuccessful((HttpStatus) response.getStatusCode(), response.getBody());
        }
        return true;
    }

    private static boolean noContent(ResponseEntity<String> response) {
        return HttpStatus.NO_CONTENT.equals(response.getStatusCode());
    }

    private static boolean failedOrNoContent(ResponseEntity<String> response, Not2xxSuccessful not2xxSuccessful) {
        if (failed(response, not2xxSuccessful)) {
            return true;
        }
        return noContent(response);
    }

    /**
     * @param content body
     * @return Triple<请求是否成功 ， 是否为自定义 ， 结果值>
     */
    private static Triple<Boolean, Boolean, JsonNode> successContent(String content) {
        try {
            JsonNode jsonNode = getObjectMapper().readTree(content);
            // 1.判断是否自定义body {"code":"0", "msg":"处理成功", "data":[]}
            if (jsonNode.isObject() && jsonNode.has(Result.FILED_CODE) && jsonNode.has(Result.FILED_MSG)) {
                boolean trueOrFalse = jsonNode.get(Result.FILED_CODE).asText().equals(CommonErrorCodeEnum.SUCCESS.getCode());
                return Triple.of(trueOrFalse, true, jsonNode);
            }
            // 2. 判断是否对象返回 例如{1}, {"username": "lisi"}
            return Triple.of(true, false, jsonNode);
        } catch (IOException e) {
            throw new CommonRuntimeException("Json deserialization failed", e);
        }
    }

    /**
     * 非200响应处理
     */
    @FunctionalInterface
    public interface Not2xxSuccessful {
        /**
         * 非2xx结果
         *
         * @param httpStatus 请求状态嘛
         * @param response   响应内容
         */
        void not2xxSuccessful(HttpStatus httpStatus, String response);
    }

    @FunctionalInterface
    public interface FailedResponse {
        /**
         * 2xx但是失败了
         *
         * @param exceptionResponse 失败回调
         */
        void failedResponse(ExceptionResponse exceptionResponse);
    }
}
