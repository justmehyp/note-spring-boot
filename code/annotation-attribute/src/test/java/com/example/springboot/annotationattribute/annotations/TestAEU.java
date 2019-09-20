package com.example.springboot.annotationattribute.annotations;


import org.springframework.core.annotation.AnnotatedElementUtils;

@Third(secondValue = "hello", thirdValue = "world")
public class TestAEU {

    public static void main(String[] args) {
        First first = AnnotatedElementUtils.getMergedAnnotation(TestAEU.class, First.class);
        Second second = AnnotatedElementUtils.getMergedAnnotation(TestAEU.class, Second.class);
        Third third = AnnotatedElementUtils.getMergedAnnotation(TestAEU.class, Third.class);

        System.out.println(String.format("Expected result of first.firstValue() is 'hello', actual value is '%s'.", first.firstValue()));
        System.out.println(String.format("Expected result of second.secondValue() is 'hello', actual value is '%s'.", second.secondValue()));
        System.out.println(String.format("Expected result of third.secondValue() is 'hello', actual value is '%s'.", third.secondValue()));

        System.out.println(String.format("Expected result of first.anotherFirstValue() is 'world', actual value is '%s'.", first.anotherFirstValue()));
        System.out.println(String.format("Expected result of second.anotherFirstValue() is 'world', actual value is '%s'.", second.anotherFirstValue()));
        System.out.println(String.format("Expected result of third.thirdValue() is 'world', actual value is '%s'.", third.thirdValue()));

    }
}
