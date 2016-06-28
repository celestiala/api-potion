package com.tmoncorp.mobile.util.common.http;

import org.apache.http.Header;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;

import java.util.Collection;

public class AsyncHttpClientBuilder implements ClientBuilder{

    private PoolingNHttpClientConnectionManager connectionManager;
    private IOReactorConfig.Builder ioReactorBuilder;
    private Collection<Header> defaultHeaders;
    private int maxTotal=500;
    private int defaultMaxPerRoute=100;
    private String userAgent;

    public AsyncHttpClientBuilder(){
        ioReactorBuilder=IOReactorConfig.custom();
    }

    public AsyncHttpClientBuilder setIoThreadCount(int count){
        ioReactorBuilder.setIoThreadCount(count);
        return this;
    }

    public AsyncHttpClientBuilder setConnectTimeout(int timeout){
        ioReactorBuilder.setConnectTimeout(timeout);
        return this;
    }

    @Override
    public AsyncHttpClientBuilder setConnectionRequestTimeout(int timeout) {

        return this;
    }

    public AsyncHttpClientBuilder setReadTimeout(int timeout){
        ioReactorBuilder.setSoTimeout(timeout);
        return this;
    }

    public AsyncHttpClientBuilder setMaxTotal(int total){
        maxTotal=total;
        return this;
    }

    public AsyncHttpClientBuilder setDefaultMaxPerRoute(int max){
        defaultMaxPerRoute=max;
        return this;
    }


    public AsyncHttpClientBuilder setUserAgent(String userAgent){
        this.userAgent=userAgent;
        return this;
    }

    public AsyncHttpClientBuilder setDefaultHeaders(Collection<Header> headers){
        defaultHeaders=headers;
        return this;
    }

    private void init() throws IOReactorException{

        IOReactorConfig config=ioReactorBuilder.build();
        ConnectingIOReactor ioReactor=new DefaultConnectingIOReactor(config);
        connectionManager=new PoolingNHttpClientConnectionManager(ioReactor);
        connectionManager.setMaxTotal(maxTotal);
        connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);

    }

    public CloseableHttpAsyncClient build(){
        try {
            init();
        } catch (IOReactorException e) {
            return null;
        }
        return HttpAsyncClients
                .custom()
                .setConnectionManager(connectionManager)
                .setUserAgent(userAgent)
                .setDefaultHeaders(defaultHeaders)
                .setMaxConnTotal(maxTotal)
                .setMaxConnPerRoute(defaultMaxPerRoute)
                .build();
    }
}
