package com.tmoncorp.mobile.util.common.cache;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.Map;

public class CacheInterceptor implements MethodInterceptor{


	protected final Map<CacheStorage,CacheService> cacheStorageServiceMap;

	public CacheInterceptor(Map<CacheStorage,CacheService> cacheStorageCacheServiceMap){
		cacheStorageServiceMap=cacheStorageCacheServiceMap;
	}

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {

		Method method = mi.getMethod();
		Cache cacheInfo = method.getAnnotation(Cache.class);
		CacheService service=cacheStorageServiceMap.get(cacheInfo.storage());
		return service.get(cacheInfo,mi);

	}

}
