package com.celestiala.apipotion.core.cache;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

public class CacheInterceptor implements MethodInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(CacheInterceptor.class);

    protected final Map<CacheStorage, CacheService> cacheStorageServiceMap;

    public CacheInterceptor(Map<CacheStorage, CacheService> cacheStorageCacheServiceMap) {
        cacheStorageServiceMap = cacheStorageCacheServiceMap;
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {

        Method method = mi.getMethod();
        Cache cacheInfo = method.getAnnotation(Cache.class);
        CacheService service = cacheStorageServiceMap.get(cacheInfo.storage());

        LOG.debug("cacheInfo storage : {}", cacheInfo.storage());
        return service.get(cacheInfo, mi);

    }

}
