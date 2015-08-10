package com.tmoncorp.mobile.util.jersey.cache;

import com.tmoncorp.mobile.util.common.cache.CacheMode;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/v1/cache")
public class CacheResource {

	@Inject
	private CacheRepository cacheRepo;
	@GET
	@Path("/mode/{mode}")
	@Produces(MediaType.APPLICATION_JSON)
	public String martDeal(@PathParam("mode") CacheMode mode) {

		cacheRepo.setMode(mode);
		return "{\"mode\":\""+mode+"\"}";
	}
}
