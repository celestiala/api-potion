package com.tmoncorp.mobile.util.common.cache;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class LocalCacheRepository implements CacheRepository {

    private final ConcurrentHashMap<String, LocalCacheItem> objectCache;

    public LocalCacheRepository() {
        objectCache = new ConcurrentHashMap<>();
    }

    @Override public void setRaw(String keyName, Object value, int expire) {
        LocalCacheItem item = new LocalCacheItem();
        item.setItem(value);
        item.setExpire(LocalDateTime.now().plusSeconds(expire));
        objectCache.put(keyName, item);
    }

    @Override public Object getRaw(String keyName) {
        LocalCacheItem item = objectCache.get(keyName);
        if (item == null)
            return null;

        if (item.getExpire().isAfter(LocalDateTime.now())) {
            return item.getItem();
        } else {
            // Object Cache remove : objectCache.remove(keyName);
            return null;
        }

    }

    @Override public void removeRaw(String keyName) {
        objectCache.remove(keyName);
    }

    @Override public CacheStorage getStorageType() {
        return CacheStorage.LOCAL;
    }

    static class LocalCacheItem {
        private Object item;
        private LocalDateTime expire;

        public Object getItem() {
            return item;
        }

        public void setItem(Object item) {
            this.item = item;
        }

        public LocalDateTime getExpire() {
            return expire;
        }

        public void setExpire(LocalDateTime expire) {
            this.expire = expire;
        }
    }

}
