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
import java.util.Properties;

@Component
public class MemcacheClient implements CacheProvider, CacheRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheProvider.class);
    private static final String MEMCACHE_SERVER_PROPERTY = "memcache.server";
    private static final String ENVIRONMENT_PROPERTY = "deploy.phase";
    private static final String CACHE_PREFIX_PROPERTY = "cache.prefix";
    private static final String CACHE_PREFIX_DEFAULT = "cache";
    private static final String APPLICATION_PROPERTIES = "applicationProperty.properties";
    @Autowired
    private AsyncService ayncService;
    private SpringCacheService innerCacheService;
    private MemCacheRepository memCacheRepository;

    @Autowired
    private SpringHttpCacheSupport httpCacheSupport;

    private boolean isDebug=false;

    public MemcacheClient() {

        try {
            Properties props = PropertiesLoaderUtils.loadAllProperties(APPLICATION_PROPERTIES);
            String buildEnv = props.getProperty(ENVIRONMENT_PROPERTY).substring(0, 2);
            String memcacheUrls = props.getProperty(MEMCACHE_SERVER_PROPERTY);
            String cachePrefix = props.getProperty(CACHE_PREFIX_PROPERTY, CACHE_PREFIX_DEFAULT);

            isDebug=!buildEnv.startsWith("r");

            memCacheRepository = new MemCacheRepository(memcacheUrls, buildEnv, cachePrefix);
            innerCacheService = new SpringCacheService(memCacheRepository, run -> ayncService.submitAsync(run));
        } catch (Exception e) {
            LOGGER.error("memcache client initialize failed : {}", e);
        }
    }

    @PostConstruct
    public void init(){
        innerCacheService.setHttpCache(httpCacheSupport);
        innerCacheService.setSupportInvalidateRequest(isDebug);
    }

    @Override public void set(String keyName, Object value, Cache cacheInfo) {
        innerCacheService.set(keyName, value, cacheInfo);
    }

    @Override public Object get(Cache cacheInfo, MethodInvocation mi) {
        return innerCacheService.get(cacheInfo, mi);
    }

    @Override public void setRaw(String keyName, Object value, int expire) {
        memCacheRepository.setRaw(keyName, value, expire);
    }

    @Override public Object getRaw(String keyName) {
        return memCacheRepository.getRaw(keyName);
    }

    @Override public void removeRaw(String keyName) {
        memCacheRepository.removeRaw(keyName);
    }

    @Override public CacheStorage getStorageType() {
        return CacheStorage.MEMCACHED;
    }

    public String getPrefix() {
        return innerCacheService.getPrefix();
    }

    public void setPrefix(String prefix) {
        innerCacheService.setPrefix(prefix);
    }

    public SpringCacheService getMemcacheService(){
        return innerCacheService;
    }
}
