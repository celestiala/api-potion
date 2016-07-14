package com.tmoncorp.mobile.resource;

import com.tmoncorp.mobile.init.JerseyTestConfig;
import com.tmoncorp.mobile.repository.CacheTestRepository;
import com.tmoncorp.mobile.util.jersey.async.AsyncRunner;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.io.*;

public class CacheTest extends JerseyTest {
    private static final Logger LOG = LoggerFactory.getLogger(CacheTest.class);

    private static final int CACHE_EXPIRE_TIME=4; //seconds
    protected Application configure() {

        MockitoAnnotations.initMocks(this);
        AsyncRunner asyncRunner = new AsyncRunner();
        asyncRunner.onStartup(null);


        JerseyTestConfig jc= new JerseyTestConfig();

        return jc;
    }

    private void waitSeconds(long second){
        try {
            Thread.sleep(second*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void syncCacheCheck(String url,int expireTime){
        String first=target(url).request().get(String.class);
        LOG.debug("first request : {}",first);
        waitSeconds(expireTime/2);
        String second=target(url).request().get(String.class);
        LOG.debug("cached value : {}",second);
        Assert.assertEquals(first,second);
        waitSeconds(expireTime/2+1);
        String third=target(url).request().get(String.class);
        LOG.debug("cache expired value : {}",third);
        Assert.assertNotEquals(first,third);

    }

    private void asyncCacheCheck(String url,int expireTime){
        String first=target(url).request().get(String.class);
        LOG.debug("first request : {}",first);
        waitSeconds(expireTime/2);
        String second=target(url).request().get(String.class);
        LOG.debug("cached value : {}",second);
        Assert.assertEquals(first,second);
        waitSeconds(expireTime/2+1);
        String third=target(url).request().get(String.class);
        LOG.debug("cache expired not yet : {}",third);
        Assert.assertEquals(first,third);
        waitSeconds(1);
        String fourth=target(url).request().get(String.class);
        LOG.debug("cache regenerate : {}",fourth);
        Assert.assertNotEquals(first,fourth);

    }

    private void asyncCacheOnlyCheck(String url,int expireTime){
        String first=target(url).request().get(String.class);
        LOG.debug("first request : {}",first);
        waitSeconds(1);
        Assert.assertEquals("",first);
        String second=target(url).request().get(String.class);
        LOG.debug("cached value : {}",second);
        waitSeconds(expireTime/2);
        String third=target(url).request().get(String.class);
        LOG.debug("cache expired not yet : {}",third);
        Assert.assertEquals(second,third);
        waitSeconds(expireTime/2);
        String fourth=target(url).request().get(String.class);
        LOG.debug("cache expired not yet : {}",fourth);
        Assert.assertEquals(second,fourth);
        waitSeconds(1);
        String last=target(url).request().get(String.class);
        LOG.debug("cache regenerate : {}",last);
        Assert.assertNotEquals(second,last);

    }

    private byte[] getByte(InputStream is){
        byte[] buffer= new byte[4096];
        ByteArrayOutputStream os=new ByteArrayOutputStream();

        try {
            int length;
            while (true){
                length=is.read(buffer);
                if (length > 0) {
                    os.write(buffer, 0, length);
                }else{
                    break;
                }
            };

            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    private void syncCompressCheck(String url,int expireTime){
        byte[] first=getByte((ByteArrayInputStream)target(url).request().get().getEntity());
        LOG.debug("first request : {}",first.length);
        waitSeconds(expireTime/2);
        byte[] second=getByte((ByteArrayInputStream)target(url).request().get().getEntity());
        LOG.debug("cached value : {}",second.length);
        Assert.assertArrayEquals(first,second);
        waitSeconds(expireTime/2+1);
        byte[] third=getByte((ByteArrayInputStream)target(url).request().get().getEntity());
        LOG.debug("cache expired value : {}",third.length);
        Assert.assertNotSame(first,third);

    }

    private void runTestSet(String type){
        syncCacheCheck(type+"/sync/default",CACHE_EXPIRE_TIME);
        syncCacheCheck(type+"/sync/setOnError",CACHE_EXPIRE_TIME);
        syncCacheCheck(type+"/sync/browserCache",CACHE_EXPIRE_TIME);
        syncCompressCheck(type+"/sync/compress",CACHE_EXPIRE_TIME);
    }

    private void runAsyncSet(String type){
        asyncCacheCheck(type+"/async/default",CACHE_EXPIRE_TIME);
        //asyncCacheCheck(type+"/async/setOnError",CACHE_EXPIRE_TIME);
        asyncCacheCheck(type+"/async/browserCache",CACHE_EXPIRE_TIME);
        //syncCompressCheck(type+"/async/compress",CACHE_EXPIRE_TIME);
    }

    private void runAsyncOnlySet(String type){
        asyncCacheOnlyCheck(type+"/asyncOnly/default",CACHE_EXPIRE_TIME);
        //asyncCacheCheck(type+"/asyncOnly/setOnError",CACHE_EXPIRE_TIME);
        asyncCacheOnlyCheck(type+"/asyncOnly/browserCache",CACHE_EXPIRE_TIME);
        //syncCompressCheck(type+"/asyncOnly/compress",CACHE_EXPIRE_TIME);
    }

    @Test
    public void memcacheSync() {
        runTestSet("memcache");
    }

    @Test
    public void memcacheAsync(){
        runAsyncSet("memcache");
    }

    @Test
    public void memcacheAsyncOnly(){
        runAsyncOnlySet("memcache");
    }

    @Test
    public void localSync() {
        runTestSet("local");
    }

    @Test
    public void localAsync() {
        runAsyncSet("local");
    }

    @Test
    public void localAsyncOnly() {
        runAsyncOnlySet("local");
    }



}
