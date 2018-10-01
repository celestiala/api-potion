package com.celestiala.apipotion.jersey.cache;

import com.celestiala.apipotion.core.cache.CacheMode;
import com.celestiala.apipotion.core.cache.MemCacheRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Properties;

@Singleton
public class JerseyMemCacheRepository extends JerseyCacheRepository {

    private static final String MEMCACHE_SERVER_PROPERTY = "memcache.servers";
    private static final String ENVIRONMENT_PROPERTY = "deploy.phase";
    private static final String MOBILE_GATEWAY_CACHE_KEY = "mg";
    private final boolean isModeSeletable;
    private CacheMode mode = CacheMode.ON;

    @Inject
    public JerseyMemCacheRepository(Properties properties) {
        super(MemCacheRepository.getInstance(properties.getProperty(MEMCACHE_SERVER_PROPERTY),
                properties.getProperty(ENVIRONMENT_PROPERTY)
                , MOBILE_GATEWAY_CACHE_KEY));

        isModeSeletable=properties.getProperty(ENVIRONMENT_PROPERTY).trim().startsWith("r");
    }

    public CacheMode getMode() {
        return mode;
    }

    public void setMode(CacheMode mode) {
        if (isModeSeletable)
            this.mode = mode;
    }

    public Object getRaw(String key) {
        return cacheRepository.getRaw(key);
    }

}
