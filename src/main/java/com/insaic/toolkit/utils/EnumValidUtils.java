package com.insaic.toolkit.utils;

import com.insaic.base.utils.StringUtil;
import com.insaic.toolkit.constants.ToolkitConstants;
import com.insaic.toolkit.enums.EnumFileEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * EnumValidUtils 枚举值属性校验工具类
 * Created by leon_zy on 2018/8/1
 */
public final class EnumValidUtils {
    private static final Logger logger = LoggerFactory.getLogger(EnumValidUtils.class);

    /**
     * 获取枚举类中定义的属性是否有value值
     * @param enumClass 枚举类
     * @param enumFile 属性名称枚举
     * @param value 校验的值
     * @param <T> 泛型
     * @return enum
     */
    public static <T extends Enum<T>> T enumFileOf(Class<T> enumClass, EnumFileEnum enumFile, Object value) {
        T en = null;
        try {
            T[] arr = null == enumClass ? null : enumClass.getEnumConstants();
            if (null != arr && arr.length > 0 && null != enumFile){
                Method m = enumClass.getMethod((EnumFileEnum.name.equals(enumFile) ? ToolkitConstants.EMPTY_STR : ToolkitConstants.GET_STR) + enumFile.name());
                for (T e : arr) {
                    if (StringUtil.toString(value).equals(StringUtil.toString(m.invoke(e)))){
                        en = e;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return en;
    }

    /**
     * 获取枚举类的指定属性的map
     * @param enumClass 枚举类
     * @param key 枚举键
     * @param value 枚举值
     * @param <T> 泛型
     * @return map
     */
    public static <T extends Enum<T>> Map<String, Object> getEnumCodeMap(Class<T> enumClass, EnumFileEnum key, EnumFileEnum value) {
        Map<String, Object> map = new HashMap<>();
        try {
            T[] arr = null == enumClass ? null : enumClass.getEnumConstants();
            if (null != arr && arr.length > 0) {
                Method codeM = enumClass.getMethod((EnumFileEnum.name.equals(key) ? ToolkitConstants.EMPTY_STR : ToolkitConstants.GET_STR) + key.name());
                Method msgM = enumClass.getMethod((EnumFileEnum.name.equals(value) ? ToolkitConstants.EMPTY_STR : ToolkitConstants.GET_STR) + value.name());
                for (T e : arr) {
                    map.put(StringUtil.toString(codeM.invoke(e)), msgM.invoke(e));
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return map;
    }

    /**
     * 根据枚举键值组装sql
     * @param sqlStr 查询语句
     * @param enumMap 枚举键值map
     */
    public static void handlerEnumMapFileSqlStr(StringBuilder sqlStr, Map<String, Object> enumMap){
        if(!enumMap.isEmpty()){
            String firstFlag = ToolkitConstants.YES;
            for(String key : enumMap.keySet()){
                if(ToolkitConstants.NO.equals(firstFlag)){
                    sqlStr.append(ToolkitConstants.COMMA_EN);
                }
                sqlStr.append(ToolkitConstants.QUOTES_EN).append(key).append(ToolkitConstants.QUOTES_EN).append(ToolkitConstants.COMMA_EN);
                sqlStr.append(ToolkitConstants.QUOTES_EN).append(enumMap.get(key)).append(ToolkitConstants.QUOTES_EN);
                firstFlag = ToolkitConstants.NO;
            }
        }
    }

}