package com.tmoncorp.mobile.util.common.cache;

import com.tmoncorp.mobile.util.common.cache.httpcache.HttpCacheType;

import java.lang.annotation.*;

/**
 * This annotation is to be used on your methods.
 *
 * Methods return cached value or method invoke result.
 * Parameters and return object should implements Serializable interface
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface Cache {

    /**
     * Default Expiretime is 5 minutes
     */
    int DEFAULT_EXPIRETIME = 5 * 60; //seconds

    /**
     * Expire time
     * @return Expire Time in seconds, default 5 minutes
     */
    int expiration() default DEFAULT_EXPIRETIME;

    /**
     * CacheType : Sync / Async / AsyncOnly
     * AsyncOnly type will return only if cache exist
     * @return
     */
    CacheType type() default CacheType.SYNC;

    /**
     * Storage Type for cache storage
     * Local Memory and Memcached
     * @return
     */
    CacheStorage storage() default CacheStorage.MEMCACHED;

    /**
     * Platform dependent
     * @return
     */
    boolean isPlatformDependent() default true;

    /**
     * Gzip compress option
     * @return
     */
    boolean compress() default false;

    /**
     * Set cache on error or not
     * ASYNC, ASYNC_ONLY
     * @return
     */
    boolean setOnError() default true;

    /**
     * Use user defined cache name
     * @return
     */
    String name() default "";

    /**
     * HTTP Cache header types
     * @return
     */
    HttpCacheType browserCache() default HttpCacheType.EXPIRE_TIME;

    /**
     * Sets Cache Invalidate mode
     * CacheParam should set if you want invalidate cache
     * @return
     */
    Invalidate invalidate() default Invalidate.OFF;
}
