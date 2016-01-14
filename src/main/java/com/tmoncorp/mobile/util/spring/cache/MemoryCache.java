package com.tmoncorp.mobile.util.spring.cache;

import com.tmoncorp.mobile.util.common.cache.*;
import com.tmoncorp.mobile.util.spring.async.AsyncService;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MemoryCache implements CacheProvider {

	@Autowired
	AsyncService ayncService;

	private SpringCacheService innerCacheService;

	public MemoryCache() {

		innerCacheService=new SpringCacheService(new LocalCacheRepository(),run->ayncService.submitAsync(run));
	}

	@Override public void set(String keyName, Object value, Cache cacheInfo) {

		innerCacheService.set(keyName,value,cacheInfo);
	}

	@Override public Object get(Cache cacheInfo, MethodInvocation mi) {
		return innerCacheService.get(cacheInfo,mi);
	}
}
