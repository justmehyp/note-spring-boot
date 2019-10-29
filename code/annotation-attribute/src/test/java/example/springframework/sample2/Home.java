package example.springframework.sample2;

import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Child("Jack")
public class Home {

    public static void main(String[] args) {
        System.out.println(AnnotatedElementUtils.getMergedAnnotation(Home.class, Child.class).name()); // Jack
        System.out.println(AnnotatedElementUtils.getMergedAnnotation(Home.class, Child.class).value()); // Jack
    }
}

@Retention(RetentionPolicy.RUNTIME)
@interface Parent {
    String name() default "John";
}

@Retention(RetentionPolicy.RUNTIME)
@Parent
@interface Child {

    @AliasFor("value")
    String name() default "";

    @AliasFor("name")
    String value() default "";
}