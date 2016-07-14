package com.tmoncorp.mobile.util.common.async;

import com.tmoncorp.mobile.util.common.clientinfo.ClientInfo;
import com.tmoncorp.mobile.util.common.clientinfo.ClientInfoProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsyncExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(AsyncExecutor.class);

    private static final int THREAD_POOL_MIN_CORE_SIZE = 4;
    private static final int THREAD_POOL_MAX_SIZE = 24;
    private static final int TASK_TIMEOUT = 10 * 1000; //ms
    private static final int TASK_DELAY = 10;
    private ThreadPoolTaskExecutor pool;

    public void init() throws Exception {
        int maxPoolSize = Runtime.getRuntime().availableProcessors();
        int corePoolSize = 0;
        if (maxPoolSize < 1) {
            maxPoolSize = THREAD_POOL_MAX_SIZE;
        }
        corePoolSize = maxPoolSize / 2; //half
        if (corePoolSize < THREAD_POOL_MIN_CORE_SIZE) {
            corePoolSize = THREAD_POOL_MIN_CORE_SIZE;
        }
        pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(corePoolSize);
        pool.setMaxPoolSize(maxPoolSize);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        pool.initialize();
    }

    public void cleanUp() throws Exception {
        if (pool != null) {
            pool.shutdown();
            pool = null;
        }
    }

    public void submitAsync(Runnable run) {
        pool.execute(run);
    }

    public <T> Future<T> submitAsync(Callable<T> call) {
        return pool.submit(call);
    }

    private <T, K> LinkedList<Future<T>> requestAsyncList(List<K> datas, AsyncTask<K, T> call) {
        LinkedList<Future<T>> futureList = new LinkedList<>();

        final ClientInfo info = ClientInfoProvider.getInfo();

        for (K data : datas) {
            Future<T> f = submitAsync(() -> {
                ClientInfoProvider.setInfo(info);
                T result;
                try {
                    result = call.async(data);
                } finally {
                    ClientInfoProvider.clean();
                }
                return result;
            });
            futureList.add(f);
        }
        return futureList;
    }

    private <A> void cancelJobs(LinkedList<Future<A>> futureList){
        futureList.stream().forEach(f->f.cancel(true));
    }

    private <T, A> List<T> getSortedList(LinkedList<Future<A>> futureList, AsyncListAdder<T, A> itemAdder,int timeout) {
        LinkedList<T> list = new LinkedList<>();
        long startTime = System.currentTimeMillis();
        while (!futureList.isEmpty()) {
                if (startTime + timeout < System.currentTimeMillis()) {
                    LOG.debug("timeout");
                    cancelJobs(futureList);
                    break;
                }

                Future<A> m = futureList.getFirst();
                if (m.isDone()) {
                    try {
                        A item = m.get();
                        if (item != null)
                            itemAdder.add(list, item);
                    } catch (InterruptedException e) {
                        cancelJobs(futureList);
                        LOG.debug("Async Task interrupted {} ", e);
                        break;
                    } catch (ExecutionException | RuntimeException e) {
                        LOG.debug("Async Task exception {} ", e);
                        continue;
                    }
                    futureList.remove(m);

                } else {
                    try {
                        Thread.sleep(TASK_DELAY);
                    } catch (InterruptedException e) {
                        cancelJobs(futureList);
                        LOG.debug("Async Task loop interrupted {} ", e);
                        break;
                    }
                }

        }
        return list;
    }

    public <T, K> List<T> processAsyncList(List<K> datas, AsyncTask<K, T> call) {

        return processAsyncList(datas,call,TASK_TIMEOUT);
    }

    public <T, K> List<T> processAsyncList(List<K> datas, AsyncTask<K, T> call, int timeout) {

        LinkedList<Future<T>> futureList = requestAsyncList(datas, call);
        return getSortedList(futureList, (list, item) -> list.add(item),timeout);
    }
    public <K, T> List<T> processAsyncMergeList(List<K> datas, AsyncTask<K, List<T>> call) {
        return processAsyncMergeList(datas,call,TASK_TIMEOUT);
    }

    public <K, T> List<T> processAsyncMergeList(List<K> datas, AsyncTask<K, List<T>> call, int timeout) {

        LinkedList<Future<List<T>>> futureList = requestAsyncList(datas, call);
        return getSortedList(futureList, (list, item) -> list.addAll(item),timeout);
    }

    interface AsyncListAdder<T, A> {
        void add(List<T> list, A item);
    }

}
