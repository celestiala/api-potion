package com.tmoncorp.mobile.util.spring.cache;

import java.lang.reflect.Method;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.tmoncorp.mobile.util.common.cache.*;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.tmoncorp.mobile.util.common.clientinfo.ClientInfo;
import com.tmoncorp.mobile.util.spring.clientinfo.ClientInfoService;

@Component
public class MCacheInterceptor implements MethodInterceptor {

	//private static final String PARAM_REGEN="regen";
	private static final String KEY_SEPERATOR=":";
	private static final Logger LOG = LoggerFactory.getLogger(MCacheInterceptor.class);
	private static final int LONG_KEY=100;
	
	private final static int THREAD_POOL_CORE_SIZE=6;
	private final static int THREAD_POOL_MAX_SIZE=12;
	//private final static int INNER_QUEUE_SIZE=5000;
	private CacheMode cacheMode = CacheMode.ON;
	private String cachePrefix="";
	private ThreadPoolTaskExecutor pool;

	@Autowired
	private MemoryCache memoryCache;

	@Autowired
	private MemcacheClient client;

	@PostConstruct
	public void init() throws Exception {
		pool=new ThreadPoolTaskExecutor();

		int maxPoolSize=Runtime.getRuntime().availableProcessors();
		int corePoolSize=THREAD_POOL_CORE_SIZE;
		if (maxPoolSize < 1){
			maxPoolSize=THREAD_POOL_MAX_SIZE;
		}
		corePoolSize=maxPoolSize/2;

		pool.setCorePoolSize(corePoolSize);
		pool.setMaxPoolSize(maxPoolSize);
		pool.setWaitForTasksToCompleteOnShutdown(true);
		//pool.setQueueCapacity(INNER_QUEUE_SIZE);
		pool.initialize();
	}
	
	@PreDestroy
	public void cleanUp() throws Exception {
		if (pool !=null){
			pool.shutdown();
			pool=null;
		}
	}

	public void setMode(CacheMode mode){
		cacheMode=mode;
	}

	public CacheMode getMode(){
		return cacheMode;
	}

	public void setPrefix(String prefix){

		if (prefix == null) {
			cachePrefix = "";
			return;
		}

		int length=prefix.length();
		if (length > 5){
			cachePrefix=prefix.substring(0,5)+":";
		}else if (length == 0){
			cachePrefix="";
		}
		else{
			cachePrefix=prefix+":";
		}
	}

	public String getPrefix(){
		return cachePrefix;
	}
	
	private String makeKeyName(MethodInvocation invo,boolean isPlatformDependent){
		
		Method method=invo.getMethod();
		
		StringBuilder cb=new StringBuilder();
		cb.append(cachePrefix+method.getDeclaringClass().getSimpleName());
		cb.append(KEY_SEPERATOR);
		cb.append(method.getName());
		
		StringBuilder cp=new StringBuilder();
		if (isPlatformDependent){
			cp.append(KEY_SEPERATOR);
			cp.append(ClientInfoService.getInfo().getPlatform());
		}
		for (Object param:invo.getArguments()){
			cp.append(KEY_SEPERATOR);
			cp.append(param);
		}
		if (cp.length() < LONG_KEY)
			cb.append(cp);
		else{
	        cb.append(getHashKey(cp));
		}
		return cb.toString();
	}
	
	private String getHashKey(CharSequence keyName){
		CharBuffer buffer=CharBuffer.wrap(keyName);
		Charset charset = StandardCharsets.UTF_16;
		CharsetEncoder encoder = charset.newEncoder();
		
		try {
            MessageDigest md=MessageDigest.getInstance("MD5");
            byte[] byteData = md.digest(encoder.encode(buffer).array());
            
            StringBuffer hexString = new StringBuffer();
        	for (int i=0;i<byteData.length;i++) {
        		String hex=Integer.toHexString(0xff & byteData[i]);
       	     	if(hex.length()==1) hexString.append('0');
       	     	hexString.append(hex);
        	}
        	return hexString.toString();
        } catch (Exception e) {
            return null;
        }
	}
	
	private boolean hasExpireCache(String keyName, CacheProvider cacheProvider){
		Object response=cacheProvider.get(keyName);
		if (response != null && response instanceof CacheItem){
			return isExpiredCache((CacheItem) response);
		}
		return true;
	}

	private boolean isExpiredCache(CacheItem item){
		return LocalDateTime.now().isAfter(item.getExpireTime());
	}

	private Runnable makeAsyncCacheItem(final MethodInvocation mi, final String keyName, final Cache cacheInfo, final CacheProvider cacheProvider){
		final ClientInfo info=ClientInfoService.getInfo();
		return new Runnable(){
			@Override
			public void run(){

				ClientInfoService.setInfo(info);
				try {
					if (hasExpireCache(keyName, cacheProvider))
						cacheProvider.set(keyName, mi.proceed(),cacheInfo);
				} catch (Throwable e) {
					LOG.debug("Cache set exception {}",e);
				} finally {
					ClientInfoService.clean();
				}
			}

		};
	}

	private Object refreshAsyncCache(Object response, final MethodInvocation mi, String keyName, Cache cacheInfo,final CacheProvider cacheProvider){
		if (response instanceof CacheItem){
			final CacheItem item=(CacheItem) response;
			if (isExpiredCache(item)){
				pool.execute(makeAsyncCacheItem(mi, keyName, cacheInfo,cacheProvider));
			}
			return item.getValue();

		}
		return response;
	}

	private Object makeSyncCache(final MethodInvocation mi,String keyName,Cache cacheInfo) throws Throwable{
		Object response=mi.proceed();
		if (response !=null){
			client.set(keyName, response,cacheInfo);
		}
		return response;
	}

	private Object getSyncCache(Object response, final MethodInvocation mi,String keyName,Cache cacheInfo) throws Throwable{
		if (response ==null)
			return makeSyncCache(mi,keyName,cacheInfo);

		if (response instanceof CacheItem){
			final CacheItem item=(CacheItem) response;
			return item.getValue();
		}
		return response;

	}


	@Override
    public Object invoke(final MethodInvocation mi) throws Throwable {

		if (cacheMode==CacheMode.OFF)
			return mi.proceed();
		
		Method method=mi.getMethod();
		Cache cacheInfo=method.getAnnotation(Cache.class);
		
		String keyName=makeKeyName(mi, cacheInfo.isPlatformDependent());
		LOG.debug("cache key {}", keyName);
		if (keyName.isEmpty())
			return mi.proceed();
		Object response;

		CacheType cacheType=cacheInfo.type();

		if (cacheType == CacheType.SYNC) {
			response=client.get(keyName);
			return getSyncCache(response, mi, keyName, cacheInfo);
		}

		if (cacheType == CacheType.ASYNC){
			response=client.get(keyName);
			if (response == null)
				return getSyncCache(response, mi,keyName, cacheInfo);
			return refreshAsyncCache(response, mi, keyName, cacheInfo,client);
		}
		if (cacheType == CacheType.ASYNC_ONLY){
			response=memoryCache.get(keyName);
			if (response == null){
				memoryCache.set(keyName, EmptyCache.getInstance(), cacheInfo);
				pool.execute(makeAsyncCacheItem(mi, keyName, cacheInfo,memoryCache));
				return null;
			}else if (response instanceof EmptyCache)
				return null;
			return refreshAsyncCache(response, mi, keyName, cacheInfo, memoryCache);
		}

		response=client.get(keyName);
		return getSyncCache(response, mi,keyName,cacheInfo);

    }
	
}
