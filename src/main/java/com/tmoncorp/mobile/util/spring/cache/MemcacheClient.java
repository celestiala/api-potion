package com.tmoncorp.mobile.util.spring.cache;

import java.time.LocalDateTime;
import java.util.Properties;

import com.tmoncorp.mobile.util.common.cache.Cache;
import com.tmoncorp.mobile.util.common.cache.CacheItem;
import com.tmoncorp.mobile.util.common.cache.CacheProvider;
import com.tmoncorp.mobile.util.common.cache.CacheType;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;

@Component
public class MemcacheClient implements CacheProvider {
		
	
	private static final String MEMCACHE_SERVER_PROPERTY="memcache.server";
	private static final String ENVIRONMENT_PROPERTY="deploy.phase";
	private static final String APPLICATION_PROPERTIES="applicationProperty.properties";
	private static final String SPERATOR=":";
	private String buildEnv;
	private String memcacheUrls;
	private MemcachedClient client;
    private static final String MART_API_CACHE_KEY="deal";
    private static final int EXPIRE_TIME_UNIT= 1; //second
    
    public MemcacheClient(){
  
		try {
		  	Properties props = PropertiesLoaderUtils.loadAllProperties(APPLICATION_PROPERTIES);
		  	buildEnv=props.getProperty(ENVIRONMENT_PROPERTY).substring(0, 2);
			memcacheUrls=props.getProperty(MEMCACHE_SERVER_PROPERTY);
		    client=new MemcachedClient(new BinaryConnectionFactory(),AddrUtil.getAddresses(memcacheUrls));
        } catch (Exception e) {
	        e.printStackTrace();
        }
	}
	
	private void setCache(String key,Object value,int expire){
		try{
		client.set(key,expire,value);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	

	@Override
	public void set(String key,Object value,Cache cacheInfo){
		CacheItem item=new CacheItem();
		item.setExpireTime(LocalDateTime.now().plusSeconds(cacheInfo.expiration()));
		item.setValue(value);
		int realExpireTime=cacheInfo.expiration();
		if (cacheInfo.type()== CacheType.ASYNC)
			realExpireTime=realExpireTime*2;
		else if (cacheInfo.type() == CacheType.ASYNC_ONLY)
			realExpireTime=60*60*24;
		setCache(makeRawKey(key), item,realExpireTime);
	}
	
	public void remove(String key){
		client.replace(key, EXPIRE_TIME_UNIT, "");
	}

	@Override
	public Object get(String key){
		Object value =client.get(makeRawKey(key));
		return value;
	}
	
	private String makeRawKey(String key){
		return MART_API_CACHE_KEY + SPERATOR+buildEnv+SPERATOR+key;
	}
	
		
	
}
