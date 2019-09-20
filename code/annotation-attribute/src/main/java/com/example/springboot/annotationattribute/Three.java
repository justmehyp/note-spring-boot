package com.example.springboot.annotationattribute;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Two
public @interface Three {

    String one() default "3";

    @AliasFor(value = "one", annotation = One.class)
    String three() default "3";
}
