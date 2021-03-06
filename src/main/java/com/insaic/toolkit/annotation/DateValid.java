package com.insaic.toolkit.annotation;

import com.insaic.toolkit.annotation.validator.DateValidator;
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
@Constraint(validatedBy = DateValidator.class)
@Documented
public @interface DateValid {

    String message() default "日期格式必须为{format}！";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default{};

    boolean isNull() default true;

    //日期格式
    String format() default ToolkitConstants.DATE_YYYY_H24_MM_SS;
}
