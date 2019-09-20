package com.example.springboot.annotationattribute.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@First
public @interface Second {

    @AliasFor(value = "firstValue", annotation = First.class)
    String secondValue() default "2";

    String anotherFirstValue() default "2";
}
