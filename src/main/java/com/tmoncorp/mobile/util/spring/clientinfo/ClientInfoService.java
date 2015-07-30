package com.tmoncorp.mobile.util.spring.clientinfo;

import com.tmoncorp.mobile.util.common.clientinfo.ClientInfo;

public class ClientInfoService {
	
	private static final ThreadLocal<ClientInfo> threadLocal = new ThreadLocal<ClientInfo>();
	
	public static ClientInfo getInfo(){
		return threadLocal.get();
	}
	
	public static void setInfo(ClientInfo info){
		threadLocal.set(info);
	}
	
	public static void clean(){
		threadLocal.remove();
	}
	
	
	

}
