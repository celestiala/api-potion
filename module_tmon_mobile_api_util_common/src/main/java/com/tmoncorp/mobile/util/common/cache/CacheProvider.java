package com.tmoncorp.mobile.util.common.cache;

import org.aopalliance.intercept.MethodInvocation;

public interface CacheProvider {

	void set(String keyName, Object value, Cache cacheInfo);
	Object get(Cache cacheInfo,MethodInvocation mi);
}
