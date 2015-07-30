package com.tmoncorp.mobile.util.common.clientinfo;

public class ClientInfo {
	
	private ClientPlatform platform;
	private String version;
	private String userAgent;
	
	public ClientPlatform getPlatform() {
		return platform;
	}
	public void setPlatform(ClientPlatform platform) {
		this.platform = platform;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	

}
