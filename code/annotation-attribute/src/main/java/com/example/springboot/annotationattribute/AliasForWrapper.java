package com.example.springboot.annotationattribute;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

class AliasForWrapper {
    private Class<? extends Annotation> annotationType;
    private String attributeName;
    private Class<? extends Annotation> aliasAnnotationType;
    private String aliasAttributeName;

    private static final Class<? extends Annotation> DEFAULT_ANNOTATION_TYPE = Annotation.class;
    private static final String DEFAULT_ALIAS_FOR_VALUE = "";

    @SuppressWarnings("unchecked")
    private AliasForWrapper(Method attribute, AliasFor aliasFor) {
        this.annotationType = (Class<? extends Annotation>) attribute.getDeclaringClass();
        this.attributeName = attribute.getName();

        Class<? extends Annotation> annotation = aliasFor.annotation();
        this.aliasAnnotationType = DEFAULT_ANNOTATION_TYPE.equals(annotation) ? this.annotationType : annotation;

        String valueFromAliasFor = getValueFromAliasFor(aliasFor);
        this.aliasAttributeName = DEFAULT_ALIAS_FOR_VALUE.equals(valueFromAliasFor) ? attributeName : valueFromAliasFor;
    }

    private String getValueFromAliasFor(AliasFor aliasFor) {
        String value = aliasFor.value();
        String attribute = aliasFor.attribute();
        if (!DEFAULT_ALIAS_FOR_VALUE.equals(value) && !DEFAULT_ALIAS_FOR_VALUE.equals(attribute) && !value.equals(attribute)) {
            throw new RuntimeException(String.format("In @AliasFor declared on attribute '%s' of annotation " +
                    "'%s', attribute 'attribute' and its alias 'value' are present with value of [%s] and [%s], " +
                    "but only one is permitted.", attributeName, annotationType.getName(), attribute, value));
        }
        return !DEFAULT_ALIAS_FOR_VALUE.equals(value) ? value : attribute;
    }

    static AliasForWrapper build(Method attribute) {
        AliasFor aliasFor = attribute.getAnnotation(AliasFor.class);
        if (aliasFor != null) {
            return new AliasForWrapper(attribute, aliasFor);
        }
        return null;
    }

    boolean isPointToSameAnnotation() {
        return aliasAnnotationType.equals(annotationType);
    }

    boolean isPointTo(Class<? extends Annotation> annotationType, String attributeName) {
        return aliasAnnotationType.equals(annotationType) && attributeName.equals(aliasAttributeName);
    }

    boolean isPointToSelf() {
        return aliasAnnotationType.equals(annotationType) && attributeName.equals(aliasAttributeName);
    }

    Class<? extends Annotation> getAliasAnnotationType() {
        return aliasAnnotationType;
    }

    String getAliasAttributeName() {
        return aliasAttributeName;
    }
}
