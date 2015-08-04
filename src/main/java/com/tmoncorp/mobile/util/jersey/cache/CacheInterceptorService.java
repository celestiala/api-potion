package com.tmoncorp.mobile.util.jersey.cache;

import com.tmoncorp.mobile.util.common.cache.Cache;
import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.InterceptionService;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

@Service
public class CacheInterceptorService implements InterceptionService {

	private static final Logger LOG = LoggerFactory.getLogger(CacheInterceptorService.class);
	private final List<MethodInterceptor> intercepters;
	private final CacheInterceptor intercepter;

	@Inject
	private CacheRepository cacheRepo;

	public CacheInterceptorService() {

		intercepter = new CacheInterceptor(this);
		intercepters = Collections.<MethodInterceptor> singletonList(intercepter);

	}

	@Override
	public List<ConstructorInterceptor> getConstructorInterceptors(Constructor<?> arg0) {

		return null;

	}

	@Override
	public Filter getDescriptorFilter() {
		// TODO Auto-generated method stub
		return new Filter() {
			@Override
			public boolean matches(final Descriptor d) {
				final String clazz = d.getImplementation();
				return clazz.startsWith("com.tmoncorp.mobile.repository")
				        || clazz.startsWith("com.tmoncorp.mobile.resource");
			}
		};
	}

	@Override
	public List<MethodInterceptor> getMethodInterceptors(Method method) {
		if (cacheRepo == null)
			LOG.debug("cacheRepo is null ");
		if (method.isAnnotationPresent(Cache.class))
			return intercepters;
		return null;
	}

	public CacheRepository getCacheRepo() {
		return cacheRepo;
	}

}
