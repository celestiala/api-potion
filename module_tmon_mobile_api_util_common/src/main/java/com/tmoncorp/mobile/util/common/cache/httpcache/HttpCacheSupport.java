package com.tmoncorp.mobile.util.common.cache.httpcache;

import java.time.LocalDateTime;

public interface HttpCacheSupport {
    void setEtag(String etag);

    void setExpire(LocalDateTime expire);

    HttpServletRequestContainer getHttpRequestContainer();
}
