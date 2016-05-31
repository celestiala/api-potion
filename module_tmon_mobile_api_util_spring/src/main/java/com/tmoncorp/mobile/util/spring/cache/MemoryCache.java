package com.tmoncorp.mobile.util.spring.cache;

import com.tmoncorp.mobile.util.common.cache.Cache;
import com.tmoncorp.mobile.util.common.cache.CacheProvider;
import com.tmoncorp.mobile.util.common.cache.LocalCacheRepository;
import com.tmoncorp.mobile.util.spring.async.AsyncService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MemoryCache implements CacheProvider {

    @Autowired
    private AsyncService ayncService;

    @Autowired
    private SpringHttpCacheSupport httpCacheSupport;

    private SpringCacheService innerCacheService;

    public MemoryCache() {

        innerCacheService = new SpringCacheService(new LocalCacheRepository(), run -> ayncService.submitAsync(run));
    }

    @PostConstruct
    public void init(){
        innerCacheService.setHttpCache(httpCacheSupport);
    }

    @Override public void set(String keyName, Object value, Cache cacheInfo) {

        innerCacheService.set(keyName, value, cacheInfo);
    }

    @Override public Object get(Cache cacheInfo, MethodInvocation mi) {
        return innerCacheService.get(cacheInfo, mi);
    }
}
