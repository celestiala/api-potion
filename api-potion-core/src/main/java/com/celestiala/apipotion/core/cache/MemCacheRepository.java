package com.celestiala.apipotion.core.cache;

import net.spy.memcached.MemcachedClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemCacheRepository implements CacheRepository {
    private static final Logger LOG = LoggerFactory.getLogger(MemCacheRepository.class);

    private static final String SPERATOR = ":";
    private static final int EXPIRE_TIME_UNIT = 1; //second
    private final String environment;
    private final String serverCacheKey;
    private MemcachedClient client;

    private MemCacheRepository(String memcacheUrl, String environment, String serverCacheKey) {
        this.environment = environment;
        this.serverCacheKey = serverCacheKey;
        client=MemcachedClientFactory.getClient(memcacheUrl);
    }

    public void remove(String key) {
        client.replace(key, EXPIRE_TIME_UNIT, "");
    }

    @Override public void setRaw(String keyName, Object value, int expire) {
        try {
            client.set(makeRawKey(keyName), expire, value);
        } catch (Exception e) {
            LOG.error("ERROR on set a cache {}", e);
        }
    }

    @Override public void removeRaw(String keyName) {
        client.delete(makeRawKey(keyName));
    }

    @Override public CacheStorage getStorageType() {
        return CacheStorage.MEMCACHED;
    }

    @Override
    public Object getRaw(String key) {
        return client.get(makeRawKey(key));
    }

    private String makeRawKey(String key) {
        return serverCacheKey + SPERATOR + environment + SPERATOR + key;
    }

    public static CacheRepository getInstance(String memcacheUrl, String environment, String serverCacheKey){
        if (StringUtils.isEmpty(memcacheUrl))
            return new DummyCacheRepository(CacheStorage.MEMCACHED);
        return new MemCacheRepository(memcacheUrl, environment, serverCacheKey);
    }

}