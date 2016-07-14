package com.tmoncorp.mobile.init;

import com.tmoncorp.mobile.repository.CacheTestRepository;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class RepositoryBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(CacheTestRepository.class).to(CacheTestRepository.class).in(Singleton.class);
    }
}
