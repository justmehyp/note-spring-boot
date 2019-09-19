package com.example.springboot.annotationattribute;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Four
public @interface Five {
    String f() default "5";
}
