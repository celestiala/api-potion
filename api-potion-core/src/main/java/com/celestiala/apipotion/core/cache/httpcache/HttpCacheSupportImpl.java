package com.celestiala.apipotion.core.cache.httpcache;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class HttpCacheSupportImpl implements HttpCacheSupport {

    private HttpServletRequestContainer container;

    public HttpCacheSupportImpl(HttpServletRequestContainer container) {
        this.container = container;
    }

    @Override
    public void setEtag(String etag) {
        container.getHttpServletRequest().setAttribute(HttpCacheConstant.ETAG, etag);
    }

    @Override public void setExpire(LocalDateTime expire) {

        ZonedDateTime zonedDateTime = expire.atZone(ZoneId.systemDefault());
        if (container.getHttpServletRequest() != null) {
            try {
                container.getHttpServletRequest().setAttribute(HttpCacheConstant.EXPIRE, zonedDateTime.format(DateTimeFormatter.RFC_1123_DATE_TIME));
            }catch(IllegalStateException e){
                // do nothing
                // TODO: remove this Exception by check running thread
                return;
            }
        }
    }

    @Override public HttpServletRequestContainer getHttpRequestContainer() {
        return container;
    }
}
