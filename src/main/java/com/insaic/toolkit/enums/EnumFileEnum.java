package com.insaic.toolkit.enums;

/**
 * EnumFileEnum 枚举类定义的属性名称枚举类
 * Created by leon_zy on 2018/8/2
 */
public enum EnumFileEnum {
    name("枚举类定义的属性名称name，传递参数请使用：EnumFileEnum.name"),
    Code("枚举类定义的属性名称code，传递参数请使用：EnumFileEnum.Code"),
    Value("枚举类定义的属性名称value，传递参数请使用：EnumFileEnum.Value"),
    Message("枚举类定义的属性名称message，传递参数请使用：EnumFileEnum.Message");

    private String message;

    public String getMessage() {
        return message;
    }

    EnumFileEnum(String message) {
        this.message = message;
    }
}