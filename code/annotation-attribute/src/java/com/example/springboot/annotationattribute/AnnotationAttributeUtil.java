package com.example.springboot.annotationattribute;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationAttributeUtil {

    private static final Object NULL_ATTRIBUTE_VALUE = new Object();

    public static Map<String, Object> getMergedAnnotationAttributes(AnnotatedElement annotatedElement,
                                                                    Class<? extends Annotation> annotationType) {
        return getMergedAnnotationAttributes(annotatedElement, null, annotationType);
    }

    private static Map<String, Object> getMergedAnnotationAttributes(AnnotatedElement annotatedElement,
                                                                     AnnotationHolder annotatedAnnotationHolder,
                                                                     Class<? extends Annotation> annotationType) {
        // annotations
        List<Annotation> annotations = getAnnotations(annotatedElement);
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annType = annotation.annotationType();
            if (annotationType.equals(annType)) {
                return getAttributes(new AnnotationHolder(annotation, annotatedAnnotationHolder));
            }
        }

        // meta-annotations
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annType = annotation.annotationType();
            return getMergedAnnotationAttributes(annType, new AnnotationHolder(annotation, annotatedAnnotationHolder),
                    annotationType);
        }

        return null;
    }

    private static List<Annotation> getAnnotations(AnnotatedElement annotatedElement) {
        return Arrays.stream(annotatedElement.getAnnotations())
                .filter((annotation -> !annotation.annotationType().getName().startsWith("java.lang.annotation")))
                .collect(Collectors.toList());
    }

    private static Map<String, Object> getAttributes(AnnotationHolder annotationHolder) {
        Class<? extends Annotation> annotationType = annotationHolder.annotationType();
        Method[] attributes = annotationType.getDeclaredMethods();

        Map<String, Object> annotationAttributes = new HashMap<>();
        for (Method attribute : attributes) {
            String attributeName = attribute.getName();
            annotationAttributes.put(attributeName, getAttributeValue(annotationHolder, attributeName));
        }
        return annotationAttributes;
    }

    private static Object getAttributeValue(AnnotationHolder annotationHolder, String attributeName) {
        Object attributeValue = NULL_ATTRIBUTE_VALUE;
        if (annotationHolder.child != null) {
            attributeValue = getAttributeValue(annotationHolder.child, attributeName);
        }

        if (NULL_ATTRIBUTE_VALUE.equals(attributeValue)) {
            attributeValue = doGetAttributeValue(annotationHolder.self, attributeName);
        }

        return attributeValue;
    }

    private static Object doGetAttributeValue(Annotation annotation, String attributeName) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        Method[] attributes = annotationType.getDeclaredMethods();
        Method attribute = getAttribute(attributes, attributeName);
        Object attributeValue = NULL_ATTRIBUTE_VALUE;
        if (attribute != null) {
            try {
                attributeValue = attribute.invoke(annotation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return attributeValue;
    }

    private static Method getAttribute(Method[] attributes, String attributeName) {
        for (Method attribute : attributes) {
            if (attribute.getName().equals(attributeName)) {
                return attribute;
            }
        }
        return null;
    }
}
