package com.lovezhima.boot.core.context;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author king on 2024/2/28
 */
@Getter
public class RequestContext {

    private ServletRequest servletRequest;


    public static ServletRequestAttributes getRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    }

    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }
}
