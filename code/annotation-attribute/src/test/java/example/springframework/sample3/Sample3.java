package example.springframework.sample3;

import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@C
public class Sample3 {

    public static void main(String[] args) {
        System.out.println(AnnotatedElementUtils.getMergedAnnotation(Sample3.class, A.class).a1()); // 2
        System.out.println(AnnotatedElementUtils.getMergedAnnotation(Sample3.class, A.class).a2()); // 2

        System.out.println(AnnotatedElementUtils.getMergedAnnotation(Sample3.class, B.class).a1()); // 3
        System.out.println(AnnotatedElementUtils.getMergedAnnotation(Sample3.class, B.class).b()); // 3

        System.out.println(AnnotatedElementUtils.getMergedAnnotation(Sample3.class, C.class).c()); // 3
        System.out.println(AnnotatedElementUtils.getMergedAnnotation(Sample3.class, C.class).b()); // 3
    }
}


@Retention(RetentionPolicy.RUNTIME)
@interface A {
    String a1() default "1";
    String a2() default "1";
}

@Retention(RetentionPolicy.RUNTIME)
@A
@interface B {

    String a1() default "2";

    @AliasFor(value = "a2", annotation = A.class)
    String b() default "2";
}

@Retention(RetentionPolicy.RUNTIME)
@B
@interface C {

    @AliasFor(value = "a1", annotation = B.class)
    String c() default "3";

    String b() default "3";
}