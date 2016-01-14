package com.tmoncorp.mobile.util.jersey.cache;

import com.tmoncorp.mobile.util.common.cache.*;
import com.tmoncorp.mobile.util.common.cache.httpcache.HttpCacheSupport;
import com.tmoncorp.mobile.util.common.cache.httpcache.HttpCacheSupportImpl;
import com.tmoncorp.mobile.util.common.cache.httpcache.HttpCacheType;
import com.tmoncorp.mobile.util.common.security.Compress;
import com.tmoncorp.mobile.util.common.security.SecurityUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.EnumMap;

public class JerseyCacheInterceptor extends CacheInterceptor{
	private static final Logger LOG = LoggerFactory.getLogger(JerseyCacheInterceptor.class);

	private final CacheInterceptorService ciService;
	private HttpCacheSupport cacheSupport;

	public JerseyCacheInterceptor(CacheInterceptorService service) {
		super(new EnumMap<>(CacheStorage.class));
		ciService = service;
		cacheSupport=new HttpCacheSupportImpl(service);


	}

	public void init(){
		CacheService memoryCache = new JerseyCacheRepository(new LocalCacheRepository());
		memoryCache.setHttpCache(cacheSupport);

		cacheStorageServiceMap.put(CacheStorage.LOCAL,memoryCache);
		cacheStorageServiceMap.put(CacheStorage.MEMCACHED,ciService.getCacheRepo());
	}

//	private void generateEtag(String keyName, Cache cacheInfo, MethodInvocation mi){
//		if (cacheInfo.browserCache() == HttpCacheType.ETAG){
//			String etag= SecurityUtils.getSHA1String(keyName + cacheInfo.expiration());
//			ciService.getCacheRepo().set("e:"+keyName,etag,cacheInfo);
//			ciService.getCacheRepo().set(etag,"e:"+keyName,cacheInfo);
//			cacheSupport.setEtag(etag);
//		}
//	}
//
//	private void setEtagCache(String keyName){
//		CacheItem etag=(CacheItem)ciService.getCacheRepo().getRaw("e:"+keyName);
//		if (etag != null)
//			cacheSupport.setEtag((String)etag.getValue());
//	}


	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {

		JerseyMemCacheRepository cacheRepo = ciService.getCacheRepo();
		if (cacheRepo == null || cacheRepo.getMode() ==CacheMode.OFF)
			return mi.proceed();

		return super.invoke(mi);
	}

}
