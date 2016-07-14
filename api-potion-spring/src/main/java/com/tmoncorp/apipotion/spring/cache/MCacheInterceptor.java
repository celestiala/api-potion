package com.tmoncorp.apipotion.spring.cache;

import com.tmoncorp.apipotion.core.cache.*;
import com.tmoncorp.apipotion.spring.async.AsyncService;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Properties;

@Component
public class MCacheInterceptor extends CacheInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(MCacheInterceptor.class);
    private static final String ENVIRONMENT_PROPERTY = "deploy.phase";
    private static final String APPLICATION_PROPERTIES = "applicationProperty.properties";

    private CacheMode cacheMode = CacheMode.ON;

    @Autowired
    private MemcacheClient client;

    @Autowired
    private SpringHttpCacheSupport springHttpCacheSupport;

    @Autowired
    private AsyncService ayncService;

    public MCacheInterceptor() {
        super(new EnumMap<>(CacheStorage.class));
    }

    @PostConstruct
    public void init(){
        CacheService memoryCache = new SpringCacheService(new LocalCacheRepository(),run -> ayncService.submitAsync(run));
        memoryCache.setHttpCache(springHttpCacheSupport);

        try {
            Properties props = PropertiesLoaderUtils.loadAllProperties(APPLICATION_PROPERTIES);
            String buildEnv = props.getProperty(ENVIRONMENT_PROPERTY).substring(0, 2);

            boolean isDebug=!buildEnv.startsWith("r");
            memoryCache.setSupportInvalidateRequest(isDebug);

        } catch (Exception e) {
            //LOGGER.error("memcache client initialize failed : {}", e);
        }

        cacheStorageServiceMap.put(CacheStorage.LOCAL, memoryCache);
        cacheStorageServiceMap.put(CacheStorage.MEMCACHED, client.getMemcacheService());
    }

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

        return super.invoke(mi);
    }

}
