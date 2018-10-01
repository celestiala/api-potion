package com.celestiala.apipotion.core.clientinfo;

public class ClientInfoProvider {

    private static final ClientInfo DEFAULT_INFO;
    private static final ThreadLocal<ClientInfo> THREAD_LOCAL = new ThreadLocal<>();

    static {
        DEFAULT_INFO = new ClientInfo();
        DEFAULT_INFO.setPlatform(ClientPlatform.MOBILE);
    }

    private ClientInfoProvider() {
        throw new AssertionError("static utility class");
    }

    public static ClientInfo getInfo() {
        ClientInfo info = THREAD_LOCAL.get();
        if (info == null)
            info = DEFAULT_INFO;
        return info;
    }

    public static void setInfo(ClientInfo info) {
        THREAD_LOCAL.set(info);
    }

    public static void clean() {
        THREAD_LOCAL.remove();
    }

    public static void setDefaultPlatform(ClientPlatform platform){
        DEFAULT_INFO.setPlatform(platform);
    }
}
