package com.insaic.toolkit.constants;

/**
 * Created by leon_zy on 2018/10/28
 * 数据字典
 */
public interface ToolkitConstants {

    //****************** 数字START *******************//
    /**
     * 数字：1
     */
    int INT_ONE = 1;
    /**
     * 数字：30
     */
    int INT_THIRTY = 30;
    /**
     * 数字：2
     */
    int INT_TWO = 2;
    /**
     * 数字：999999999
     */
    int INT_MAX = 999999999;
    /**
     * 字符串：0
     */
    String ZERO_STR = "0";
    /**
     * 字符串：1
     */
    String ONE_STR = "1";
    /**
     * 字符串：-1
     */
    String MINUS_ONE_STR = "-1";
    //****************** 数字END *******************//

    //****************** 符号START *******************//
    /**
     * 单引号：英文
     */
    String QUOTES_EN = "'";
    /**
     * 冒号：英文
     */
    String COLON_EN = ":";
    /**
     * 冒号：中文
     */
    String COLON_ZH = "：";
    /**
     * 叹号：中文
     */
    String EX_MARK_ZH = "！";
    /**
     * 等于
     */
    String EQUAL_STR = "=";
    /**
     * 且
     */
    String AND_STR = "&";
    /**
     * 逗号：英文
     */
    String COMMA_EN = ",";
    /**
     * 减号
     */
    String MINUS_STR = "-";
    /**
     * 下划线
     */
    String UNDERLINE_STR = "_";
    /**
     * 点：英文
     */
    String DOT_EN = ".";
    /**
     * 空字符串
     */
    String EMPTY_STR = "";
    //****************** 符号END *******************//

    //****************** 日期格式START *******************//
    /**
     * 时间地区
     */
    String GMT_8 = "GMT+8";
    /**
     * 日期格式
     */
    String YYYY_MM_DD = "yyyy-MM-dd";
    /**
     * 时间格式:24小时
     */
    String DATE_YYYY_H24_MM_SS = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期最大值
     */
    String DATE_MAX = "2099-12-31";
    //****************** 日期格式END *******************//

    //****************** 判断属性START *******************//
    /**
     * 是
     */
    String YES = "Y";
    /**
     * 否
     */
    String NO = "N";
    /**
     * 有效
     */
    String VALID = "VALID";
    /**
     * 无效
     */
    String INVALID = "INVALID";
    /**
     * 失败
     */
    String FALSE_STR = "false";
    /**
     * 成功
     */
    String TRUE_STR = "true";
    //****************** 判断属性END *******************//

    //****************** 加密解密属性START *******************//
    /**
     * 加密
     */
    String ENCODE = "ENCODE";
    /**
     * 解密
     */
    String DECODE = "DECODE";
    /**
     * 加密MD5
     */
    String ENCODE_MD5 = "ENCODE_MD5";
    /**
     * 加密BASE64
     */
    String ENCODE_BASE64 = "ENCODE_BASE64";
    //****************** 加密解密属性END *******************//

    //****************** 其他属性START *******************//
    /**
     * 全部
     */
    String ALL_NAME = "全部";
    /**
     * 全部
     */
    String ALL_STR = "ALL";
    /**
     * 字段属性名称
     */
    String serialVersionUID = "serialVersionUID";
    //****************** 其他属性END *******************//
}
