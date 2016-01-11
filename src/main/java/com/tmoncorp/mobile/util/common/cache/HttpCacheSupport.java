package com.tmoncorp.mobile.util.common.cache;

import java.time.LocalDateTime;

public interface HttpCacheSupport {
	void setEtag(String etag);
	void setExpire(LocalDateTime expire);
}
