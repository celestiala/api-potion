package com.celestiala.apipotion.spring.http;

import com.celestiala.apipotion.core.clientinfo.ClientInfoProvider;
import com.celestiala.apipotion.core.clientinfo.ClientPlatform;
import com.celestiala.apipotion.core.clientinfo.HeaderNames;
import com.celestiala.apipotion.core.http.AsyncHttpClientBuilder;
import com.celestiala.apipotion.core.http.ClientBuilder;
import com.celestiala.apipotion.core.http.HttpClientBuilder;
import org.apache.commons.lang3.StringUtils;
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

    private static final String APPLICATION_PROPERTIES = "applicationProperty.properties";
    private static final String CLIENT_PLATORM_PROPERTY="client.platform";

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

    @Value("${client.platform:MOBILE}")
    String clientPlatform;


    private void setClientPlatform(){
        if (StringUtils.isEmpty(clientPlatform))
            return;
        ClientPlatform platform=ClientPlatform.valueOf(clientPlatform);
        if (platform != null)
            ClientInfoProvider.setDefaultPlatform(platform);
    }

    @PostConstruct
    public void init(){

        setClientPlatform();

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
