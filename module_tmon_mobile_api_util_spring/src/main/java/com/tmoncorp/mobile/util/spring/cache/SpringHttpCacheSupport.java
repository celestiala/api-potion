package com.tmoncorp.mobile.util.spring.cache;

import com.tmoncorp.mobile.util.common.cache.httpcache.HttpCacheSupport;
import com.tmoncorp.mobile.util.common.cache.httpcache.HttpCacheSupportImpl;
import com.tmoncorp.mobile.util.common.cache.httpcache.HttpServletRequestContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Component
public class SpringHttpCacheSupport implements HttpCacheSupport {

    @Autowired
    private HttpServletRequest httpServletRequest;
    private HttpServletRequestContainer httpServletRequestContainer;
    private HttpCacheSupport innerSupport;

    public SpringHttpCacheSupport(){
        httpServletRequestContainer=()->httpServletRequest;
        innerSupport=new HttpCacheSupportImpl(httpServletRequestContainer);
    }


    @Override public void setEtag(String etag) {
        innerSupport.setEtag(etag);
    }

    @Override public void setExpire(LocalDateTime expire) {
        innerSupport.setExpire(expire);
    }

    @Override public HttpServletRequestContainer getHttpRequestContainer() {
        return httpServletRequestContainer;
    }
}
