package com.tmoncorp.mobile.util.spring.cache;

import java.lang.reflect.Method;
import com.tmoncorp.mobile.util.common.cache.*;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MCacheInterceptor implements MethodInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(MCacheInterceptor.class);

    private CacheMode cacheMode = CacheMode.ON;

    @Autowired
    private MemcacheClient client;


    public void setMode(CacheMode mode){
        cacheMode=mode;
    }

    public CacheMode getMode(){
        return cacheMode;
    }

    public void setPrefix(String prefix){
        client.setPrefix(prefix);
    }

    public String getPrefix(){
        return client.getPrefix();
    }

    @Override
    public Object invoke(final MethodInvocation mi) throws Throwable {
        if (cacheMode==CacheMode.OFF)
            return mi.proceed();
        
        Method method=mi.getMethod();
        Cache cacheInfo=method.getAnnotation(Cache.class);
        return client.get(cacheInfo,mi);
    }
    
}
