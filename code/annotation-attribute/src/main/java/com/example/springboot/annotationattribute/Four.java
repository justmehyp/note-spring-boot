package com.example.springboot.annotationattribute;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Three
public @interface Four {

    @AliasFor(value = "two", annotation = Two.class)
    String four() default "4";

    @AliasFor(value = "two", annotation = Two.class)
    String f() default "4";
}
