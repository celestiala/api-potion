package com.tmoncorp.mobile.util.common.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface Cache {
	
	public static final int DEFAULT_EXPIRETIME = 5*60; //seconds
	int expiration() default DEFAULT_EXPIRETIME;
	CacheType type() default CacheType.SYNC;
	boolean isPlatformDependent() default true;
	String name() default "";
}
