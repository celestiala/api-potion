package com.tmoncorp.mobile.util.common.cache.httpcache;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class HttpCacheSupportImpl implements HttpCacheSupport{

	private HttpServletRequestContainer container;

	public HttpCacheSupportImpl(HttpServletRequestContainer container){
		this.container=container;
	}

	@Override
	public void setEtag(String etag){
		container.getHttpServletRequest().setAttribute(HttpCacheConstant.ETAG,etag);
	}

	@Override public void setExpire(LocalDateTime expire) {

		ZonedDateTime zonedDateTime=expire.atZone(ZoneId.systemDefault());
		container.getHttpServletRequest().setAttribute(HttpCacheConstant.EXPIRE,zonedDateTime.format(DateTimeFormatter.RFC_1123_DATE_TIME));
	}
}
