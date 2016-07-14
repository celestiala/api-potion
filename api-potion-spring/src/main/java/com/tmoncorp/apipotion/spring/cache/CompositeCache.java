package com.tmoncorp.apipotion.spring.cache;

import com.tmoncorp.apipotion.core.cache.Cache;
import com.tmoncorp.apipotion.core.cache.CacheProvider;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Not Tested, Do not Use This Class yet.
 */
public class CompositeCache implements CacheProvider {

    @Autowired
    private MemcacheClient memcacheClient;

    @Autowired
    private MemoryCache memoryCache;

    @Override
    public void set(String keyName, Object value, Cache cacheInfo) {

        memoryCache.set(keyName, value, cacheInfo);
        memcacheClient.set(keyName, value, cacheInfo);

    }

    @Override
    public Object get(Cache cacheInfo, MethodInvocation mi) {
        Object cachedItem = memoryCache.get(cacheInfo, mi);
        if (cachedItem == null)
            cachedItem = memcacheClient.get(cacheInfo, mi);
        if (cachedItem == null)
            return null;

        return cachedItem;
    }
}
