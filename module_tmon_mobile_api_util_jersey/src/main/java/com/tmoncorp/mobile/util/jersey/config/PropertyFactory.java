package com.tmoncorp.mobile.util.jersey.config;


import org.glassfish.hk2.api.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertyFactory implements Factory<Properties> {

    private Properties properties;

    private static final Logger LOG = LoggerFactory.getLogger(PropertyFactory.class);

    public PropertyFactory(){
        InputStream in = getClass().getClassLoader().getResourceAsStream("application.properties");
        properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            LOG.error("Properties: loading error", e);
        }
    }

    @Override
    public Properties provide() {
        return properties;
    }

    @Override
    public void dispose(Properties instance) {

    }
}
