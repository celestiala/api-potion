package com.tmoncorp.mobile.util.jersey.cache;

import com.tmoncorp.mobile.util.common.cache.CacheItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryCache {

	private static final Logger LOG = LoggerFactory.getLogger(MemoryCache.class);

	private final ConcurrentHashMap<String, CacheItem> objectCache;

	public MemoryCache() {
		objectCache = new ConcurrentHashMap<>();
	}

	public Object get(String keyName) {
		CacheItem cache = objectCache.get(keyName);
		if (cache != null) {
			if (LocalDateTime.now().isAfter(cache.getExpireTime())) {
				objectCache.remove(keyName);
				LOG.debug("memory cache expired");
				cache = null;
			} else {
				LOG.debug("memory cache not expired {}", cache.getExpireTime());
				return cache.getValue();
			}
		}
		return null;
	}

	public void set(String keyName, Object value, int expire) {
		if (value == null)
			return;
		CacheItem cache = new CacheItem();
		cache.setExpireTime(LocalDateTime.now().plusSeconds(expire));
		cache.setValue(value);
		objectCache.put(keyName, cache);
	}

}
