package com.tmoncorp.mobile.util.spring.clientinfo;

import com.tmoncorp.mobile.util.common.clientinfo.ClientInfo;
import com.tmoncorp.mobile.util.common.clientinfo.ClientPlatform;

public class ClientInfoService {

	private static final ClientInfo defaultInfo;

	static {
		defaultInfo=new ClientInfo();
		defaultInfo.setPlatform(ClientPlatform.MOBILE);
	}
	
	private static final ThreadLocal<ClientInfo> threadLocal = new ThreadLocal<>();
	
	public static ClientInfo getInfo(){
		ClientInfo info=threadLocal.get();
		if (info == null)
			info=defaultInfo;
		return info;
	}
	
	public static void setInfo(ClientInfo info){
		threadLocal.set(info);
	}
	
	public static void clean(){
		threadLocal.remove();
	}
	
	
	

}
