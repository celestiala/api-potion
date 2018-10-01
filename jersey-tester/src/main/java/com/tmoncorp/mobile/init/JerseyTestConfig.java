package com.celestiala.mobile.init;


import com.celestiala.apipotion.jersey.cache.CacheFilter;
import com.celestiala.apipotion.jersey.config.MobileUtilBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JerseyTestConfig extends ResourceConfig {
	private static final Logger LOG = LoggerFactory.getLogger(JerseyTestConfig.class);

	public JerseyTestConfig() {
		initResources();
		register(CacheFilter.class);
		registerRepositoryBinder();

	}

	private void initResources() {

		packages("com.celestiala.gateway.util.jersey.cache");
		packages("com.celestiala.gateway.resource");
	}


	private void registerRepositoryBinder() {
		register(new RepositoryBinder());
		register(new MobileUtilBinder());
	}
}
