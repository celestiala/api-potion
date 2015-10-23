package com.tmoncorp.mobile.util.common.clientinfo;

public class DeviceInfo {
	public enum DeviceType{
		ios,android,unknown
	}

	private DeviceType deviceType;
	private String deviceVersion;

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceVersion() {
		return deviceVersion;
	}

	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}

	private static DeviceInfo defaultInfo=new DeviceInfo();

	static {
		defaultInfo.setDeviceType(DeviceType.unknown);
	}

	public static DeviceInfo getDefaultInfo(){
		return defaultInfo;
	}
}
