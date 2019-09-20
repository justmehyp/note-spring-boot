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
            attributeValue = overrideAttributeValue(annotationHolder.child, annotationHolder.annotationType(), attributeName);
        }

        if (NULL_ATTRIBUTE_VALUE.equals(attributeValue)) {
            attributeValue = doGetAttributeValue(annotationHolder.self, attributeName);
        }

        return attributeValue;
    }

    private static Object overrideAttributeValue(AnnotationHolder childAnnotationHold,
                                                 Class<? extends Annotation> annotationType, String attributeName) {
        if (childAnnotationHold.child != null) {
            Object value = overrideAttributeValue(childAnnotationHold.child, annotationType, attributeName);
            if (NULL_ATTRIBUTE_VALUE.equals(value)) {
                value = getValueFromAliasAttributes(childAnnotationHold.child.self, annotationType, attributeName);
            }
            if (!NULL_ATTRIBUTE_VALUE.equals(value)) {
                return value;
            }
        }

        return doGetAttributeValue(childAnnotationHold.self, attributeName);
    }

    private static Object getValueFromAliasAttributes(Annotation annotation, Class<? extends Annotation> annotationType,
                                                      String attributeName) {
        List<Method> aliasAttributes = getAliasAttributes(annotation, annotationType, attributeName);
        if (!aliasAttributes.isEmpty()) {
            return doGetValueFromAliasAttributes(annotation, aliasAttributes);
        }
        else {
            return NULL_ATTRIBUTE_VALUE;
        }
    }

    private static Object doGetValueFromAliasAttributes(Annotation annotation, List<Method> aliasAttributes) {
        Method firstAttribute = aliasAttributes.get(0);
        Object firstDefaultValue = firstAttribute.getDefaultValue();
        Object attributeValue = NULL_ATTRIBUTE_VALUE;
        Method attrWithSuppliedValue = null;

        Object firstAttributeValue = evalAttributeValue(annotation, firstAttribute);
        if (!NULL_ATTRIBUTE_VALUE.equals(firstAttributeValue)) {
            attributeValue = firstAttributeValue;
        }

        for (int i = 1, size = aliasAttributes.size(); i < size; i++) {
            // check default value
            Method attribute = aliasAttributes.get(i);
            Object defaultValue = attribute.getDefaultValue();
            if ((firstDefaultValue == null && defaultValue != null) ||
                    (firstDefaultValue != null && !firstDefaultValue.equals(defaultValue))) {
                throw new RuntimeException(String.format("Attribute '%s' and its alias '%s' in annotation [%s] must " +
                                "be declared same defautl value.", firstAttribute.getName(), attribute.getName(),
                        annotation.annotationType().getName()));
            }

            // find attribute with supplied value
            Object value = evalAttributeValue(annotation, attribute);
            if (NULL_ATTRIBUTE_VALUE.equals(attributeValue) && (defaultValue != null && !defaultValue.equals(value))) {
                attributeValue = value;
                attrWithSuppliedValue = attribute;
            }

            // check supplied value
            if (!NULL_ATTRIBUTE_VALUE.equals(attributeValue) && (defaultValue != null && !defaultValue.equals(value)) &&
                    !attributeValue.equals(value)) {
                throw new RuntimeException(String.format("Attribute '%s' and its alias '%s' in annotation '%s' are " +
                        "present with values of [%s] and [%s], but only one is permitted.", attrWithSuppliedValue,
                        attribute.getName(), annotation.annotationType().getName(), attributeValue, value));
            }
        }
        return attributeValue;
    }

    private static List<Method> getAliasAttributes(Annotation annotation, Class<? extends Annotation> annotationType,
                                                   String attributeName) {
        Method[] attributes = annotation.annotationType().getDeclaredMethods();
        List<Method> aliasAttributes = new ArrayList<>();
        for (Method attribute : attributes) {
            AliasForWrapper aliasForWrapper = AliasForWrapper.build(attribute);
            if (aliasForWrapper != null && aliasForWrapper.isPointTo(annotationType, attributeName)) {
                aliasAttributes.add(attribute);
            }
        }
        return aliasAttributes;
    }

    private static Object doGetAttributeValue(Annotation annotation, String attributeName) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        Method[] attributes = annotationType.getDeclaredMethods();
        if(attributes.length > 0) {
            Method attribute = getAttribute(attributes, attributeName);
            if(attribute != null) {
                AliasForWrapper aliasForWrapper = AliasForWrapper.build(attribute);
                if (aliasForWrapper != null) { // annotated with @Alias
                    if (aliasForWrapper.isPointToSameAnnotation()) {
                        if (aliasForWrapper.isPointToSelf()) {
                            throw new RuntimeException(String.format("Attribute '%s' in annotation [%s] point to itself, " +
                                    "specify attribute 'annotation' to point to an attribute in hierarchy.",
                                    attributeName, annotationType.getName()));
                        }
                        Method aliasAttribute = getAttribute(attributes, aliasForWrapper.getAliasAttributeName());
                        if (aliasAttribute == null) {
                            throw new RuntimeException(String.format("Declaring a nonexistent attribute '%s' in " +
                                            "annotation '%s'.", aliasForWrapper.getAliasAttributeName(),
                                    annotationType.getName()));
                        }
                        AliasForWrapper aliasAliasForWrapper = AliasForWrapper.build(aliasAttribute);
                        if (aliasAliasForWrapper == null) {
                            throw new RuntimeException(String.format("Attribute '%s' in annotation [%s] must be " +
                                    "declared as @AliasFor [%s]", aliasAttribute.getName(), annotationType.getName(),
                                    attributeName));
                        } else if (!aliasAliasForWrapper.isPointTo(annotationType, attributeName)) {
                            throw new RuntimeException(String.format("Attribute '%s' in annotation [%s] must be " +
                                            "declared as @AliasFor [%s], not [%s]", aliasAttribute.getName(),
                                    annotationType.getName(), attributeName, aliasAliasForWrapper.getAliasAttributeName()));
                        } else {
                            return getValueFromAttributeOrAlias(annotation, attribute, aliasAttribute);
                        }
                    }
                    else {
                        return getValueFromAliasAttributes(annotation, aliasForWrapper.getAliasAnnotationType(),
                                aliasForWrapper.getAliasAttributeName());
                    }
                }
                else {
                    return getValueFromAttribute(annotation, attribute);
                }
            }
        }
        return NULL_ATTRIBUTE_VALUE;
    }

    private static Object getValueFromAttributeOrAlias(Annotation annotation, Method attribute, Method aliasAttribute) {
        // check default value
        Object defaultValue = attribute.getDefaultValue();
        Object aliasDefaultValue = aliasAttribute.getDefaultValue();
        if ((defaultValue == null && aliasDefaultValue != null) ||
                (defaultValue != null && !defaultValue.equals(aliasDefaultValue))) {
            throw new RuntimeException(String.format("Attribute '%s' and its alias '%s' in annotation '%s' must " +
                            "declare same default value.", attribute.getName(), aliasAttribute.getName(),
                    annotation.annotationType().getName()));
        }

        Object attributeValue = evalAttributeValue(annotation, attribute);
        Object aliasAttributeValue = evalAttributeValue(annotation, aliasAttribute);
        if (!attributeValue.equals(defaultValue) && !aliasAttributeValue.equals(aliasDefaultValue)) { // both
            if (!attributeValue.equals(aliasAttributeValue)) { // check supplied value
                throw new RuntimeException(String.format("Attribute '%s' and its alias '%s' in annotation '%s' " +
                                "both declare supplied value, but only one is permitted.", attribute.getName(),
                        aliasAttribute.getName(), annotation.annotationType().getName()));
            }
            else {
                return attributeValue;
            }
        }
        else if (!attributeValue.equals(defaultValue)) { // just attribute
            return attributeValue;
        }
        else if (!aliasAttributeValue.equals(aliasDefaultValue)) { // just alias
            return aliasAttributeValue;
        }
        else {
            return defaultValue;
        }
    }

    private static Object getValueFromAttribute(Annotation annotation, Method attribute) {
        return evalAttributeValue(annotation, attribute);
    }

    private static Method getAttribute(Method[] attributes, String attributeName) {
        for (Method attribute : attributes) {
            if (attribute.getName().equals(attributeName)) {
                return attribute;
            }
        }
        return null;
    }

    private static Object evalAttributeValue(Annotation annotation, Method attribute) {
        try {
            return attribute.invoke(annotation);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return NULL_ATTRIBUTE_VALUE;
    }
}
