package com.tmoncorp.mobile.util.common.cache;

public interface CacheProvider {

	void set(String keyName, Object value, Cache cacheInfo);
	Object get(String keyName);
}
