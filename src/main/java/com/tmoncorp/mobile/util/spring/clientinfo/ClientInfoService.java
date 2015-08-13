package com.tmoncorp.mobile.util.spring.clientinfo;

import com.tmoncorp.mobile.util.common.clientinfo.ClientInfo;
import com.tmoncorp.mobile.util.common.clientinfo.ClientInfoProvider;

public class ClientInfoService {

	@Deprecated
	public static ClientInfo getInfo(){
		return ClientInfoProvider.getInfo();
	}

	@Deprecated
	public static void setInfo(ClientInfo info){
		ClientInfoProvider.setInfo(info);
	}

	@Deprecated
	public static void clean(){
		ClientInfoProvider.clean();
	}

}
