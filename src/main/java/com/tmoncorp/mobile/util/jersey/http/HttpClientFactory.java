package com.tmoncorp.mobile.util.jersey.http;

import com.tmoncorp.mobile.util.common.clientinfo.ClientInfo;
import com.tmoncorp.mobile.util.common.clientinfo.ClientPlatform;
import com.tmoncorp.mobile.util.common.clientinfo.HeaderNames;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;

import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import com.owlike.genson.ext.jaxrs.GensonJsonConverter;

import java.io.IOException;
import java.util.Properties;

@Singleton
public class HttpClientFactory {
	private static final Logger LOG = LoggerFactory.getLogger(HttpClientFactory.class);
	private final static int READ_TIMEOUT = 240 * 1000; //ms
	private final static int CONNETION_TIMEOUT = 1 * 1000; //ms
	private Client client;

	public HttpClientFactory(Properties properties) {
		//String str=properties.getProperty("readTimeout");

		createClient();
	}

	static class HttpClientRequestFilter implements ClientRequestFilter {

		@Override public void filter(ClientRequestContext requestContext) throws IOException {
				MultivaluedMap<String, Object> headerMap= requestContext.getHeaders();
				headerMap.add(HeaderNames.CLIENT_PLATFORM, ClientPlatform.DEVICE);
				headerMap.add(HeaderNames.API_VERSION,1);
				headerMap.add(HeaderNames.USER_AGENT,"mobile gateway");

		}

	}

	private void createClient() {
		PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
		manager.setMaxTotal(100);
		manager.setDefaultMaxPerRoute(50);

		ClientConfig conf = new ClientConfig();
		conf.connectorProvider(new ApacheConnectorProvider());
		conf.property(ClientProperties.READ_TIMEOUT, READ_TIMEOUT);
		conf.property(ClientProperties.CONNECT_TIMEOUT, CONNETION_TIMEOUT);
		conf.property(ApacheClientProperties.CONNECTION_MANAGER, manager);
		conf.register(GensonJsonConverter.class);
		conf.register(HttpClientRequestFilter.class);

		client = ClientBuilder.newClient(conf);
	}

	public Client getClient() {
		return client;
	}

}
