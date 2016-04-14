package com.tmoncorp.mobile.util.common.http;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.Collection;

public class HttpClientBuilder {

    private final PoolingHttpClientConnectionManager cm;
    private IdleConnectionMonitor im;
    private Collection<Header> defaultHeaders;
    private RequestConfig.Builder requestConfigBuilder=RequestConfig.custom();
    private String userAgent;


    public HttpClientBuilder(){

        this(new PoolingHttpClientConnectionManager());
    }

    public HttpClientBuilder(PoolingHttpClientConnectionManager cm){
        this.cm=cm;
    }

    public HttpClientBuilder setIdleConnecetionMonitor(IdleConnectionMonitor monitor){
        im=monitor;
        im.setConnectionManager(cm);
        return this;
    }

    public HttpClientBuilder setMaxTotal(int max){
        cm.setMaxTotal(max);
        return this;
    }

    public HttpClientBuilder setDefaultMaxPerRoute(int max){
        cm.setDefaultMaxPerRoute(max);
        return this;
    }

    public HttpClientBuilder setMaxperRoute(String name,int max){
        setMaxperRoute(new HttpHost(name),max);
        return this;
    }

    public HttpClientBuilder setMaxperRoute(HttpHost host,int max){
        cm.setMaxPerRoute(new HttpRoute(host),max);
        return this;
    }

    public HttpClientBuilder setDefaultHeaders(Collection<Header> headers){
        defaultHeaders=headers;
        return this;
    }

    public HttpClientBuilder setUserAgent(String userAgent){
        this.userAgent=userAgent;
        return this;
    }

    public HttpClientBuilder setConnectTimeout(int timeout){
        requestConfigBuilder.setConnectTimeout(timeout);
        return this;
    }

    public HttpClientBuilder setConnectionRequestTimeout(int timeout){
        requestConfigBuilder.setConnectionRequestTimeout(timeout);
        return this;
    }


    public HttpClientBuilder setReadTimeout(int timeout){
        requestConfigBuilder.setSocketTimeout(timeout);
        return this;
    }

    public CloseableHttpClient build(){

        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfigBuilder.build())
                .setUserAgent(userAgent)
                .setConnectionManager(cm)
                .setDefaultHeaders(defaultHeaders)
                .build();
    }

}
