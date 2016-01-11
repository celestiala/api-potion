package com.tmoncorp.mobile.util.jersey.cache;

import com.tmoncorp.mobile.util.common.cache.HttpCacheConstant;
import com.tmoncorp.mobile.util.common.cache.HttpCacheType;
import com.tmoncorp.mobile.util.common.cache.Cache;

import javax.inject.Inject;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CacheFilter implements ContainerRequestFilter, ContainerResponseFilter {

	@Context
	private ResourceInfo resourceInfo;

	@Inject
	private CacheRepository cacheRepo;

	private boolean hasNotEtag(){
		Cache cache=resourceInfo.getResourceMethod().getAnnotation(Cache.class);
		return cache == null || cache.browserCache() != HttpCacheType.ETAG;
	}
	private boolean isNotModified(ContainerRequestContext requestContext){
		String etag=requestContext.getHeaderString(HttpHeaders.IF_NONE_MATCH);
		if (cacheRepo.get(etag) != null)
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
		responseContext.getHeaders().add(HttpHeaders.EXPIRES,requestContext.getProperty(HttpCacheConstant.EXPIRE));

		if (hasNotEtag())
			return;
		responseContext.getHeaders().add(HttpHeaders.ETAG,requestContext.getProperty(HttpCacheConstant.ETAG));
	}
}

