package com.celestiala.mobile.resource;

import com.celestiala.mobile.repository.CacheTestRepository;
import com.celestiala.mobile.util.common.cache.Cache;
import com.celestiala.mobile.util.common.cache.httpcache.HttpCacheType;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/memcache/sync")
public class SyncCacheTestResource {


	@Inject
	CacheTestRepository cacheTestRepository;

	@GET
	@Cache(expiration = 4)
	@Path("default")
	public String getSync(){
		return cacheTestRepository.makeValue();
	}

	@GET
	@Cache(expiration = 4,setOnError = false)
	@Path("setOnError")
	public String getSetOnError(){
		return cacheTestRepository.makeOnce();
	}

	@GET
	@Cache(expiration = 4,compress=true)
	@Path("compress")
	public Object getCompressed(){
		return cacheTestRepository.makeValue();
	}

	@GET
	@Cache(expiration = 4, browserCache = HttpCacheType.NONE)
	@Path("browserCache")
	public String getBrowserCacheNone(){
		return cacheTestRepository.makeValue();
	}

}
