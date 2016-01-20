package com.tmoncorp.mobile.util.common.cache;

import com.tmoncorp.mobile.util.common.cache.httpcache.HttpCacheType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface Cache {

	int DEFAULT_EXPIRETIME = 5 * 60; //seconds

	int expiration() default DEFAULT_EXPIRETIME;

	CacheType type() default CacheType.SYNC;

	CacheStorage storage() default CacheStorage.MEMCACHED;

	boolean isPlatformDependent() default true;

	boolean compress() default false;

	boolean setOnError() default true;

	String name() default "";

	HttpCacheType browserCache() default HttpCacheType.EXPIRE_TIME;
}
