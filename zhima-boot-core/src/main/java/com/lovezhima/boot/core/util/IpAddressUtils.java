package com.lovezhima.boot.core.util;

import com.lovezhima.boot.core.context.RequestContext;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.Optional;

public class IpAddressUtils {
    private static final String UN_KNOWN = "unKnown";
    private static final String SEPARATOR = ",";

    public IpAddressUtils() {
    }


    public static String getRealIp(Object request) {
        if (request instanceof HttpServletRequest) {
            return IpAddressUtils.getIpAddress((HttpServletRequest) request);
        } else if (request instanceof ServerHttpRequest) {
            return IpAddressUtils.getIpAddress((ServerHttpRequest) request);
        } else {
            throw new IllegalArgumentException("Request type must be HttpServletRequest or ServerHttpRequest.");
        }
    }

    public static String getRealIp() {
        HttpServletRequest request = ((ServletRequestAttributes)
                Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        return getRealIp(request);
    }

    public static String getRealIpAddress(RequestContext requestContext) {
        return getRealIpAddress(requestContext.getServletRequest());
    }


    public static String getRealIpAddress(ServletRequest servletRequest) {
        return getRealIp(servletRequest);
    }

    @Nullable
    public static String getIpAddress(@Nullable IpRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        String realIp = request.getHeaderXxForwardedFor();
        if (isValidAddress(realIp)) {
            assert realIp != null;
            int index = realIp.indexOf(",");
            return index != -1 ? realIp.substring(0, index) : realIp;
        }
        realIp = request.getHeaderXxRealIp();
        if (isValidAddress(realIp)) {
            return realIp;
        }
        realIp = request.getHeaderProxyClientIp();
        if (isValidAddress(realIp)) {
            return realIp;
        }
        realIp = request.getHeaderWlProxyClientIp();
        if (isValidAddress(realIp)) {
            return realIp;
        }
        realIp = request.getHeaderHttpClientIp();
        if (isValidAddress(realIp)) {
            return realIp;
        }
        realIp = request.getHeaderHttpXxForwardedFor();
        if (isValidAddress(realIp)) {
            return realIp;
        }
        realIp = request.getRemoteAddress();
        return isValidAddress(realIp) ? realIp : null;
    }

    private static boolean isValidAddress(@Nullable String ip) {
        return StringUtils.isNotBlank(ip) && !"unKnown".equalsIgnoreCase(ip);
    }

    public static String getIpAddress(HttpServletRequest request) {
        return getIpAddress(HttpServletIpRequest.of(request));
    }

    public static String getIpAddress(ServerHttpRequest request) {
        return getIpAddress(ServerHttpIpRequest.of(request));
    }

    public static class ServerHttpIpRequest implements IpRequest {
        private final ServerHttpRequest request;
        private final HttpHeaders headers;

        private ServerHttpIpRequest(@NonNull ServerHttpRequest request) {
            this.request = request;
            this.headers = request.getHeaders();
        }

        @Nullable
        public static ServerHttpIpRequest of(@Nullable ServerHttpRequest request) {
            return Objects.isNull(request) ? null : new ServerHttpIpRequest(request);
        }

        @Nullable
        public String getHeaderXxForwardedFor() {
            return this.headers.getFirst("X-Forwarded-For");
        }

        @Nullable
        public String getHeaderXxRealIp() {
            return this.headers.getFirst("X-Real-IP");
        }

        @Nullable
        public String getHeaderProxyClientIp() {
            return this.headers.getFirst("Proxy-Client-IP");
        }

        @Nullable
        public String getHeaderWlProxyClientIp() {
            return this.headers.getFirst("WL-Proxy-Client-IP");
        }

        @Nullable
        public String getHeaderHttpClientIp() {
            return this.headers.getFirst("HTTP_CLIENT_IP");
        }

        @Nullable
        public String getHeaderHttpXxForwardedFor() {
            return this.headers.getFirst("HTTP_X_FORWARDED_FOR");
        }

        @Nullable
        public String getRemoteAddress() {
            return Optional.ofNullable(this.request.getRemoteAddress()).map(InetSocketAddress::getAddress).map(InetAddress::getHostAddress).orElse(null);
        }
    }

    public static class HttpServletIpRequest implements IpRequest {
        private final HttpServletRequest request;

        private HttpServletIpRequest(@NonNull HttpServletRequest request) {
            this.request = request;
        }

        @Nullable
        public static HttpServletIpRequest of(@Nullable HttpServletRequest request) {
            return Objects.isNull(request) ? null : new HttpServletIpRequest(request);
        }

        @Nullable
        public String getHeaderXxForwardedFor() {
            return this.request.getHeader("X-Forwarded-For");
        }

        @Nullable
        public String getHeaderXxRealIp() {
            return this.request.getHeader("X-Real-IP");
        }

        @Nullable
        public String getHeaderProxyClientIp() {
            return this.request.getHeader("Proxy-Client-IP");
        }

        @Nullable
        public String getHeaderWlProxyClientIp() {
            return this.request.getHeader("WL-Proxy-Client-IP");
        }

        @Nullable
        public String getHeaderHttpClientIp() {
            return this.request.getHeader("HTTP_CLIENT_IP");
        }

        @Nullable
        public String getHeaderHttpXxForwardedFor() {
            return this.request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        @Nullable
        public String getRemoteAddress() {
            return this.request.getRemoteAddr();
        }
    }

    public interface IpRequest {
        String HEADER_X_FORWARDED_FOR = "X-Forwarded-For";
        String HEADER_X_REAL_IP = "X-Real-IP";
        String HEADER_PROXY_CLIENT_IP = "Proxy-Client-IP";
        String HEADER_WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
        String HEADER_HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
        String HEADER_HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";

        @Nullable
        default String getHeaderXxForwardedFor() {
            return null;
        }

        @Nullable
        default String getHeaderXxRealIp() {
            return null;
        }

        @Nullable
        default String getHeaderProxyClientIp() {
            return null;
        }

        @Nullable
        default String getHeaderWlProxyClientIp() {
            return null;
        }

        @Nullable
        default String getHeaderHttpClientIp() {
            return null;
        }

        @Nullable
        default String getHeaderHttpXxForwardedFor() {
            return null;
        }

        @Nullable
        default String getRemoteAddress() {
            return null;
        }
    }
}
