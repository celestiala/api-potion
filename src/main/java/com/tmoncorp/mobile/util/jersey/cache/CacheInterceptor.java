package com.tmoncorp.mobile.util.jersey.cache;

import com.tmoncorp.mobile.util.common.cache.Cache;
import com.tmoncorp.mobile.util.common.cache.CacheMode;
import com.tmoncorp.mobile.util.common.cache.CacheType;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class CacheInterceptor implements MethodInterceptor {
	private static final Logger LOG = LoggerFactory.getLogger(CacheInterceptor.class);

	private static final String KEY_SEPERATOR = ":";
	private static final int LONG_KEY = 100;

	private final CacheInterceptorService ciService;
	private final MemoryCache memoryCache;

	public CacheInterceptor(CacheInterceptorService service) {
		ciService = service;
		memoryCache = new MemoryCache();
	}

	private String makeKeyName(MethodInvocation invo) {
		Method method = invo.getMethod();

		StringBuilder cb = new StringBuilder();
		cb.append(method.getDeclaringClass().getSimpleName());
		cb.append(KEY_SEPERATOR);
		cb.append(method.getName());

		StringBuilder cp = new StringBuilder();
		for (Object param : invo.getArguments()) {
			cp.append(KEY_SEPERATOR);
			cp.append(param);
		}
		if (cp.length() < LONG_KEY)
			cb.append(cp);
		else {
			cb.append(getHashKey(cp));
		}
		return cb.toString();
	}

	private String getHashKey(CharSequence keyName) {
		CharBuffer buffer = CharBuffer.wrap(keyName);
		Charset charset = StandardCharsets.UTF_16;
		CharsetEncoder encoder = charset.newEncoder();

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] byteData = md.digest(encoder.encode(buffer).array());

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				String hex = Integer.toHexString(0xff & byteData[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception e) {
			LOG.error("", e);
			return null;
		}
	}

	private Object getMemcache(Cache cacheInfo, MethodInvocation mi) throws Throwable {
		CacheRepository cacheRepo = ciService.getCacheRepo();
		if (cacheRepo == null)
			return mi.proceed();

		String keyName = makeKeyName(mi);
		Object response;
		response = cacheRepo.get(keyName);
		if (response != null)
			return response;

		int expire = cacheInfo.expiration();
		response = mi.proceed();
		cacheRepo.set(keyName, response, expire);
		return response;
	}

	private Object getMemoryCache(Cache cacheInfo, MethodInvocation mi) throws Throwable {
		String keyName = makeKeyName(mi);
		Object cache = memoryCache.get(keyName);
		if (cache != null) {
			return cache;
		}
		cache = mi.proceed();
		memoryCache.set(keyName, cache, cacheInfo.expiration());
		return cache;
	}

	private Object getCompositeCache(Cache cacheInfo, MethodInvocation mi) throws Throwable {
		String keyName = makeKeyName(mi);
		Object cache = memoryCache.get(keyName);
		if (cache != null) {
			return cache;
		}
		cache = getMemcache(cacheInfo, mi);
		memoryCache.set(keyName, cache, cacheInfo.expiration());
		return cache;
	}

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		CacheRepository cacheRepo = ciService.getCacheRepo();
		if (cacheRepo == null || cacheRepo.getMode() ==CacheMode.OFF)
			return mi.proceed();

		Method method = mi.getMethod();
		Cache cacheInfo = method.getAnnotation(Cache.class);
		if (cacheInfo.type() == CacheType.MEMORY) {
			return getMemoryCache(cacheInfo, mi);
		} else if (cacheInfo.type() == CacheType.COMPOSITE) {
			return getCompositeCache(cacheInfo, mi);
		}

		return getMemcache(cacheInfo, mi);
	}
}
