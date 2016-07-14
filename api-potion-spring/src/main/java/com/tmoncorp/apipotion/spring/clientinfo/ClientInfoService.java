package com.tmoncorp.apipotion.spring.clientinfo;

import com.tmoncorp.apipotion.core.clientinfo.ClientInfo;
import com.tmoncorp.apipotion.core.clientinfo.ClientInfoProvider;

public class ClientInfoService {

    private ClientInfoService() {
        throw new AssertionError("static utility class");
    }

    public static ClientInfo getInfo() {
        return ClientInfoProvider.getInfo();
    }

    public static void setInfo(ClientInfo info) {
        ClientInfoProvider.setInfo(info);
    }

    public static void clean() {
        ClientInfoProvider.clean();
    }

}
