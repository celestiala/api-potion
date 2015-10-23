package com.tmoncorp.mobile.util.common.clientinfo;

import ch.qos.logback.classic.Logger;
import org.junit.Test;
import org.junit.Assert;
import org.slf4j.LoggerFactory;

public class ClientInfoTest {

	private static Logger LOGGER = (Logger) LoggerFactory.getLogger(ClientInfoTest.class);

	private final static String CLIENT="ClientInfoTester/1.0";
	private final static String DEVICE_VERSION="3.9.0";

	private String makeUserAgent(String platformType,String clientVersion){
		return CLIENT+" ("+platformType+";"+clientVersion+")";
	}

	private void deviceTypeTest(DeviceInfo.DeviceType type){
		ClientInfo info=new ClientInfo();
		String userAgent=makeUserAgent(type.toString(),DEVICE_VERSION);
		LOGGER.debug("generated user agent : {}",userAgent);
		info.setPlatform(ClientPlatform.DEVICE);
		info.setUserAgent(userAgent);
		DeviceInfo deviceInfo=info.getDeviceInfo();

		Assert.assertEquals("Device type check for "+type.toString()+" failed.",type,deviceInfo.getDeviceType());
		Assert.assertEquals("Device version check for " + DEVICE_VERSION + " failed", DEVICE_VERSION,deviceInfo.getDeviceVersion());
		LOGGER.debug("{} test passed",type.toString());
	}

	private void userAgentformat(String testName,String useragent){
		ClientInfo info=new ClientInfo();
		info.setPlatform(ClientPlatform.DEVICE);
		info.setUserAgent(useragent);
		DeviceInfo deviceInfo=info.getDeviceInfo();

		Assert.assertEquals("Device type check for no user agent failed.",DeviceInfo.DeviceType.unknown,deviceInfo.getDeviceType());
		Assert.assertEquals("Device version check failed", null,deviceInfo.getDeviceVersion());
		LOGGER.debug("{} test passed",testName);
	}

	@Test
	public void malform(){
		userAgentformat("null" ,null);
		userAgentformat("empty string", "");
		userAgentformat(") missing", "(");
		userAgentformat("empty extra infos", "Vers 1 ( )");
		userAgentformat("empty extra infos 2 ","Vers 1 ( ; )");
		userAgentformat("extra info malformat", "Vers 1 ( ww; @@)");
		userAgentformat("extra info malformat", "Vers 1 ( IOS; @@)");
	}

	@Test
	public void userAgentType(){
		deviceTypeTest(DeviceInfo.DeviceType.ios);
		deviceTypeTest(DeviceInfo.DeviceType.android);
	}


}