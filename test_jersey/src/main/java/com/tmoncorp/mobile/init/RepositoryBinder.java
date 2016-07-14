package com.tmoncorp.mobile.init;

import com.tmoncorp.mobile.repository.CacheTestRepository;
import com.tmoncorp.mobile.util.jersey.async.AsyncRunner;
import com.tmoncorp.mobile.util.jersey.cache.JerseyMemCacheRepository;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;

import javax.inject.Singleton;
import java.util.Properties;

public class RepositoryBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(CacheTestRepository.class).to(CacheTestRepository.class).in(Singleton.class);
    }
}
