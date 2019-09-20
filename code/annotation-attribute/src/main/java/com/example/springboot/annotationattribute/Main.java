package com.example.springboot.annotationattribute;


import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

import java.util.Map;


@Five(f = "6")
public class Main {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        System.out.println("------ spring -----");


        start = System.currentTimeMillis();
        System.out.println(AnnotatedElementUtils.getMergedAnnotationAttributes(Main.class, Five.class));
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        System.out.println(AnnotatedElementUtils.getMergedAnnotationAttributes(Main.class, Four.class));
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        System.out.println(AnnotatedElementUtils.getMergedAnnotationAttributes(Main.class, Three.class));
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        System.out.println(AnnotatedElementUtils.getMergedAnnotationAttributes(Main.class, Two.class));
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        System.out.println(AnnotatedElementUtils.getMergedAnnotationAttributes(Main.class, One.class));
        System.out.println(System.currentTimeMillis() - start);

        System.out.println("------ me -----");

        start = System.currentTimeMillis();
        System.out.println(AnnotationAttributeUtil.getMergedAnnotationAttributes(Main.class, Five.class));
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        System.out.println(AnnotationAttributeUtil.getMergedAnnotationAttributes(Main.class, Four.class));
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        System.out.println(AnnotationAttributeUtil.getMergedAnnotationAttributes(Main.class, Three.class));
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        System.out.println(AnnotationAttributeUtil.getMergedAnnotationAttributes(Main.class, Two.class));
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        System.out.println(AnnotationAttributeUtil.getMergedAnnotationAttributes(Main.class, One.class));
        System.out.println(System.currentTimeMillis() - start);
    }
}

