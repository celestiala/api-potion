package com.tmoncorp.mobile.util.jersey.cache;

import com.tmoncorp.mobile.util.common.async.AsyncExecutor;
import com.tmoncorp.mobile.util.common.async.AsyncTask;
import com.tmoncorp.mobile.util.common.async.AsyncWorker;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Singleton
public class CacheAsyncRunner implements ContainerLifecycleListener, AsyncWorker {

    private static final Logger LOG = LoggerFactory.getLogger(CacheAsyncRunner.class);
    private static CacheAsyncRunner instance;
    private final AsyncExecutor excutor;

    public CacheAsyncRunner() {
        excutor = new AsyncExecutor();
        instance = this;
    }

    public static CacheAsyncRunner getInstance() {
        return instance;
    }

    @Override
    public void submitAsync(Runnable run) {
        excutor.submitAsync(run);
    }

    @Override
    public void onStartup(Container container) {
        try {
            excutor.init();
        } catch (Exception e) {
            LOG.error("", e);
        }
    }

    @Override
    public void onReload(Container container) {
        //do nothing
    }

    @Override public void onShutdown(Container container) {
        try {
            excutor.cleanUp();
        } catch (Exception e) {
            LOG.error("", e);
        }
    }
}
