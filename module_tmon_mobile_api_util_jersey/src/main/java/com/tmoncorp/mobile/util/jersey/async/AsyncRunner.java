package com.tmoncorp.mobile.util.jersey.async;

import com.tmoncorp.mobile.util.common.async.AsyncExecutor;
import com.tmoncorp.mobile.util.common.async.AsyncTask;
import com.tmoncorp.mobile.util.common.async.AsyncWorker;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Singleton
public class AsyncRunner implements ContainerLifecycleListener,AsyncWorker {


	private final AsyncExecutor excutor;
	private static AsyncRunner instance;

	public AsyncRunner(){
		excutor=new AsyncExecutor();
		instance=this;
	}
	

	public <T> Future<T> submitAsync(Callable<T> call){

		return excutor.submitAsync(call);
	}

	@Override
	public void submitAsync(Runnable run){
		excutor.submitAsync(run);
	}


	public <T,K> List<T> processAsyncList(List<K> datas,AsyncTask<K,T> call){

		return excutor.processAsyncList(datas, call);
	}

	public <K,T> List<T> processAsyncMergeList(List<K> datas,AsyncTask<K,List<T>> call){

		return excutor.processAsyncMergeList(datas, call);
	}

	@Override
	public void onStartup(Container container) {
		try{
			excutor.init();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onReload(Container container) {

	}

	@Override public void onShutdown(Container container) {
		try {
			excutor.cleanUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static AsyncRunner getInstance(){
		return instance;
	}
}
