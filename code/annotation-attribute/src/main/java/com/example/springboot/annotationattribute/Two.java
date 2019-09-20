package com.example.springboot.annotationattribute;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@One
public @interface Two {

    String two() default "2";
}
