package com.insaic.toolkit.utils;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.insaic.base.utils.StringUtil;
import com.insaic.toolkit.constants.ToolkitConstants;
import com.insaic.toolkit.enums.EncodingEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * ModelClassUtils
 * Created by leon_zy on 2018/10/17
 */
public class ModelClassUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 获取类加载器
     * @return ClassLoader    返回类型
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类，需要提供类名与是否初始化的标志，初始化是指是否执行静态代码块
     * @param className 类名
     * @param isInitialized  为提高性能设置为false
     * @return Class
     */
    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> cls;
        try {
            cls = Class.forName(className, isInitialized, getClassLoader());
            //Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            LOGGER.error("加载类失败 loadClass->{}", e);
            throw new RuntimeException(e);
        }
        return cls;
    }

    /**
     * 校验指定包下的所有类
     * @param packageName 包名
     * @return Set 返回类型
     */
    public static <A extends Annotation> String validPackageClass(String packageName, Class<A> annotationCls) {
        StringBuilder str = new StringBuilder();
        Set<Class<?>> classSet = getClassSet(packageName);
        for(Class<?> cls : classSet){
            if(null == cls.getAnnotation(annotationCls)){
                if(StringUtil.isNotBlank(str.toString())){
                    str.append(ToolkitConstants.COMMA_EN);
                }
                str.append(cls.getSimpleName());
            }
        }
        return str.toString();
    }

    /**
     * 加载指定包下的所有类
     * @param packageName 包名
     * @return Set 返回类型
     */
    public static Set<Class<?>> getClassSet(String packageName) {
        Set<Class<?>> classSet = new HashSet<>();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")) {
                        // 转码
                        String packagePath = URLDecoder.decode(url.getFile(), EncodingEnum.UTF_8.getCode());
                        // 添加
                        addClass(classSet, packagePath, packageName);
                    } else if (protocol.equals("jar")) {
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        if (jarURLConnection != null) {
                            JarFile jarFile = jarURLConnection.getJarFile();
                            if (jarFile != null) {
                                Enumeration<JarEntry> jarEntries = jarFile.entries();
                                while (jarEntries.hasMoreElements()) {
                                    JarEntry jarEntry = jarEntries.nextElement();
                                    String jarEntryName = jarEntry.getName();
                                    if (jarEntryName.endsWith(".class")) {
                                        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf("."))
                                                .replaceAll("/", ".");
                                        if(className.contains(packageName)){
                                            doAddClass(classSet, className);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("查找包下的类失败{}", e);
            throw new RuntimeException("查找包下的类失败！");
        }
        return classSet;
    }

    /**
     * 添加文件到SET集合
     * @param classSet 类集合
     * @param packagePath 包地址
     * @param packageName 包名
     */
    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class") || file.isDirectory());
            }
        });
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (StringUtils.isNotBlank(packageName)) {
                    className = packageName + "." + className;
                    LOGGER.info("className: " + className);
                }
                // 添加
                doAddClass(classSet, className);
            } else {
                // 子目录
                String subPackagePath = fileName;
                if (StringUtils.isNotBlank(packagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (StringUtils.isNotBlank(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                addClass(classSet, subPackagePath, subPackageName);
            }
        }

    }

    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> cls = loadClass(className, false);
        classSet.add(cls);
    }

    private static <A extends Annotation> void validClassAnnotation(Class<?> cls, Class<A> clazz, String errorMsg){
        if(null == cls.getAnnotation(clazz)){
            LOGGER.info(errorMsg);
            throw new RuntimeException(errorMsg);
        }
    }

}