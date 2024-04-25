package com.lovezhima.boot.core.constant;

import java.util.Locale;

/**
 * 全局基础常量
 *
 * @author king
 */
public interface Constants {

    /**
     * 默认租户ID
     */
    Long DEFAULT_TENANT_ID = 0L;

    /**
     * 默认匿名用户ID
     */
    Long ANONYMOUS_USER_ID = 0L;

    /**
     * 匿名用户名
     */
    String ANONYMOUS_USER_NAME = "ANONYMOUS";

    /**
     * 默认平台用户类型
     */
    String DEFAULT_USER_TYPE = "P";

    /**
     * 默认本地语言
     */
    Locale DEFAULT_LOCALE = Locale.CHINA;

    /**
     * 默认本地语言字符串
     */
    String DEFAULT_LOCALE_STR = Locale.CHINA.toString();

    /**
     * 默认字符编码
     */
    String DEFAULT_CHARSET = "UTF-8";

    /**
     * 默认手机区号
     */
    String DEFAULT_CROWN_CODE = "+86";

    /**
     * 默认时间时区
     */
    String DEFAULT_TIME_ZONE = "GMT+8";

    /**
     * 当前页
     */
    int PAGE = 0;

    /**
     * 当前页显示数量
     */
    int SIZE = 10;

    String PAGE_FIELD_NAME = "page";
    String SIZE_FIELD_NAME = "size";
    String FIELD_BODY = "body";
    String FIELD_CONTENT = "content";
    String FIELD_MSG = "message";
    String FIELD_FAILED = "failed";
    String FIELD_SUCCESS = "success";
    String FIELD_ERROR_MSG = "errorMsg";


    interface ResponseCode {

        /**
         * 成功code
         */
        String SUCCESS_CODE = "0";
    }
    /**
     * 符号常量
     */
    interface Symbol {

        /**
         * 感叹号
         */
        String SIGH = "!";

        /**
         * 艾特符号
         */
        String AT = "@";

        /**
         * 井号
         */
        String WELL = "#";

        /**
         * 美元符号
         */
        String DOLLAR = "$";

        /**
         * 大写RMB符号
         */
        String RMB = "￥";

        /**
         * 空字符
         */
        String EMPTY = "";

        /**
         * 空格符号
         */
        String SPACE = " ";

        /**
         * 分割符号
         */
        String LB = System.lineSeparator();

        /**
         * 百分号
         */
        String PERCENTAGE = "%";

        /**
         * 与号
         */
        String AND = "&";

        /**
         * 星号
         */
        String STAR = "*";

        /**
         * 中横杠符号
         */
        String MIDDLE_LINE = "-";

        /**
         * 下划线符号
         */
        String LOWER_LINE = "_";

        /**
         * 相等符号
         */
        String EQUAL = "=";

        /**
         * 加号
         */
        String PLUS = "+";

        /**
         * 冒号
         */
        String COLON = ":";

        /**
         * 分号
         */
        String SEMICOLON = ";";

        /**
         * 逗号
         */
        String COMMA = ",";

        /**
         * 点号
         */
        String POINT = ".";

        /**
         * 斜杠
         */
        String SLASH = "/";

        /**
         * 中竖线
         */
        String VERTICAL_BAR = "|";

        /**
         * 双斜杠
         */
        String DOUBLE_SLASH = "//";

        /**
         * 双反斜杠
         */
        String BACKSLASH = "\\";

        /**
         * 问号
         */
        String QUESTION = "?";

        /**
         * 左大括号
         */
        String LEFT_BIG_BRACE = "{";

        /**
         * 右大括号
         */
        String RIGHT_BIG_BRACE = "}";

        /**
         * 左中括号
         */
        String LEFT_MIDDLE_BRACE = "[";

        /**
         * 右中括号
         */
        String RIGHT_MIDDLE_BRACE = "]";

        /**
         * 反向引用
         */
        String BACKQUOTE = "`";

        /**
         * 左括号
         */
        String LEFT_BRACE = "(";

        /**
         * 右括号
         */
        String RIGHT_BRACE = ")";
    }

    interface HeaderParam {
        String REQUEST_HEADER_PARAM_PREFIX = "param-";
    }

    /**
     * 数字
     */
    interface Digital {

        /**
         * 数字-1
         */
        int NEGATIVE_ONE = -1;

        /**
         * 数字0
         */
        int ZERO = 0;

        /**
         * 数字1
         */
        int ONE = 1;

        /**
         * 数字2
         */
        int TWO = 2;

        /**
         * 数字4
         */
        int FOUR = 4;

        /**
         * 数字8
         */
        int EIGHT = 8;

        /**
         * 数字16
         */
        int SIXTEEN = 16;
    }

    interface FlagChar {
        String YES = "Y";
        String NO = "N";
        String X = "X";
    }

    /**
     * 标识符
     */
    interface Flag {

        /**
         * 数字1
         */
        Integer YES = 1;

        /**
         * 数字0
         */
        Integer NO = 0;
    }

    /**
     * 日期时间格式
     */
    interface DateTimeFormatPattern {

        /**
         * 日期类型 yyyy-MM-dd
         */
        String DATE = "yyyy-MM-dd";

        /**
         * 日期时间类型 yyyy-MM-dd HH:mm:ss
         */
        String DATETIME = "yyyy-MM-dd HH:mm:ss";

        /**
         * 日期时间（不含秒）类型 yyyy-MM-dd HH:mm
         */
        String DATETIME_MM = "yyyy-MM-dd HH:mm";

        /**
         * 日期时间（包含毫秒）类型 yyyy-MM-dd HH:mm:ss.SSS
         */
        String DATETIME_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

        /**
         * 时间 HH:mm
         */
        String TIME = "HH:mm";

        /**
         * 时间包含秒 HH:mm:ss
         */
        String TIME_SS = "HH:mm:ss";

        /**
         * 系统时间格式 yyyy/MM/dd
         */
        String SYS_DATE = "yyyy/MM/dd";

        /**
         * 系统时间格式 yyyy/MM/dd HH:mm:ss
         */
        String SYS_DATETIME = "yyyy/MM/dd HH:mm:ss";

        /**
         * 系统时间格式（不包含秒） yyyy/MM/dd HH:mm
         */
        String SYS_DATETIME_MM = "yyyy/MM/dd HH:mm";

        /**
         * 系统格式日期 （包含毫秒） yyyy/MM/dd HH:mm:ss.SSS
         */
        String SYS_DATETIME_SSS = "yyyy/MM/dd HH:mm:ss.SSS";

        /**
         * 无符号日期 yyyyMMdd
         */
        String NONE_DATE = "yyyyMMdd";

        /**
         * 无符号日期时间 yyyyMMddHHmmss
         */
        String NONE_DATETIME = "yyyyMMddHHmmss";

        /**
         * 无符号日期时间（不包含秒）yyyyMMddHHmm
         */
        String NONE_DATETIME_MM = "yyyyMMddHHmm";

        /**
         * 无符号日期时间（含毫秒）yyyyMMddHHmmssSSS
         */
        String NONE_DATETIME_SSS = "yyyyMMddHHmmssSSS";

        /**
         * CST 日期时间格式
         */
        String CST_DATETIME = "EEE MMM dd HH:mm:ss 'CST' yyyy";
    }

    interface AmountPattern {

        /**
         * 整数零
         */
        String NONE_DECIMAL = "0";

        /**
         * 零，包含一位小数点
         */
        String ONE_DECIMAL = "0.0";

        /**
         * 零，包含两位小数点
         */
        String TWO_DECIMAL = "0.00";

        /**
         * 尾数补0
         */
        String TB_NONE_DECIMAL = "#,##0";

        /**
         * 小数点1
         */
        String TB_ONE_DECIMAL = "#,##0.0";

        /**
         * 小数点2
         */
        String TB_TWO_DECIMAL = "#,##0.00";
    }
}
