package example.springframework.sample1;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Child
public class Home {

    public static void main(String[] args) {
        System.out.println(AnnotatedElementUtils.getMergedAnnotation(Home.class, Child.class).name()); // Jack
        System.out.println(AnnotatedElementUtils.getMergedAnnotation(Home.class, Parent.class).name()); // Jack
    }
}

@Retention(RetentionPolicy.RUNTIME)
@interface Parent {
    String name() default "John";
}

@Retention(RetentionPolicy.RUNTIME)
@Parent
@interface Child {
    String name() default "Jack";
}
