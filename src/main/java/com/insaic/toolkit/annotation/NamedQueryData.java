package com.insaic.toolkit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * NamedQueryData
 * Created by leon_zy on 2018/11/15
 */
@Target({ElementType.TYPE, ElementType.PACKAGE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NamedQueryData {

    String queryId();

    String[] parameters() default {};

    boolean cacheable() default false;

    String cacheRegion() default "";

    int fetchSize() default -1;

    int timeout() default -1;

    String comment() default "";

    boolean readOnly() default false;

    Class<?>[] returnClz() default {};
}