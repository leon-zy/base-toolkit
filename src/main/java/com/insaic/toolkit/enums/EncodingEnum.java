package com.insaic.toolkit.enums;

/**
 * EncodingEnum
 * Created by leon_zy on 2018/12/25
 */
public enum EncodingEnum {

    UTF_8("UTF-8", "UTF-8"),
    GBK("GBK", "GBK");

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