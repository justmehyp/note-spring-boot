package com.example.springboot.annotationattribute;


import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

import java.util.Map;


@Three(two = "4")
public class Main {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        start = System.currentTimeMillis();
        System.out.println(AnnotatedElementUtils.getMergedAnnotationAttributes(Main.class, One.class));
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        System.out.println(AnnotatedElementUtils.getMergedAnnotationAttributes(Main.class, Two.class));
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        System.out.println(AnnotatedElementUtils.getMergedAnnotationAttributes(Main.class, Three.class));
        System.out.println(System.currentTimeMillis() - start);

        System.out.println("-----------");

        start = System.currentTimeMillis();
        System.out.println(AnnotationAttributeUtil.getMergedAnnotationAttributes(Main.class, One.class));
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        System.out.println(AnnotationAttributeUtil.getMergedAnnotationAttributes(Main.class, Two.class));
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        System.out.println(AnnotationAttributeUtil.getMergedAnnotationAttributes(Main.class, Three.class));
        System.out.println(System.currentTimeMillis() - start);
    }
}

