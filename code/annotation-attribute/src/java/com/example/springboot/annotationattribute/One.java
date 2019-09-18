package com.example.springboot.annotationattribute;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface One {
    String one() default "1";
}
