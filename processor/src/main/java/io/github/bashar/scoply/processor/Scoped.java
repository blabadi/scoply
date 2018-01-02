package io.github.bashar.scoply.processor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface Scoped {
    String pkg() default "*";
}
