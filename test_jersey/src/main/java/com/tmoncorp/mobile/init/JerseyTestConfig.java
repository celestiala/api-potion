package com.tmoncorp.mobile.init;


import com.tmoncorp.mobile.util.jersey.cache.CacheFilter;
import com.tmoncorp.mobile.util.jersey.config.MobileUtilBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JerseyTestConfig extends ResourceConfig {
	private static final Logger LOG = LoggerFactory.getLogger(JerseyTestConfig.class);

	public JerseyTestConfig() {
		initResources();
		register(CacheFilter.class);
		registerRepositoryBinder();

	}

	private void initResources() {

		packages("com.tmoncorp.mobile.util.jersey.cache");
		packages("com.tmoncorp.mobile.resource");
	}


	private void registerRepositoryBinder() {
		register(new RepositoryBinder());
		register(new MobileUtilBinder());
	}
}
