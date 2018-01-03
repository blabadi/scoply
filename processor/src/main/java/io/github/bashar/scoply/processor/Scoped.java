package io.github.bashar.scoply.processor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
public @interface Scoped {
    String pkg() default "*";
}
