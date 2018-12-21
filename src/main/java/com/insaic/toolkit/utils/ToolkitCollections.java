package com.insaic.toolkit.utils;

import org.dozer.DozerBeanMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Collections
 * Created by leon_yan on 2018/4/16
 */
public class ToolkitCollections {
    /**
     * copy集合中class信息到另一个集合的class
     * @param collection 被复制的集合
     * @param clazz 转换后的class
     * @param <T> 泛型
     * @return list
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> List<T> copyList(Collection collection, Class<T> clazz) throws IllegalAccessException {
        List destinationList = new ArrayList();
        if(!isEmpty(collection)){
            DozerBeanMapper doze = new DozerBeanMapper();
            for (Iterator i$ = collection.iterator(); i$.hasNext();) {
                Object sourceObject = i$.next();
                Object destinationObject = doze.map(sourceObject, clazz);
                //校验属性值是否全部为空，属性值全部为空则不添加，serialVersionUID不校验值
                if(ObjectUtils.objAllFiledNullFlag(destinationObject)){
                    destinationList.add(destinationObject);
                }
            }
        }
        return destinationList;
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }
}