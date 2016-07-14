package com.tmoncorp.apipotion.spring.http;

import com.tmoncorp.apipotion.core.clientinfo.ClientInfoProvider;
import com.tmoncorp.apipotion.core.clientinfo.HeaderNames;
import com.tmoncorp.apipotion.core.http.AsyncHttpClientBuilder;
import com.tmoncorp.apipotion.core.http.ClientBuilder;
import com.tmoncorp.apipotion.core.http.HttpClientBuilder;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Component
@PropertySource({"classpath:/applicationProperty.properties"})
public class SpringHttpClientFactory {

    private CloseableHttpClient client;
    private CloseableHttpAsyncClient asyncClient;


    private static final Logger LOG = LoggerFactory.getLogger(SpringHttpClientFactory.class);

    @Value("${apiClient.readTimeout:30000}")
    String defaultReadTimeout;
    @Value("${apiClient.connectionTimeout:2000}")
    String defaultConnectionTimeout;
    @Value("${apiClient.connectionRequestTimeout:2000}")
    String defaultConnectionRequestTimeout;
    @Value("${apiClient.maxTotalClient:1000}")
    String defaultMaxTotalClient;
    @Value("${apiClient.maxDefaultRoute:200}")
    String defaultMaxPerRoute;


    @PostConstruct
    public void init(){
        HttpClientBuilder builder=new HttpClientBuilder();
        configure(builder);
        client=builder.build();

        AsyncHttpClientBuilder asyncBuilder= new AsyncHttpClientBuilder();
        configure(builder);
        asyncClient=asyncBuilder.build();
    }

    protected void addDefaultHeaders(Collection<Header> headers){
        headers.add(new BasicHeader(HeaderNames.CLIENT_PLATFORM, ClientInfoProvider.getInfo().getPlatform().toString()));
        headers.add(new BasicHeader(HttpHeaders.CONNECTION,"close"));
    }

    protected void configure(ClientBuilder builder){
        ArrayList<Header> headers=new ArrayList<>();
        addDefaultHeaders(headers);

        builder.setDefaultMaxPerRoute(Integer.parseInt(defaultMaxPerRoute));
        builder.setMaxTotal(Integer.valueOf(defaultMaxTotalClient));
        builder.setUserAgent("Mobile API 0.1");
        builder.setConnectTimeout(Integer.valueOf(defaultConnectionTimeout));
        builder.setConnectionRequestTimeout(Integer.valueOf(defaultConnectionRequestTimeout));
        builder.setReadTimeout(Integer.valueOf(defaultReadTimeout));
        builder.setDefaultHeaders(headers);
    }

    public CloseableHttpClient getClient(){
        return client;
    }

    public CloseableHttpAsyncClient getAsyncClient(){
        return asyncClient;
    }

    @PreDestroy
    public void cleanUp(){
        try {
            client.close();
        } catch (IOException e) {
            LOG.warn("{}",e);
        }

    }
}
