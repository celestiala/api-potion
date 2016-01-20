package com.tmoncorp.mobile.util.spring.cache;

import com.tmoncorp.mobile.util.common.cache.Cache;
import com.tmoncorp.mobile.util.common.cache.CacheMode;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class MCacheInterceptor implements MethodInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(MCacheInterceptor.class);

    private CacheMode cacheMode = CacheMode.ON;

    @Autowired
    private MemcacheClient client;

    public CacheMode getMode() {
        return cacheMode;
    }

    public void setMode(CacheMode mode) {
        cacheMode = mode;
    }

    public String getPrefix() {
        return client.getPrefix();
    }

    public void setPrefix(String prefix) {
        client.setPrefix(prefix);
    }

    @Override
    public Object invoke(final MethodInvocation mi) throws Throwable {
        if (cacheMode == CacheMode.OFF)
            return mi.proceed();

        Method method = mi.getMethod();
        Cache cacheInfo = method.getAnnotation(Cache.class);
        return client.get(cacheInfo, mi);
    }

}
