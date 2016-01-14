package com.tmoncorp.mobile.util.jersey.cache;

import org.glassfish.hk2.api.InterceptionService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class CacheInterceptorBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(CacheInterceptorService.class).to(InterceptionService.class).in(Singleton.class);

	}

}
