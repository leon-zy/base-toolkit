package com.insaic.toolkit.enums;

/**
 * 文件类型枚举值
 * Created by FansenSen on 2018/10/25 0025.
 */
public enum FileTypeEnum {
    jpg("jpg", "图片格式"),
    jpeg("jpeg", "图片格式"),
    arm("arm", "录音格式");

    FileTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
