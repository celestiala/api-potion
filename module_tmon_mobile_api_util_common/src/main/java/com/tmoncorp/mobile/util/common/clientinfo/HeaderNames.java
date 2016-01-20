package com.tmoncorp.mobile.util.common.clientinfo;


public class HeaderNames {

	private HeaderNames(){
		throw new AssertionError("static utility class");
	}

	public static final String CLIENT_PLATFORM="Api-Client-Platform";
	public static final String API_VERSION="Api-Version";
	public static final String USER_AGENT="User-Agent";
}
