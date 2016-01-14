package com.tmoncorp.mobile.util.common.cache;

import java.util.concurrent.ConcurrentHashMap;

public class LocalCacheRepository implements CacheRepository{

	private final ConcurrentHashMap<String, Object> objectCache;

	public LocalCacheRepository() {
		objectCache = new ConcurrentHashMap<>();
	}


	@Override public void setRaw(String keyName, Object value, int expire) {
		objectCache.put(keyName,value);
	}

	@Override public Object getRaw(String keyName) {
		return objectCache.get(keyName);
	}

	@Override public void removeRaw(String keyName) {
		objectCache.remove(keyName);
	}

	@Override public CacheStorage getStorageType() {
		return CacheStorage.LOCAL;
	}

}
