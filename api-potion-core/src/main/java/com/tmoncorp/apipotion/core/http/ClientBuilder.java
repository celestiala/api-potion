package com.tmoncorp.apipotion.core.http;

import org.apache.http.Header;

import java.io.Closeable;
import java.util.Collection;

public interface ClientBuilder {

    ClientBuilder setMaxTotal(int max);

    ClientBuilder setDefaultMaxPerRoute(int max);

    ClientBuilder setDefaultHeaders(Collection<Header> headers);

    ClientBuilder setUserAgent(String userAgent);

    ClientBuilder setConnectTimeout(int timeout);

    ClientBuilder setConnectionRequestTimeout(int timeout);

    ClientBuilder setReadTimeout(int timeout);

    Closeable build();
}
