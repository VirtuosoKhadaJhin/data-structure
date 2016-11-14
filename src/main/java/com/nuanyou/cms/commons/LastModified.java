package com.nuanyou.cms.commons;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by xiejing on 2016/7/26.
 */
@Documented
@Target({FIELD, METHOD})
@Retention(RUNTIME)
public @interface LastModified {
    String value() default "yyyy-MM-dd HH:mm:ss";
}
