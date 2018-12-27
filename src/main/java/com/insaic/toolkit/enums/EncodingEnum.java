package com.insaic.toolkit.enums;

/**
 * EncodingEnum 字符编码枚举类
 */
public enum EncodingEnum {

    UTF_8("UTF-8", "UTF-8"),
    GBK("GBK", "GBK"),
    ISO8859_1("ISO8859-1", "ISO8859-1"),
    GB2312("GB2312", "GB2312");

    private String code;
    private String message;

    EncodingEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}