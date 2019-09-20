package com.example.springboot.annotationattribute.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface First {

    String firstValue() default "1";

    String anotherFirstValue() default "1";
}
