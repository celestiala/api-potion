package com.tmoncorp.mobile.util.spring.async;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.tmoncorp.mobile.util.common.async.AsyncTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.tmoncorp.mobile.util.common.clientinfo.ClientInfo;
import com.tmoncorp.mobile.util.spring.clientinfo.ClientInfoService;


@Service
public class AsyncService {
	
	private static final Logger LOG = LoggerFactory.getLogger(AsyncService.class);
	
	private final static int THREAD_POOL_MIN_CORE_SIZE=2;
	private final static int THREAD_POOL_MAX_SIZE=12;
	private final static int TASK_TIMEOUT=240*1000; //ms
	private final static int TASK_DELAY=50;
	private ThreadPoolTaskExecutor pool;
	
	@PostConstruct
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
	
	@PreDestroy
	public void cleanUp() throws Exception {
		if (pool !=null){
			pool.shutdown();
			pool=null;
		}
	}
	
	public <T> Future<T> submitAsync(Callable<T> call){
		return pool.submit(call);
	}
	
	public <T,K> List<T> processAsyncList(List<K> datas,AsyncTask<K,T> call){
		Vector<T> list = new Vector<T>();
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
		
		int tasktime=0;
		while(futureList.size() > 0){
			
			Future<T> m=futureList.firstElement();
			if (m.isDone()){
				T deal;
                try {
	                deal = m.get();
	                if (deal != null)
	                	list.add(deal);
                } catch (InterruptedException e) {
                	LOG.debug("Async Task interrupted {} ",e);
                	e.printStackTrace();
	                break;
                } catch (ExecutionException e) {
                	LOG.debug("Async Task excution exception {} ",e);
                	e.printStackTrace();
	                break;
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
	
	
}
