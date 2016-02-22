package com.tmoncorp.mobile.util.common.cache;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MemcachedClientFactory {

    private static final Logger LOG = LoggerFactory.getLogger(MemcachedClientFactory.class);
    private static final MemcachedClientFactory instance=new MemcachedClientFactory();
    private MemcachedClient client;

    private MemcachedClientFactory(){

    }

    private void createClient(String memcacheUrl){
        try {
            client = new MemcachedClient(new BinaryConnectionFactory(), AddrUtil.getAddresses(memcacheUrl));
        } catch (IOException e) {
            LOG.error("{}", e);
        }
    }

    private MemcachedClient getOrMakeClient(String memcacheUrl){
        if (client == null)
            createClient(memcacheUrl);
        return client;
    }

    private MemcachedClient getInnerClient(){
        return client;
    }

    public static MemcachedClient getClient(String memcacheUrl){
        return instance.getOrMakeClient(memcacheUrl);
    }

    public static MemcachedClient getClient(){
        return instance.getInnerClient();

    }
}
