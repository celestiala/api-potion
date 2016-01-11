package com.tmoncorp.mobile.util.jersey.cache;

import com.tmoncorp.mobile.util.common.cache.*;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;

@Singleton
public class CacheRepository implements CacheProvider ,HttpCacheInfoContainer {
	private static final Logger LOG = LoggerFactory.getLogger(CacheRepository.class);

	private static final String MEMCACHE_SERVER_PROPERTY = "memcache.servers";
	private static final String ENVIRONMENT_PROPERTY = "deploy.phase";
	private static final String SPERATOR = ":";
	private final String memcacheUrl;
	private final String environment;
	private MemcachedClient client;
	private static final String MOBILE_GATEWAY_CACHE_KEY = "mg";
	private static final int EXPIRE_TIME_UNIT = 1; //second
	private final boolean isModeSeletable;
	private CacheMode mode=CacheMode.ON;

	private HttpCacheSupport httpCacheSupport;


	public CacheRepository(Properties properties) {
		memcacheUrl = properties.getProperty(MEMCACHE_SERVER_PROPERTY);
		environment = properties.getProperty(ENVIRONMENT_PROPERTY);
		if (environment.trim().startsWith("r"))
			isModeSeletable=false;
		else
			isModeSeletable=true;

		try {
			client = new MemcachedClient(AddrUtil.getAddresses(memcacheUrl));
		} catch (IOException e) {
			LOG.error("{}", e);
		}
	}

	private void setCache(String key, Object value, int expire) {
		if (value == null) {
			return;
		}
		CacheItem cache = new CacheItem();
		cache.setExpireTime(LocalDateTime.now().plusSeconds(expire));
		cache.setValue(value);
		setExpire(cache);
		client.set(key, expire, cache);
	}

	public void set(String key, Object value) {
		set(key, value, Cache.DEFAULT_EXPIRETIME);
	}

	public void set(String key, Object value, int expire) {
		setCache(makeRawKey(key), value, expire);
	}

	public void remove(String key) {
		client.replace(key, EXPIRE_TIME_UNIT, "");
	}

	@Override public void set(String keyName, Object value, Cache cacheInfo) {
		set(keyName,value,cacheInfo.expiration());
	}

	//TODO FIXME support composite expiration
	public Object get(String key) {
		Object value = client.get(makeRawKey(key));
		if (value instanceof CacheItem) {
			CacheItem item = (CacheItem) value;
			if (!LocalDateTime.now().isAfter(item.getExpireTime())) {
				LOG.debug("memcache not expired {}", item.getExpireTime());
				setExpire(item);
				return item.getValue();
			} else {
				LOG.debug("memcache expired");
				return null;
			}
		}
		return value;
	}

	private String makeRawKey(String key) {
		return MOBILE_GATEWAY_CACHE_KEY + SPERATOR + environment + SPERATOR + key;
	}



	public CacheMode getMode(){
		return mode;
	}

	public void setMode(CacheMode mode){
		if (isModeSeletable)
			this.mode=mode;
	}

	public void setHttpCache(HttpCacheSupport register) {
		httpCacheSupport =register;
	}

	public HttpCacheSupport getHttpCache() {
		return httpCacheSupport;
	}

	private void setExpire(CacheItem item){
		getHttpCache().setExpire(item.getExpireTime());
	}
}
