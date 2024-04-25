package com.lovezhima.boot.core.util.secrect;

import com.lovezhima.boot.core.exception.CommonRuntimeException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5 加密算法
 *
 * @author binbin.hou
 * @since 0.1.5
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Md5Utils {

    /**
     * 获取字符串的 md5 值
     *
     * @param string 字符串
     * @return md5
     */
    public static String md5(final String string) {
        try {
            if (StringUtils.isEmpty(string)) {
                return string;
            }
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] output = messageDigest.digest(string.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : output) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new CommonRuntimeException(e);
        }
    }
}