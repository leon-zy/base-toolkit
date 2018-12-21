package com.insaic.toolkit.annotation;


import com.insaic.toolkit.annotation.validator.StringValidator;
import com.insaic.toolkit.constants.ToolkitConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字符串校验
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StringValidator.class)
@Documented
public @interface StringValid {

    String message() default "字段值不能为空或长度必须小于等于{length}(中文UTF-8编码字符长度必须小于等于{length}/3)！";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default{};

    boolean isNull() default true;

    int length() default 0;
    //秘密方式
    String secretWay() default ToolkitConstants.EMPTY_STR;
    //秘密类型
    String secretType() default ToolkitConstants.EMPTY_STR;
    //钥匙类型
    String keyType() default ToolkitConstants.EMPTY_STR;
}
