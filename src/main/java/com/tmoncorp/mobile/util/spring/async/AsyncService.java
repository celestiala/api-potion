package com.tmoncorp.mobile.util.spring.async;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.tmoncorp.mobile.util.common.async.AsyncExecutor;
import com.tmoncorp.mobile.util.common.async.AsyncTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.tmoncorp.mobile.util.common.clientinfo.ClientInfo;
import com.tmoncorp.mobile.util.spring.clientinfo.ClientInfoService;


@Service
public class AsyncService {
	

	private final AsyncExecutor excutor;

	public AsyncService(){
		excutor=new AsyncExecutor();
	}
	
	@PostConstruct
	public void init() throws Exception {
		excutor.init();
	}
	
	@PreDestroy
	public void cleanUp() throws Exception {
		excutor.cleanUp();
	}
	
	public <T> Future<T> submitAsync(Callable<T> call){

		return excutor.submitAsync(call);
	}


	public <T,K> List<T> processAsyncList(List<K> datas,AsyncTask<K,T> call){

		return excutor.processAsyncList(datas,call);
	}

	public <K,T> List<T> processAsyncMergeList(List<K> datas,AsyncTask<K,List<T>> call){

		return excutor.processAsyncMergeList(datas,call);
	}
	
	
}
