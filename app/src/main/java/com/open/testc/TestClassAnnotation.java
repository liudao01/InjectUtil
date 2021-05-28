package com.open.testc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface TestClassAnnotation {
    String id();
//    String value();
    //如果这里不是value 是id那么调用的时候需要id = "22"  value比较特殊
} 