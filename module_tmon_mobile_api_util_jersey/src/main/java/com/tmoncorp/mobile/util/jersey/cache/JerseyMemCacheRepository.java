package com.tmoncorp.mobile.util.jersey.cache;

import com.tmoncorp.mobile.util.common.cache.*;
import net.spy.memcached.MemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Properties;

@Singleton
public class JerseyMemCacheRepository extends JerseyCacheRepository {

	private static final String MEMCACHE_SERVER_PROPERTY = "memcache.servers";
	private static final String ENVIRONMENT_PROPERTY = "deploy.phase";
	private static final String MOBILE_GATEWAY_CACHE_KEY = "mg";

	private CacheMode mode=CacheMode.ON;
	private final boolean isModeSeletable;

	public JerseyMemCacheRepository(Properties properties) {
		super(new MemCacheRepository(properties.getProperty(MEMCACHE_SERVER_PROPERTY),
				properties.getProperty(ENVIRONMENT_PROPERTY)
				,MOBILE_GATEWAY_CACHE_KEY));

		if (properties.getProperty(ENVIRONMENT_PROPERTY).trim().startsWith("r"))
			isModeSeletable=false;
		else
			isModeSeletable=true;
	}


	public CacheMode getMode(){
		return mode;
	}

	public void setMode(CacheMode mode){
		if (isModeSeletable)
			this.mode=mode;
	}

	public Object getRaw(String key){
		return cacheRepository.getRaw(key);
	}

}
