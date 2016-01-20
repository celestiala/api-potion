package com.tmoncorp.mobile.util.spring.clientinfo;

import com.tmoncorp.mobile.util.common.clientinfo.ClientPlatform;

public class LagacyPlatformTransrator {

	private LagacyPlatformTransrator(){
		throw new AssertionError("static utility class");
	}
	
	private static ClientPlatform fromPrefix(String platform){
		
		if (platform.startsWith("M") || platform.startsWith("m")){
			return ClientPlatform.MOBILE;
		}
		
		if (platform.startsWith("A") || platform.startsWith("a")){
			return ClientPlatform.DEVICE;
		}
		
		if (platform.startsWith("P") || platform.startsWith("p")){
			return ClientPlatform.NORMAL;
		}
		return ClientPlatform.DEVICE;
	}
	
	public static ClientPlatform fromMobilePC(String platform){
		return fromPrefix(platform);
	}
	
	public static ClientPlatform fromMPCString(String platform){
		return fromPrefix(platform);
	}
	
	public static String toMobilePC(){
		ClientPlatform platform = ClientInfoService.getInfo().getPlatform();
		if (platform == ClientPlatform.MOBILE)
			return "MOBILE";
		if (platform == ClientPlatform.DEVICE)
			return "APP";
		if (platform == ClientPlatform.NORMAL)
			return "PC";
		return "MOBILE";
	}
	
	public static String toMobilePClowerCase(){
		ClientPlatform platform = ClientInfoService.getInfo().getPlatform();
		if (platform == ClientPlatform.MOBILE)
			return "mobile";
		if (platform == ClientPlatform.DEVICE)
			return "app";
		if (platform == ClientPlatform.NORMAL)
			return "pc";
		return "mobile";
	}

}
