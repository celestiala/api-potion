package com.tmoncorp.mobile.util.spring.cache;

import java.lang.reflect.Method;

import com.tmoncorp.mobile.util.common.cache.*;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MCacheInterceptor implements MethodInterceptor {

	private static final Logger LOG = LoggerFactory.getLogger(MCacheInterceptor.class);

	private CacheMode cacheMode = CacheMode.ON;

	@Autowired
	private MemcacheClient client;


	public void setMode(CacheMode mode){
		cacheMode=mode;
	}

	public CacheMode getMode(){
		return cacheMode;
	}

	public void setPrefix(String prefix){
		client.setPrefix(prefix);
	}

	public String getPrefix(){
		return client.getPrefix();
	}

		@Override
    public Object invoke(final MethodInvocation mi) throws Throwable {

		if (cacheMode==CacheMode.OFF)
			return mi.proceed();
		
		Method method=mi.getMethod();
		Cache cacheInfo=method.getAnnotation(Cache.class);

		Object response;

		CacheType cacheType=cacheInfo.type();

		return client.get(cacheInfo,mi);


//		if (cacheType == CacheType.SYNC) {
//			response=client.getRaw(keyName);
//			return getSyncCache(response, mi, keyName, cacheInfo);
//		}
//
//		if (cacheType == CacheType.ASYNC){
//			response=client.get(keyName,cacheInfo,mi);
//			if (response == null)
//				return getSyncCache(response, mi,keyName, cacheInfo);
//		}
//		if (cacheType == CacheType.ASYNC_ONLY){
//			response=memoryCache.get(keyName);
//			if (response == null){
//				memoryCache.set(keyName, EmptyCache.getInstance(), cacheInfo);
//				pool.execute(makeAsyncCacheItem(mi, keyName, cacheInfo,memoryCache));
//				return null;
//			}else if (response instanceof EmptyCache)
//				return null;
//			response=refreshAsyncCache(response, mi, keyName, cacheInfo, memoryCache);
//			if (response instanceof EmptyCache)
//				return null;
//			else
//				return response;
//		}
//
//		response=client.get(keyName);
//		return getSyncCache(response, mi,keyName,cacheInfo);

    }
	
}
