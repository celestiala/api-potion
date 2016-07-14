package com.tmoncorp.apipotion.jersey.config;

import com.tmoncorp.apipotion.jersey.async.AsyncRunner;
import com.tmoncorp.apipotion.jersey.cache.CacheInterceptorService;
import com.tmoncorp.apipotion.jersey.cache.JerseyMemCacheRepository;
import com.tmoncorp.apipotion.jersey.cache.CacheAsyncRunner;
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
