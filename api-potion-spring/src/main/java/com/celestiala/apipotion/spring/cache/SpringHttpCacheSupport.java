package com.celestiala.apipotion.spring.cache;

import com.celestiala.apipotion.core.cache.httpcache.HttpCacheSupport;
import com.celestiala.apipotion.core.cache.httpcache.HttpCacheSupportImpl;
import com.celestiala.apipotion.core.cache.httpcache.HttpServletRequestContainer;
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
