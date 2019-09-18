package com.example.springboot.annotationattribute;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Two
public @interface Three {
    String one() default "3";

    String two() default "3";

    String three() default "3";
}
