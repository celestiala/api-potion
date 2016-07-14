package com.tmoncorp.mobile.util.jersey.config;

import com.tmoncorp.mobile.util.jersey.async.AsyncRunner;
import com.tmoncorp.mobile.util.jersey.cache.CacheAsyncRunner;
import com.tmoncorp.mobile.util.jersey.cache.CacheInterceptorService;
import com.tmoncorp.mobile.util.jersey.cache.JerseyMemCacheRepository;
import org.glassfish.hk2.api.InterceptionService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;

import javax.inject.Singleton;
import java.util.Properties;

public class MobileUtilBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bindFactory(PropertyFactory.class).to(Properties.class).in(Singleton.class);
        bind(AsyncRunner.class).to(ContainerLifecycleListener.class).in(Singleton.class);
        bind(CacheAsyncRunner.class).to(ContainerLifecycleListener.class).in(Singleton.class);
        bind(CacheInterceptorService.class).to(InterceptionService.class).in(Singleton.class);
        bindAsContract(JerseyMemCacheRepository.class).in(Singleton.class);
    }

}
