package com.tmoncorp.apipotion.core.clientinfo;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class ClientInfo {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ClientInfo.class);

    private ClientPlatform platform;
    private DeviceInfo deviceInfo;
    private String version;
    private String userAgent;

    public ClientPlatform getPlatform() {
        return platform;
    }

    public void setPlatform(ClientPlatform platform) {
        this.platform = platform;
    }

    private DeviceInfo makeDeviceInfo() {
        int extrainfoStart = userAgent.indexOf("(");
        if (extrainfoStart < 0)
            return DeviceInfo.getDefaultInfo();
        int extrainfoend = userAgent.indexOf(")", extrainfoStart);
        if (extrainfoStart > extrainfoend)
            return DeviceInfo.getDefaultInfo();
        String extrainfos = userAgent.substring(extrainfoStart + 1, extrainfoend);
        String[] extras = extrainfos.split(";");
        if (extras.length > 1) {
            DeviceInfo.DeviceType type;
            try {
                type = DeviceInfo.DeviceType.valueOf(extras[0].trim());
            } catch (IllegalArgumentException e) {
                LOGGER.debug("",e);
                return DeviceInfo.getDefaultInfo();
            }

            DeviceInfo info = new DeviceInfo();
            info.setDeviceType(type);
            info.setDeviceVersion(extras[1].trim());
            return info;
        }

        return DeviceInfo.getDefaultInfo();
    }

    public DeviceInfo getDeviceInfo() {
        if (deviceInfo == null) {
            if (platform == ClientPlatform.DEVICE && userAgent != null)
                deviceInfo = makeDeviceInfo();
            else
                deviceInfo = DeviceInfo.getDefaultInfo();
        }
        return deviceInfo;

    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
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
