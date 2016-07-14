package com.tmoncorp.mobile.util.jersey.cache;

import com.tmoncorp.mobile.util.common.cache.Cache;
import com.tmoncorp.mobile.util.common.cache.httpcache.HttpCacheConstant;
import com.tmoncorp.mobile.util.common.cache.httpcache.HttpCacheType;

import javax.inject.Inject;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CacheFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Inject
    private JerseyMemCacheRepository cacheRepo;

    private boolean hasNotEtag() {
        Cache cache = resourceInfo.getResourceMethod().getAnnotation(Cache.class);
        return cache == null || cache.browserCache() != HttpCacheType.ETAG;
    }

    private boolean isNotModified(ContainerRequestContext requestContext) {
        String etag = requestContext.getHeaderString(HttpHeaders.IF_NONE_MATCH);
        if (cacheRepo.getRaw(etag) != null)
            return true;
        return false;
    }

    @Override public void filter(ContainerRequestContext requestContext) throws IOException {
        if (hasNotEtag())
            return;
        if (isNotModified(requestContext))
            requestContext.abortWith(Response.status(Response.Status.NOT_MODIFIED).build());

    }

    @Override public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {

        if (resourceInfo == null||resourceInfo.getResourceMethod() == null)
            return;

        Cache cache = resourceInfo.getResourceMethod().getAnnotation(Cache.class);
        if (cache == null)
            return;
        MultivaluedMap<String, Object> responseHeaders = responseContext.getHeaders();

        String expireTime = (String) requestContext.getProperty(HttpCacheConstant.EXPIRE);
        if (expireTime != null && !expireTime.isEmpty())
            responseHeaders.add(HttpHeaders.EXPIRES, expireTime);

        if (cache.browserCache() != HttpCacheType.ETAG)
            return;
        responseHeaders.add(HttpHeaders.ETAG, requestContext.getProperty(HttpCacheConstant.ETAG));
    }
}

