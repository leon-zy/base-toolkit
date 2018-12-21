package com.insaic.toolkit.utils;

import com.insaic.base.utils.Reflections;

import java.lang.reflect.Field;

/**
 * ObjectUtils
 * Created by leon_yan on 2018/4/16
 */
public class ObjectUtils {

    /**
     * 校验对象中属性的值是否全部为空
     * @param obj 校验的类
     * @return boolean
     * @throws IllegalAccessException
     */
    public static Boolean objAllFiledNullFlag(Object obj) throws IllegalAccessException {
        Boolean valFlag = false;
        //校验属性值是否全部为空，serialVersionUID不校验值
        for(Field f : Reflections.getAllFields(obj.getClass())){
            f.setAccessible(true);
            if(!"serialVersionUID".equals(f.getName())
                    && !(f.get(obj) == null || "".equals(f.get(obj)))){
                valFlag = true;
                break;
            }
        }
        return valFlag;
    }
}