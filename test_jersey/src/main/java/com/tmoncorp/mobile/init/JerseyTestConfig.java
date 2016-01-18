package com.tmoncorp.mobile.init;


import com.tmoncorp.mobile.util.jersey.cache.CacheFilter;
import com.tmoncorp.mobile.util.jersey.cache.CacheInterceptorBinder;
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

	private Properties loadProperties() {
		LOG.info("initializating backend api sources.");
		InputStream in = getClass().getClassLoader().getResourceAsStream("application.properties");
		Properties p = new Properties();
		try {
			p.load(in);
			LOG.info("Properties: {}", p.toString());
		} catch (IOException e) {
			LOG.error("Properties: loding error", e);
		}

		return p;
	}

	private void initResources() {

		packages("com.tmoncorp.mobile.util.jersey.cache");
		packages("com.tmoncorp.mobile.resource");
	}


	private void registerRepositoryBinder() {
		Properties p = loadProperties();
		register(new RepositoryBinder(p));
		register(new CacheInterceptorBinder());
	}
}
