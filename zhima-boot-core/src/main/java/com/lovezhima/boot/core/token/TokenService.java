package com.lovezhima.boot.core.token;

import jakarta.servlet.http.HttpServletRequest;

/**
 * token服务接口
 *
 * @author king
 * @since 2023.1
 */
public interface TokenService {

    /**
     * 创建token
     * @return token
     */
    String createToken();

    /**
     * 获取token值
     * @param token token值
     * @return token value
     */
    Object getToken(String token);

    /**
     * 校验token
     * @param request 请求
     */
    void checkToken(HttpServletRequest request);


    /**
     * 删除token
     * @param token token
     * @return boolean
     */
    boolean deleteToken(String token);
}
