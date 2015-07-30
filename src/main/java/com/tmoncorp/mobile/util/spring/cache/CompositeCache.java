package com.tmoncorp.mobile.util.spring.cache;

import com.tmoncorp.mobile.util.common.cache.Cache;
import com.tmoncorp.mobile.util.common.cache.CacheItem;
import com.tmoncorp.mobile.util.common.cache.CacheProvider;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * Not Tested, Do not Use This Class yet.
 *
 */
public class CompositeCache implements CacheProvider {

	@Autowired
	private MemcacheClient memcacheClient;

	@Autowired
	private MemoryCache memoryCache;

	@Override
	public void set(String keyName, Object value, Cache cacheInfo) {

		memoryCache.set(keyName,value,cacheInfo);
		memcacheClient.set(keyName,value,cacheInfo);

	}

	//TODO cache expiration
	@Override
	public Object get(String keyName) {
		Object cachedItem=memoryCache.get(keyName);
		if (cachedItem == null)
			cachedItem=memcacheClient.get(keyName);
		if (cachedItem == null)
			return null;

		if (cachedItem instanceof CacheItem){
			CacheItem item=(CacheItem)cachedItem;

		}

		return cachedItem;
	}
}
