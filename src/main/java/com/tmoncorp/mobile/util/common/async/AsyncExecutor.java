package com.tmoncorp.mobile.util.common.async;

import com.tmoncorp.mobile.util.common.clientinfo.ClientInfo;
import com.tmoncorp.mobile.util.spring.clientinfo.ClientInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsyncExecutor {
	
	private static final Logger LOG = LoggerFactory.getLogger(AsyncExecutor.class);
	
	private static final int THREAD_POOL_MIN_CORE_SIZE=2;
	private static final int THREAD_POOL_MAX_SIZE=12;
	private static final int TASK_TIMEOUT=240*1000; //ms
	private static final int TASK_DELAY=50;
	private ThreadPoolTaskExecutor pool;
	
	public void init() throws Exception {
		int maxPoolSize=Runtime.getRuntime().availableProcessors();
		int corePoolSize=0;
		if (maxPoolSize < 1){
			maxPoolSize=THREAD_POOL_MAX_SIZE;
		}
		corePoolSize=maxPoolSize/2; //half
		if (corePoolSize < THREAD_POOL_MIN_CORE_SIZE){
			corePoolSize=THREAD_POOL_MIN_CORE_SIZE;
		}
		pool=new ThreadPoolTaskExecutor();
		pool.setCorePoolSize(corePoolSize);
		pool.setMaxPoolSize(maxPoolSize);
		pool.setWaitForTasksToCompleteOnShutdown(true);
		pool.initialize();
	}
	
	public void cleanUp() throws Exception {
		if (pool !=null){
			pool.shutdown();
			pool=null;
		}
	}
	
	public <T> Future<T> submitAsync(Callable<T> call){
		return pool.submit(call);
	}

	private <T,K> Vector<Future<T>> requestAsyncList(List<K> datas,AsyncTask<K,T> call){
		Vector<Future<T>> futureList=new Vector<>();

		final ClientInfo info=ClientInfoService.getInfo();

		for (K data : datas) {

			Future<T> f=submitAsync(new Callable<T>(){
				@Override
				public T call() throws Exception {
					ClientInfoService.setInfo(info);
					T result=call.async(data);
					ClientInfoService.clean();
					return result;
				}});
			futureList.add(f);

		}
		return futureList;
	}

	private <T> List<T> gatherList(Vector<Future<T>> futureList){
		Vector<T> list = new Vector<>();
		int tasktime=0;
		while(futureList.size() > 0){

			Future<T> m=futureList.firstElement();
			if (m.isDone()){
				T item;
				try {
					item = m.get();
					if (item != null)
						list.add(item);
				} catch (InterruptedException e) {
					LOG.debug("Async Task interrupted {} ",e);
					break;
				} catch (ExecutionException e) {
					LOG.debug("Async Task excution exception {} ",e);
					continue;
				} catch (RuntimeException e){
					LOG.debug("Async Task runtime exception {} ",e);
					continue;
				}
				futureList.remove(m);

			}else{
				if (TASK_TIMEOUT < tasktime)
					break;
				try {
					tasktime+=TASK_DELAY;
					Thread.sleep(TASK_DELAY);
				} catch (InterruptedException e) {
					LOG.debug("Async Task loop interrupted {} ",e);
					break;
				}
			}
		}
		return list;
	}

	private <T> List<T> mergeListOrder(Vector<Future<List<T>>> futureList){
		Vector<T> list = new Vector<>();
		int tasktime=0;
		while(futureList.size() > 0){

			Future<List<T>> m=futureList.firstElement();
			if (m.isDone()){
				List<T> item;
				try {
					item = m.get();
					if (item != null)
						list.addAll(item);
				} catch (InterruptedException e) {
					LOG.debug("Async Task interrupted {} ",e);
					break;
				} catch (ExecutionException e) {
					LOG.debug("Async Task excution exception {} ",e);
					continue;
				} catch (RuntimeException e){
					LOG.debug("Async Task runtime exception {} ",e);
					continue;
				}
				futureList.remove(m);

			}else{
				if (TASK_TIMEOUT < tasktime)
					break;
				try {
					tasktime+=TASK_DELAY;
					Thread.sleep(TASK_DELAY);
				} catch (InterruptedException e) {
					LOG.debug("Async Task loop interrupted {} ",e);
					break;
				}
			}
		}
		return list;
	}
	
	public <T,K> List<T> processAsyncList(List<K> datas,AsyncTask<K,T> call){

		Vector<Future<T>> futureList=requestAsyncList(datas,call);
		return gatherList(futureList);
	}

	public <K,T> List<T> processAsyncMergeList(List<K> datas,AsyncTask<K,List<T>> call){

		Vector<Future<List<T>>> futureList=requestAsyncList(datas,call);
		return mergeListOrder(futureList);
	}
	
	
}
