package com.tmoncorp.mobile.util.jersey.cache;

import com.tmoncorp.mobile.util.common.cache.Cache;
import com.tmoncorp.mobile.util.common.cache.httpcache.HttpCacheSupport;
import com.tmoncorp.mobile.util.common.cache.httpcache.HttpCacheSupportImpl;
import com.tmoncorp.mobile.util.common.cache.httpcache.HttpServletRequestContainer;
import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.InterceptionService;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

@Service
public class CacheInterceptorService implements InterceptionService, HttpServletRequestContainer {

    private static final Logger LOG = LoggerFactory.getLogger(CacheInterceptorService.class);
    private final List<MethodInterceptor> intercepters;
    private final JerseyCacheInterceptor intercepter;

    @Inject
    private JerseyMemCacheRepository cacheRepo;

    @Context
    private HttpServletRequest request;

    private HttpCacheSupport cacheSupport;

    public CacheInterceptorService() {

        cacheSupport = new HttpCacheSupportImpl(this);
        intercepter = new JerseyCacheInterceptor(this);
        intercepters = Collections.<MethodInterceptor>singletonList(intercepter);

    }

    @PostConstruct
    public void setRepository() {
        LOG.debug("postConstruct called");
        intercepter.init();
    }

    @Override
    public List<ConstructorInterceptor> getConstructorInterceptors(Constructor<?> arg0) {
        return null;
    }

    @Override
    public Filter getDescriptorFilter() {
        return d -> {
            final String clazz = d.getImplementation();
            return clazz.startsWith("com.tmoncorp.mobile.repository")
                    || clazz.startsWith("com.tmoncorp.mobile.resource");
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

    public JerseyMemCacheRepository getCacheRepo() {
        cacheRepo.setHttpCache(cacheSupport);
        return cacheRepo;
    }

    @Override
    public HttpServletRequest getHttpServletRequest() {
        return request;
    }

}
