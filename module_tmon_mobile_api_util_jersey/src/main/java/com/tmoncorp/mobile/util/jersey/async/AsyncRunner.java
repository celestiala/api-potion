package com.tmoncorp.mobile.util.jersey.async;

import com.tmoncorp.mobile.util.common.async.AsyncExecutor;
import com.tmoncorp.mobile.util.common.async.AsyncTask;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Singleton
public class AsyncRunner implements ContainerLifecycleListener {

    private static final Logger LOG = LoggerFactory.getLogger(AsyncRunner.class);
    private static AsyncRunner instance;
    private final AsyncExecutor excutor;

    public AsyncRunner() {
        excutor = new AsyncExecutor();
        instance = this;
    }

    public static AsyncRunner getInstance() {
        return instance;
    }

    public <T> Future<T> submitAsync(Callable<T> call) {

        return excutor.submitAsync(call);
    }

    public <T, K> List<T> processAsyncList(List<K> datas, AsyncTask<K, T> call) {

        return excutor.processAsyncList(datas, call);
    }

    public <K, T> List<T> processAsyncMergeList(List<K> datas, AsyncTask<K, List<T>> call) {

        return excutor.processAsyncMergeList(datas, call);
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
