package com.example.springboot.annotationattribute;

import java.lang.annotation.Annotation;

class AnnotationHolder {
    Annotation self;
    AnnotationHolder child;

    AnnotationHolder(Annotation self, AnnotationHolder child) {
        this.self = self;
        this.child = child;
    }

    Class<? extends Annotation> annotationType() {
        return self.annotationType();
    }
}
