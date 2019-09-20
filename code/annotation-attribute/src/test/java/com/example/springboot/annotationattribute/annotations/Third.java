package com.example.springboot.annotationattribute.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Second
public @interface Third {

    String secondValue() default "3";

    @AliasFor(value = "anotherFirstValue", annotation = Second.class)
    String thirdValue() default "3";
}
