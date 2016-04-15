package com.tmoncorp.mobile.util.common.http;

import org.apache.http.conn.HttpClientConnectionManager;

import java.util.concurrent.TimeUnit;

public class IdleConnectionMonitor implements Runnable {

    private static final int THREAD_DEALY = 5 * 1000; //ms
    private static final int IDLE_TIME = 30; //seconds

    private volatile boolean isShudown=false;
    private HttpClientConnectionManager connectionManager;

    public IdleConnectionMonitor(){

    }

    public IdleConnectionMonitor(HttpClientConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void setConnectionManager(HttpClientConnectionManager connectionManager){
        this.connectionManager=connectionManager;
    }

    @Override
    public void run() {

        try {
            while (!isShudown) {
                synchronized (this) {
                    wait(THREAD_DEALY);
                }
                connectionManager.closeExpiredConnections();
                connectionManager.closeIdleConnections(IDLE_TIME, TimeUnit.SECONDS);
            }

        } catch (InterruptedException e) {
            isShudown=true;
        }
    }

    public void shudown() {
        isShudown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}
