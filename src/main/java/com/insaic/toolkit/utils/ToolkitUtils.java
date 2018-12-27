package com.insaic.toolkit.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.insaic.base.utils.Collections3;
import com.insaic.base.utils.DateUtil;
import com.insaic.base.utils.Reflections;
import com.insaic.base.utils.StringUtil;
import com.insaic.toolkit.annotation.DateValid;
import com.insaic.toolkit.constants.ToolkitConstants;
import com.insaic.toolkit.enums.EncodingEnum;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlElement;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ToolkitUtils 工具类
 * Created by leon_yan on 2018/5/30
 */
public final class ToolkitUtils {
    private static final Logger logger = LoggerFactory.getLogger(ToolkitUtils.class);

    //中文字符、英文字母和中英特殊符号
    private static final String regEx = "[^0-9]";
    private static final String regEx_prefix_site = "{0}[0-9]{1}";
    private static final String brace_left = "{";
    private static final String brace_right = "}";
    private final static int[] li_SecPosValue = { 1601, 1637, 1833, 2078, 2274,
            2302, 2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858,
            4027, 4086, 4390, 4558, 4684, 4925, 5249, 5590 };
    private final static String[] lc_FirstLetter = { "a", "b", "c", "d", "e",
            "f", "g", "h", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "w", "x", "y", "z" };


    /**
     * 获取属性上指定类型的注解name
     * @param f 属性
     * @param clazz 属性上的注解类
     * @return str
     */
    public static String getFileAnnotationNameByClass(Field f, Class clazz){
        String name = ToolkitConstants.EMPTY_STR;
        if(null != clazz){
            //获取属性上的指定类型的注解
            Annotation annotation = f.getAnnotation(clazz);
            if (null != annotation) {
                //强制转化为相应的注解，获取属性上的指定类型的注解的name
                if(clazz.equals(XmlElement.class)){
                    XmlElement obj = (XmlElement)annotation;
                    name = obj.name();
                }else if(clazz.equals(Column.class)){
                    Column obj = (Column)annotation;
                    name = obj.name();
                }
            }
        }
        return name;
    }

    /**
     * java驼峰字段转成数据库下划线字段
     * @param name 字段名称
     * @return str
     */
    public static String underscoreName(String name) {
        StringBuilder result = new StringBuilder();
        if(StringUtil.isNotBlank(name)){
            //将第一个字符处理成大写
            result.append(name.substring(0,1).toUpperCase());
            //循环处理其余字符
            for(int i =1;i < name.length(); i++) {
                String s = name.substring(i, i +1);
                //在大写字母前添加下划线
                if(s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
                    result.append(ToolkitConstants.UNDERLINE_STR);
                }
                //其他字符直接转成大写
                result.append(s.toUpperCase());
            }
        }
        return  result.toString();
    }

    /**
     * 数据库字段转成java驼峰式
     * @param name 字段名称
     * @return str
     */
    public static String convertToHump(String name) {
        StringBuilder buffer = new StringBuilder();
        if(StringUtil.isNotBlank(name)){
            if(name.contains(ToolkitConstants.UNDERLINE_STR)){
                String[] words = name.toLowerCase().split(ToolkitConstants.UNDERLINE_STR);
                for(int i = 0; i < words.length; i++){
                    String word = words[i];
                    String firstLetter = word.substring(0, 1);
                    String others = word.substring(1);
                    String upperLetter = i != 0 ? firstLetter.toUpperCase() : firstLetter;
                    buffer.append(upperLetter).append(others);
                }
            }else{
                buffer.append(name.toLowerCase());
            }
        }
        return buffer.toString();
    }

    /**
     * 判断对象是否为空
     * @param obj 对象
     * @return boolean
     */
    public static Boolean isBlankOrNull(Object obj){
        return null == obj || ToolkitConstants.EMPTY_STR.equals(obj);
    }

    /**
     * 获取属性类型
     * @param type 属性类型
     * @param objVal 属性值
     * @return Object
     */
    public static Object replaceObjType(String type, Object objVal){
        Object val = null;
        switch (type) {
            case "class java.lang.String":
                val = isBlankOrNull(objVal) ? null : String.valueOf(objVal);
                break;
            case "class java.lang.Long":
                val = isBlankOrNull(objVal) ? null : Long.valueOf(String.valueOf(objVal));
                break;
            case "class java.lang.Integer":
                val = isBlankOrNull(objVal) ? null : Integer.valueOf(String.valueOf(objVal));
                break;
            case "class java.lang.Short":
                val = isBlankOrNull(objVal) ? null : Short.valueOf(String.valueOf(objVal));
                break;
            case "class java.lang.Double":
                val = isBlankOrNull(objVal) ? null : Double.valueOf(String.valueOf(objVal));
                break;
            case "class java.lang.Boolean":
                val = isBlankOrNull(objVal) ? null : Boolean.valueOf(String.valueOf(objVal));
                break;
            case "class java.util.Date":
                val = isBlankOrNull(objVal) ? null : DateUtil.stringToDate(String.valueOf(objVal),
                        String.valueOf(objVal).length() > 10 ?  "yyyy-MM-dd HH:mm:ss" : "yyyy-MM-dd");
                break;
            case "class java.math.BigDecimal":
                val = isBlankOrNull(objVal) ? null : new BigDecimal(String.valueOf(objVal));
                break;
        }
        return val;
    }

    /**
     * 组装属性为字符串
     * @param files 属性值
     * @return str
     */
    public static String buildFileStr(Object... files){
        StringBuilder str = new StringBuilder();
        if(null != files && files.length > 0){
            for(Object file : files){
                str.append(StringUtil.toString(file));
            }
        }
        return str.toString();
    }

    /**
     * 根据key判断集合中是否有重复数据，排除重复数据
     * @param eo 数据库数据
     * @param map 已处理的map集合
     * @param list 需要保存的
     * @param fileStr key
     * @param <T> 泛型
     */
    public static <T> void addObjectEO(T eo, Map<String, String> map, List<T> list, String fileStr){
        if(StringUtil.isNotBlank(fileStr) && null == map.get(fileStr)){
            list.add(eo);
            map.put(fileStr, fileStr);
        }
    }

    /**
     * 字符串首字母转大写
     * @param str 字符串
     * @return str
     */
    public static String firstLetterUpper(String str) {
        String val = null;
        if(StringUtil.isNotBlank(str)){
            char[] ch = str.toCharArray();
            if (ch[0] >= 'a' && ch[0] <= 'z') {
                ch[0] = (char) (ch[0] - 32);
            }
            val = String.valueOf(ch);
        }
        return val;
    }

    /**
     * 判断错误信息是否存在返回成功或失败
     * @param errorMsg 错误信息
     * @return str
     */
    public static String returnSuccessStr(String errorMsg){
        return StringUtil.isBlank(errorMsg) ? ToolkitConstants.TRUE_STR : ToolkitConstants.FALSE_STR;
    }

    /**
     * 半角转全角
     * @param str String
     * @return 全角字符串
     */
    public static String toSBC(String str) {
        char c[] = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    /**
     * 全角转半角
     * @param str String
     * @return 半角字符串
     */
    public static String toDBC(String str) {
        char c[] = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    /**
     * 字符串全角转半角并把中英文和特殊字符转为英文逗号，方便分隔
     * @param str 字符串
     * @return str
     */
    public static String replaceSpecStr(Object str) {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(toDBC(StringUtil.toString(str)));
        return m.replaceAll(ToolkitConstants.COMMA_EN).trim();
    }

    /**
     * 属性值转为半角并获取属性指定长度的值
     * @param val 属性值
     * @param len 属性值的长度
     * @return str
     */
    public static String getFileLengthStr(Object val, int len){
        String[] values = replaceSpecStr(val).split(ToolkitConstants.COMMA_EN);
        return (String) CollectionUtils.find(Arrays.asList(values), o -> StringUtil.toString(o).length() == len);
    }

    /**
     * 属性值转为半角并获取属性指定长度和前缀的值
     * @param val 属性值
     * @param prefix 前缀
     * @param len 属性值的长度
     * @return str
     */
    public static String getFileLengthStrPrefix(Object val, String prefix, int len){
        String str = null;
        String regExStr = MessageFormat.format(regEx_prefix_site, StringUtil.toString(prefix), brace_left + len + brace_right);
        Pattern p = Pattern.compile(regExStr);
        Matcher m = p.matcher(toDBC(StringUtil.toString(val)));
        if(m.find()){
            str = m.group();
        }
        return str;
    }

    /**
     * 字符串全角转半角并把中英文和特殊字符转为英文逗号，方便分隔
     * @param str 字符串
     * @return str
     */
    public static String replaceSpecStrPrefix(Object str, String prefix, int len) {
        String regExStr = MessageFormat.format(regEx_prefix_site, StringUtil.toString(prefix), brace_left + len + brace_right);
        Pattern p = Pattern.compile(regExStr);
        Matcher m = p.matcher(toDBC(StringUtil.toString(str)));
        return m.replaceAll(ToolkitConstants.COMMA_EN).trim();
    }

    /**
     * 根据属性名和值查询集合中的对象
     * @param list  对象集合
     * @param fileName 属性名称
     * @param val 属性值
     * @return obj
     */
    public static <T> T findObjByPropertyNameValue(List<T> list, String fileName, Object val){
        T result = null;
        String upperName = firstLetterUpper(fileName);
        if(Collections3.isNotEmpty(list) && StringUtil.isNotBlank(upperName)){
            Method method;
            Object value;
            try {
                for (T item : list) {
                    method = item.getClass().getMethod(ToolkitConstants.GET_STR + upperName);
                    value = method.invoke(item);
                    if (StringUtil.toString(value).equals(StringUtil.toString(val))) {
                        result = item;
                        break;
                    }
                }
            } catch (Exception e) {
                logger.error("findObjByPropertyNameValue：" + e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * 根据属性名和值查询集合中的对象集合
     * @param list  对象集合
     * @param fileName 属性名称
     * @param val 属性值
     * @return list
     */
    public static <T> List<T> findListByPropertyNameValue(List<T> list, String fileName, Object val){
        List<T> result = new ArrayList<>();
        String upperName = firstLetterUpper(fileName);
        if(Collections3.isNotEmpty(list) && StringUtil.isNotBlank(upperName)){
            Method method;
            Object value;
            try {
                for (T item : list) {
                    method = item.getClass().getMethod(ToolkitConstants.GET_STR + upperName);
                    value = method.invoke(item);
                    if (StringUtil.toString(value).equals(StringUtil.toString(val))) {
                        result.add(item);
                    }
                }
            } catch (Exception e) {
                logger.error("findListByPropertyNameValue：" + e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * 获取开始时间当天最早的时间
     * @param startDate 开始时间
     * @return 日期
     */
    public static Date getStartDateDayLastTime(Date startDate){
        Date date = startDate;
        if(null!= startDate){
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            date = cal.getTime();
        }
        return date;
    }

    /**
     * 获取结束时间当天最后的时间
     * @param endDate 结束时间
     * @return 日期
     */
    public static Date getEndDateDayLastTime(Date endDate){
        Date date = endDate;
        if(null != endDate){
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            cal.set(Calendar.HOUR, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            date = cal.getTime();
        }
        return date;
    }

    /**
     * 获取属性为null的属性名称
     * @param source 对象
     * @return 数组
     */
    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<String>();
        for(PropertyDescriptor pd : pds) {
            /*Field f=Reflections.getAccessibleField(source,pd.getName());
            if(f!=null){
                if(f.isAnnotationPresent(ForceCopyField.class)){
                    // emptyNames.add(pd.getName());
                    continue;
                }
            }*/
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null || ToolkitConstants.EMPTY_STR.equals(srcValue)){
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 确认对象中是否有需要加密解密的属性并赋值
     * @param source 对象
     */
    public static void copyDateProperty(Object source, Object target, String... ignoreProperties){
        try {
            //获取成员变量
            Field[] fields = Reflections.getAllFields(source.getClass());
            Object val;
            List<String> ignoreList = ignoreProperties != null ? Arrays.asList(ignoreProperties) : null;
            for(Field field : fields){
                //设置属性可访问
                field.setAccessible(true);
                val = field.get(source);
                //校验对象是否为基础类型
                if(val instanceof String && (ignoreList == null || !ignoreList.contains(field.getName()))){
                    String str = StringUtil.toString(val);
                    //判断成员变量是否有注解
                    if(StringUtil.isNotBlank(str) && field.isAnnotationPresent(DateValid.class)){
                        //获取定义在成员变量中的注解
                        DateValid myAnnotation = field.getAnnotation(DateValid.class);
                        //获取注解设置的属性值
                        String formatStr = myAnnotation.format();
                        if(StringUtil.isNotBlank(formatStr)
                                && null != BeanUtils.getPropertyDescriptor(target.getClass(), field.getName())){
                            Field dateField = target.getClass().getDeclaredField(field.getName());
                            if(null != dateField && Date.class.equals(dateField.getType())){
                                dateField.setAccessible(true);
                                SimpleDateFormat format = new SimpleDateFormat(formatStr);
                                format.setLenient(false);
                                //将加值赋给成员变量
                                dateField.set(target, format.parse(str));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("copyDateProperty出错：" + e.getMessage(), e);
        }
    }

    /**
     * 获取利用反射获取类里面的值和名称
     * @param obj 对象
     * @return map
     * @throws IllegalAccessException 异常
     */
    public static Map<String, String> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, String> map = new HashMap<>();
        //获取成员变量
        Field[] fields = Reflections.getAllFields(obj.getClass());
        String str;
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            Boolean serializeFlag = true;
            if(field.isAnnotationPresent(JSONField.class)){
                JSONField myAnnotation = field.getAnnotation(JSONField.class);
                serializeFlag = myAnnotation.serialize();
                if(serializeFlag){
                    fieldName = myAnnotation.name();
                }
            }
            if(serializeFlag && !ToolkitConstants.serialVersionUID.equals(fieldName)){
                if(validFieldsBaseTypeFlag(value)){
                    str = StringUtil.toString(value);
                }else{
                    str = JSONObject.toJSONString(value);
                }
                map.put(fieldName, str);
            }
        }
        return map;
    }

    /**
     * 首字母转换为小写
     * @param str 字符串
     * @return str
     */
    public static String firstLetterLower(String str) {
        String val = null;
        if (StringUtil.isNotBlank(str)) {
            char[] ch = str.toCharArray();
            if (ch[0] >= 'A' && ch[0] <= 'Z') {
                ch[0] = (char)(ch[0] + 32);
            }
            val = String.valueOf(ch);
        }
        return val;
    }

    /**
     * 取得给定汉字串的首字母串,即声母串
     * @param str 给定汉字串
     * @return str
     */
    public static String getAllFirstLetter(String str) {
        String value = ToolkitConstants.EMPTY_STR;
        if (StringUtil.isNotBlank(str)) {
            StringBuilder _str = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                _str.append(getFirstLetter(str.substring(i, i + 1)));
            }
            value = _str.toString();
        }
        return value;
    }

    /**
     * 取得给定汉字的首字母,即声母
     * @param chStr 给定的汉字
     * @return 给定汉字的声母
     */
    public static String getFirstLetter(String chStr) {
        String str = ToolkitConstants.EMPTY_STR;
        if (StringUtil.isNotBlank(chStr)) {
            str = conversionStr(chStr, EncodingEnum.GB2312.getCode(), EncodingEnum.ISO8859_1.getCode());
            if (str.length() > 1) {
                int li_SectorCode = ((int) str.charAt(0)) - 160; // 汉字区码
                int li_PositionCode = ((int) str.charAt(1)) - 160; // 汉字位码
                int li_SecPosCode = li_SectorCode * 100 + li_PositionCode; // 汉字区位码
                // 判断是不是汉字
                if (li_SecPosCode > 1600 && li_SecPosCode < 5590) {
                    for (int i = 0; i < 23; i++) {
                        if (li_SecPosCode >= li_SecPosValue[i]
                                && li_SecPosCode < li_SecPosValue[i + 1]) {
                            str = lc_FirstLetter[i];
                            break;
                        }
                    }
                } else {
                    // 非汉字字符,如图形符号或ASCII码
                    str = conversionStr(str, EncodingEnum.ISO8859_1.getCode(), EncodingEnum.GB2312.getCode());
                    str = str.substring(0, 1);
                }
            }
        }
        return str;
    }

    /**
     * 字符串编码转换
     * @param str 要转换编码的字符串
     * @return 经过编码转换后的字符串
     */
    private static String conversionStr(String str, String charsetName,String toCharsetName) {
        String conStr = str;
        try {
            conStr = new String(str.getBytes(charsetName), toCharsetName);
        } catch (UnsupportedEncodingException ex) {
            logger.error("conversionStr字符串编码转换异常!", ex);
        }
        return conStr;
    }

    /**
     * 校验对象是否为基础类型
     * @param obj 对象
     * @return boolean
     */
    public static Boolean validFieldsBaseTypeFlag(Object obj){
        return obj instanceof String  || obj instanceof Integer
                || obj instanceof Double || obj instanceof Float
                || obj instanceof Long || obj instanceof Boolean
                || obj instanceof Date || obj instanceof BigDecimal;
    }

}