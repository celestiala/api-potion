package com.celestiala.apipotion.core.cache;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Inherited
@Documented
public @interface CacheParam {
    boolean ignore() default false;
    boolean invalidate() default false;
}
