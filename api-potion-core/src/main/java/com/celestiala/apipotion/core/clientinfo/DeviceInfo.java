package com.celestiala.apipotion.core.clientinfo;

public class DeviceInfo {
    private static DeviceInfo defaultInfo = new DeviceInfo();

    static {
        defaultInfo.setDeviceType(DeviceType.unknown);
    }

    private DeviceType deviceType;
    private String deviceVersion;

    public static DeviceInfo getDefaultInfo() {
        return defaultInfo;
    }

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

    public enum DeviceType {
        ios, android, unknown
    }
}
