package com.insaic.toolkit.utils;

import com.insaic.base.exception.BusinessException;
import com.insaic.base.utils.Collections3;
import com.insaic.base.utils.Reflections;
import com.insaic.toolkit.constants.ToolkitConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ToolkitUtils 数据查询赋值工具类
 * Created by leon_yan on 2018/5/30
 */
public final class QueryDataResultUtils {
    private static final Logger logger = LoggerFactory.getLogger(QueryDataResultUtils.class);

    //中文字符、英文字母和中英特殊符号
    private static final Map<Class, Map<String, Field>> classCatchMap = new HashMap<>();

    /**
     * 查询数据库的值转换到对应类的对应属性名上
     * @param dataMaps 查询结果
     * @param clazz 赋值的类
     * @param <T> 泛型
     * @return list
     */
    public static <T> List<T> getDataResultList(List<Map<String, Object>> dataMaps, Class<T> clazz) {
        List<T> results = new ArrayList<>();
        if(null != clazz && Collections3.isNotEmpty(dataMaps)){
            for(Map<String, Object> dataMap : dataMaps){
                if(!dataMap.isEmpty()) {
                    results.add(getDataResult(dataMap, clazz));
                }
            }
        }
        return results;
    }

    /**
     * 转换属性值到对应类的对应属性名上
     * @param dataMap 属性值
     * @param clazz 赋值的类
     * @param <T> 泛型
     * @return T
     */
    public static <T> T getDataResult(Map<String, Object> dataMap, Class<T> clazz) {
        T obj = null;
        try{
            if(null != clazz && null != dataMap && !dataMap.isEmpty()){
                obj = clazz.newInstance();
                Map<String, Field> fieldCatchMap = null == classCatchMap.get(clazz) ? new HashMap<>() : classCatchMap.get(clazz);
                classCatchMap.putIfAbsent(clazz, fieldCatchMap);
                for(String key : dataMap.keySet()){
                    Field f = fieldCatchMap.get(key);
                    //取缓存Field
                    if(null != f){
                        f.setAccessible(true);
                        f.set(obj, ToolkitUtils.replaceObjType(f.getGenericType().toString(), dataMap.get(key)));
                    }else{
                        //若没有缓存则取class中的属性，并放到缓存中
                        for (Field field : Reflections.getAllFields(clazz)) {
                            if(key.equals(ToolkitUtils.getFileAnnotationNameByClass(field, Column.class))
                                    || key.toLowerCase().equals(field.getName().toLowerCase())
                                    || key.replace(ToolkitConstants.UNDERLINE_STR, ToolkitConstants.EMPTY_STR).toLowerCase().equals(field.getName().replace(ToolkitConstants.UNDERLINE_STR, ToolkitConstants.EMPTY_STR).toLowerCase())){
                                field.setAccessible(true);
                                field.set(obj, ToolkitUtils.replaceObjType(field.getGenericType().toString(), dataMap.get(key)));
                                fieldCatchMap.put(key, field);
                                break;
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error("getDataResult出错：" + e.getMessage(), e);
            throw new BusinessException("查询异常，请联系管理员。");
        }
        return obj;
    }


}