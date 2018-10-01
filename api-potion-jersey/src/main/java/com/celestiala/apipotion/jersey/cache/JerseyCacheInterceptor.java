package com.celestiala.apipotion.jersey.cache;

import com.celestiala.apipotion.core.cache.*;
import com.celestiala.apipotion.core.cache.httpcache.HttpCacheSupport;
import com.celestiala.apipotion.core.cache.httpcache.HttpCacheSupportImpl;
import org.aopalliance.intercept.MethodInvocation;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.EnumMap;

public class JerseyCacheInterceptor extends CacheInterceptor {

    private final CacheInterceptorService ciService;
    private HttpCacheSupport cacheSupport;

    public JerseyCacheInterceptor(CacheInterceptorService service) {
        super(new EnumMap<>(CacheStorage.class));
        ciService = service;
        cacheSupport = new HttpCacheSupportImpl(service);

    }

    public void init() {
        CacheService memoryCache = new JerseyCacheRepository(new LocalCacheRepository());
        memoryCache.setHttpCache(cacheSupport);
        memoryCache.setSupportInvalidateRequest(ciService.isDebugMode());

        cacheStorageServiceMap.put(CacheStorage.LOCAL, memoryCache);
        cacheStorageServiceMap.put(CacheStorage.MEMCACHED, ciService.getCacheRepo());
    }


    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {

        JerseyMemCacheRepository cacheRepo = ciService.getCacheRepo();
        if (cacheRepo == null || cacheRepo.getMode() == CacheMode.OFF)
            return mi.proceed();

        Object result=super.invoke(mi);
        Method method = mi.getMethod();
        Cache cacheInfo = method.getAnnotation(Cache.class);
        if (cacheInfo.compress() && result != null){
            Response.ResponseBuilder builder=Response.ok();
            builder.header(HttpHeaders.CONTENT_ENCODING,"gzip");
            builder.entity(result);
            return builder.build();
        }

        return result;
    }

}
