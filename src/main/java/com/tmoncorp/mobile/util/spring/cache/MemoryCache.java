package com.tmoncorp.mobile.util.spring.cache;

import com.tmoncorp.mobile.util.common.cache.Cache;
import com.tmoncorp.mobile.util.common.cache.CacheItem;
import com.tmoncorp.mobile.util.common.cache.CacheProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MemoryCache implements CacheProvider {
	private static final Logger LOG = LoggerFactory.getLogger(MemoryCache.class);
	private final ConcurrentHashMap<String, CacheItem> objectCache;

	public MemoryCache() {
		objectCache = new ConcurrentHashMap<>();
	}

	public Object getNotExpiredCache(String keyName) {
		CacheItem cache = objectCache.get(keyName);
		if (cache != null) {
			if (LocalDateTime.now().isAfter(cache.getExpireTime())) {
				objectCache.remove(keyName);
				cache = null;
			} else {
				return cache.getValue();
			}
		}
		return null;
	}

	@Override
	public Object get(String keyName){
		CacheItem cache = objectCache.get(keyName);
		return cache;
	}

	@Override
	public void set(String keyName, Object value, Cache cacheInfo) {
		if (value == null)
			return;
		CacheItem cache = new CacheItem();
		cache.setExpireTime(LocalDateTime.now().plusSeconds(cacheInfo.expiration()));//cacheInfo.expiration()));
		cache.setValue(value);
		objectCache.put(keyName, cache);
	}
}
