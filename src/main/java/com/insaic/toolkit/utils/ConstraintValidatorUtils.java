package com.insaic.toolkit.utils;

import com.alibaba.fastjson.annotation.JSONField;
import com.insaic.base.exception.ExceptionUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * ConstraintValidatorUtils
 * Created by leon_zy on 2018/10/9
 */
public class ConstraintValidatorUtils {

    private static Validator validator;
    static{
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static <T> Map<String, String> validate(T obj) {
        Map<String, String> errorMap = null;
        Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
        if(set != null && set.size() > 0 ){
            errorMap = new HashMap<>();
            String property;
            for(ConstraintViolation<T> cv : set){
                //这里循环获取错误信息，可以自定义格式
                property = cv.getPropertyPath().toString();
                if(null == errorMap.get(property)){
                    //获取接口属性注解上定义的属性名称
                    try {
                        Field field = obj.getClass().getDeclaredField(property);
                        if(null != field){
                            if(field.isAnnotationPresent(JSONField.class)){
                                JSONField jsonField = field.getAnnotation(JSONField.class);
                                property = jsonField.name();
                            }
                        }
                    } catch (Exception e) {
                        ExceptionUtil.handleException(e, "获取接口属性注解JSONField定义的属性名称异常：class:" + obj.getClass() +", name为:" + property);
                    }
                    errorMap.put(property, cv.getMessage());
                }
            }
        }
        return errorMap;
    }
}