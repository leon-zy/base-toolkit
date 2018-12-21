package com.insaic.toolkit.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * De 获取方法中局部变量的名称
 * Created by leon_zy on 2018/7/12
 */
public class MethodDataUtils {
    public MethodDataUtils() {
    }

    /**
     * 打印 变量名称 和 变量值
     * @param args
     */
    public static void main(String[] args) {
        String str="abc";
        Map<String, String> result = print(str);//str=abc
        int num=789;
        Map<String, String> result1 = print(num);//num=789
        String[]strArray={"qqq","www","eee","rrr","ttt"};
        Map<String, String> result2 = print(strArray);//strArray=[qqq, www, eee, rrr, ttt]
        int[]intArray={111,222,333};
        Map<String, String> result3 = print(intArray);//intArray=[111, 222, 333]
        Set set = result.keySet();
    }

    public static Map<String, String> print(Object obj) {
        Map<String, String> result = new HashMap<>();
        StackTraceElement ste = (new Throwable()).getStackTrace()[1];
        String lineStr = print(ste);
        String name = extract(lineStr);
        String value = toStringSupportArray(obj);
        System.out.println(name + "=" + value);
        result.put(name, value);
        return result;
    }

    public static String getFileNameStr(Object obj){
        StackTraceElement ste = (new Throwable()).getStackTrace()[1];
        return print(ste);
    }

    private static String toStringSupportArray(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof int[]) {
            return Arrays.toString((int[])obj);
        } else if (obj instanceof long[]) {
            return Arrays.toString((long[])obj);
        } else if (obj instanceof double[]) {
            return Arrays.toString((double[])obj);
        } else if (obj instanceof boolean[]) {
            return Arrays.toString((boolean[])obj);
        } else if (obj instanceof char[]) {
            return Arrays.toString((char[])obj);
        } else if (obj instanceof byte[]) {
            return Arrays.toString((byte[])obj);
        } else if (obj instanceof short[]) {
            return Arrays.toString((short[])obj);
        } else if (obj instanceof float[]) {
            return Arrays.toString((float[])obj);
        } else {
            return obj instanceof Object[] ? Arrays.toString((Object[])obj) : obj.toString();
        }
    }

    private static String extract(String lineStr) {
        lineStr = lineStr.trim();
        int start = lineStr.indexOf("De.print(") + 9;
        int end = lineStr.lastIndexOf(");");
        return lineStr.substring(start, end);
    }

    private static String print(StackTraceElement ste) {
        String fileName = ste.getClassName();
        fileName = "src/" + fileName.replace(".", "/") + ".java";
        int line = ste.getLineNumber();
        return getStrByLine(fileName, line);
    }

    private static String getStrByLine(String fileName, int num) {
        File file = new File(fileName);
        BufferedReader reader = null;
        String tempString;
        int line;
        String path;
        if (!file.exists()) {
            tempString = file.getName();
            line = tempString.lastIndexOf("$");
            if (line == -1) {
                throw new RuntimeException("文件名无法识别");
            }

            String fullName = file.getAbsolutePath();
            fullName = fullName.replace("\\", "/");
            path = fullName.substring(0, fullName.lastIndexOf("/"));
            file = new File(path + "/" + file.getName().substring(0, line) + ".java");
        }

        try {
            reader = new BufferedReader(new FileReader(file));
            tempString = null;

            for(line = 1; (tempString = reader.readLine()) != null; ++line) {
                if (line == num) {
                    path = tempString;
                    return path;
                }
            }

            reader.close();
            return null;
        } catch (IOException var16) {
            var16.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException var15) {
                    ;
                }
            }

        }
    }
}