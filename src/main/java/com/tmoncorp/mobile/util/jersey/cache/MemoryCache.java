package com.tmoncorp.mobile.util.jersey.cache;

import com.tmoncorp.mobile.util.common.cache.Cache;
import com.tmoncorp.mobile.util.common.cache.CacheItem;
import com.tmoncorp.mobile.util.common.cache.SyncType;
import com.tmoncorp.mobile.util.jersey.async.AsyncRunner;
import com.tmoncorp.mobile.util.spring.clientinfo.ClientInfoService;
import org.aopalliance.intercept.MethodInvocation;
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

	private void refreshAsync(final String keyName, final Cache cacheinfo,MethodInvocation mi){
		Runnable cacheRequest = new Runnable() {
			@Override
			public void run() {
				try {
					CacheItem cache = objectCache.get(keyName);
					if (cache == null || LocalDateTime.now().isAfter(cache.getExpireTime())) {
						Object result=null;
						try {
							result = mi.proceed();
						} catch (Throwable e){
							if (cacheinfo.setOnError()){
								objectCache.remove(keyName);
								cache=null;
							}
							LOG.error("Fail to make a cache : {}", e);
						}
						if (result != null){
							cache = new CacheItem();
							cache.setExpireTime(LocalDateTime.now().plusSeconds(cacheinfo.expiration()));
							cache.setValue(result);
							objectCache.put(keyName,cache);
						}
					}

				} catch (Throwable e) {
					LOG.debug("Cache set exception {}", e);
				}
			}

		};
		AsyncRunner.getInstance().submitAsync(cacheRequest);

	}

	public Object get(String keyName, Cache cacheinfo,MethodInvocation mi) {
		CacheItem cache = objectCache.get(keyName);
		if (cache != null) {
			if (LocalDateTime.now().isAfter(cache.getExpireTime())) {
				if (cacheinfo.syncType()== SyncType.ASYNC) {
					Object value=cache.getValue();
					refreshAsync(keyName,cacheinfo,mi);
					return value;
				}else {
					objectCache.remove(keyName);
					LOG.debug("memory sync cache expired");
					cache = null;
				}
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
