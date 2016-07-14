package com.tmoncorp.mobile.util.common.cache;

import com.tmoncorp.mobile.util.common.async.AsyncWorker;
import com.tmoncorp.mobile.util.common.cache.httpcache.HttpCacheInfoContainer;
import com.tmoncorp.mobile.util.common.cache.httpcache.HttpCacheSupport;
import com.tmoncorp.mobile.util.common.cache.httpcache.HttpCacheType;
import com.tmoncorp.mobile.util.common.security.Compress;
import com.tmoncorp.mobile.util.common.security.SecurityUtils;
import com.tmoncorp.mobile.util.common.type.PrimitiveDefaults;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;

public class CacheService implements CacheProvider, HttpCacheInfoContainer {

    protected static final String KEY_SEPERATOR = ":";
    protected static final int LONG_KEY = 100;
    private static final Logger LOG = LoggerFactory.getLogger(CacheService.class);
    private static final int EXPIRE_TIME_ON_ERROR = 60 * 60; // an hour
    protected final CacheRepository cacheRepository;
    protected final AsyncWorker asyncWorker;
    protected HttpCacheSupport httpCacheSupport;
    protected String cachePrefix = "";

    private boolean isSupportInvalidateRequest=false;
    private boolean invalidationMode=false;
    private boolean isHttpCacheSupport = false;

    public CacheService(CacheRepository repo, AsyncWorker worker) {
        cacheRepository = repo;
        asyncWorker = worker;
    }

    protected void setExpire(CacheItem item) {
        if (isHttpCacheSupport)
            getHttpCache().setExpire(item.getExpireTime());
    }

    protected void addMethodName(StringBuilder cb, MethodInvocation invo) {
        Method method = invo.getMethod();
        cb.append(method.getDeclaringClass().getSimpleName());
        cb.append(KEY_SEPERATOR);
        cb.append(method.getName());
    }

    protected void appendEnvName(StringBuilder builder, Cache cacheInfo) {
        // default, do notihing
    }

    protected String makeKeyName(MethodInvocation invo, Cache cacheInfo) {

        StringBuilder cb = new StringBuilder();
        String name = cacheInfo.name();
        if (name.isEmpty()) {
            addMethodName(cb, invo);
        } else {
            cb.append(cacheInfo);
            cb.append(name);
        }

        StringBuilder cp = new StringBuilder();
        appendEnvName(cp, cacheInfo);

        Parameter[] params=invo.getMethod().getParameters();

        int i=0;
        for (Object param : invo.getArguments()) {
            CacheParam cacheParam=params[i].getAnnotation(CacheParam.class);
            if (cacheParam == null || (!cacheParam.ignore() && !cacheParam.invalidate())) {
                cp.append(KEY_SEPERATOR);
                cp.append(param);
            }
            ++i;
        }
        if (cp.length() < LONG_KEY)
            cb.append(cp);
        else {
            cb.append(SecurityUtils.getHash(cp.toString(), SecurityUtils.MD5));
        }
        return cb.toString();
    }

    public String getPrefix() {
        return cachePrefix;
    }

    public void setPrefix(String prefix) {

        if (prefix == null) {
            cachePrefix = "";
            return;
        }

        int length = prefix.length();
        if (length > 5) {
            cachePrefix = prefix.substring(0, 5) + ":";
        } else if (length == 0) {
            cachePrefix = "";
        } else {
            cachePrefix = prefix + ":";
        }
    }

    @Override
    public void set(String keyName, Object value, Cache cacheInfo) {

        if (value == null)
            return;

        Object cacheItem = value;
        CacheType cacheType = cacheInfo.type();
        int realExpireTime = cacheInfo.expiration();
        if (cacheType != CacheType.SYNC) {

            if (cacheType == CacheType.ASYNC)
                realExpireTime = realExpireTime * 3;
            else if (cacheType == CacheType.ASYNC_ONLY)
                realExpireTime = 60 * 60 * 24;

            if (cacheInfo.setOnError() && realExpireTime < EXPIRE_TIME_ON_ERROR)
                realExpireTime = EXPIRE_TIME_ON_ERROR;

            CacheItem cache = new CacheItem();
            cache.setExpireTime(LocalDateTime.now().plusSeconds(cacheInfo.expiration()));
            cache.setValue(value);

            cacheItem = cache;
        }

        if (isHttpCacheSupport && cacheInfo.browserCache() == HttpCacheType.ETAG) {
            String etag = SecurityUtils.getSHA1String(keyName + cacheInfo.expiration());
            getHttpCache().setEtag(etag);
        }

        cacheRepository.setRaw(keyName, cacheItem, realExpireTime);
    }

    private boolean isNeedInvalidation(MethodInvocation mi){
        Parameter[] params=mi.getMethod().getParameters();
        int i=0;
        for (Object param : mi.getArguments()) {
            CacheParam cacheParam=params[i].getAnnotation(CacheParam.class);
            if (cacheParam !=null && cacheParam.invalidate()){
                return (Boolean) param;
            }
            ++i;
        }
        return false;
    }

    private Invalidate getInvalidateFromRequest(){
        String refresh;
        try {
            refresh = httpCacheSupport.getHttpRequestContainer().getHttpServletRequest().getParameter("refreshcache");
            if (refresh !=null)
                return Invalidate.REFRESH;
        }catch (NullPointerException | IllegalStateException e){
            // do nothing
        }
        return null;
    }

    private Invalidate getInvalidateFromMethod(Cache cacheInfo, MethodInvocation mi){
        if (cacheInfo.invalidate() == Invalidate.OFF)
            return Invalidate.OFF;
        if (isNeedInvalidation(mi))
            return cacheInfo.invalidate();
        return Invalidate.OFF;
    }

    private Invalidate getInvalidate(Cache cacheInfo, MethodInvocation mi){
        if (isSupportInvalidateRequest) {
            Invalidate invalidate = getInvalidateFromRequest();
            if (invalidate != null)
                return invalidate;
        }
        return getInvalidateFromMethod(cacheInfo,mi);
    }

    @Override
    public Object get(Cache cacheInfo, MethodInvocation mi) {

        String keyName = makeKeyName(mi, cacheInfo);
        Object item;

        Invalidate invalidate=getInvalidate(cacheInfo,mi);

        if (invalidate==Invalidate.OFF){
            item = cacheRepository.getRaw(keyName);
        }else {
            if (invalidate==Invalidate.REFRESH)
                item=null;
            else{

                cacheRepository.removeRaw(keyName);
                if (mi.getMethod().getReturnType().isPrimitive())
                    return PrimitiveDefaults.getDefault(mi.getMethod().getReturnType());
                return null;
            }
        }


        if (item == null) {
            if (cacheInfo.type() != CacheType.ASYNC_ONLY) {
                return makeCache(keyName, cacheInfo, mi);
            } else {
                makeAsyncCache(keyName, cacheInfo, mi);
                return null;
            }
        } else {
            if (item instanceof CacheItem) {
                CacheItem cache = (CacheItem) item;
                if (isExpiredCache(cache)) {
                    return expireCache(keyName, cache, cacheInfo, mi);
                } else {
                    LOG.debug("cache not expired {}", cache.getExpireTime());
                    setExpire(cache);
                    return cache.getValue();
                }
            }
        }
        return item;
    }

    private Object expireCache(String keyName, CacheItem item, Cache cacheInfo, MethodInvocation mi) {
        if (cacheInfo.type() == CacheType.ASYNC_ONLY)
            return null;

        if (cacheInfo.type() == CacheType.SYNC) {
            cacheRepository.removeRaw(keyName);
            return makeCache(keyName, cacheInfo, mi);
        } else {
            Object value = item.getValue();
            makeAsyncCache(keyName, cacheInfo, mi);
            return value;
        }
    }

    protected void makeExpiredCache(String keyName, Cache cacheinfo, MethodInvocation mi) {
        CacheItem cache = (CacheItem) cacheRepository.getRaw(keyName);
        if (cache == null || isExpiredCache(cache)) {
            makeCache(keyName, cacheinfo, mi);
        }

    }

    private Object makeCache(String keyName, Cache cacheInfo, MethodInvocation mi) {
        Object result = null;
        try {
            if (cacheInfo.compress())
                result = compress(mi.proceed());
            else
                result = mi.proceed();
        } catch (Exception e) {
            if (cacheInfo.setOnError())
                cacheRepository.removeRaw(keyName);
            LOG.error("Fail to make a Async cache : {}", e);
        } catch (Throwable e) {
            LOG.error("Fail to make a Async cache, error : {}", e);
            throw new Error(e);
        }
        set(keyName, result, cacheInfo);
        return result;

    }

    protected void makeAsyncCache(final String keyName, final Cache cacheinfo, MethodInvocation mi) {
        asyncWorker.submitAsync(()->{
            try {
                makeExpiredCache(keyName, cacheinfo, mi);
            } catch (Exception e) {
                LOG.warn("Cache set exception {}", e);
            }
        });
    }

    protected boolean isExpiredCache(CacheItem item) {
        return LocalDateTime.now().isAfter(item.getExpireTime());
    }

    @Override
    public HttpCacheSupport getHttpCache() {
        return httpCacheSupport;
    }

    @Override
    public void setHttpCache(HttpCacheSupport cacheSupport) {

        isHttpCacheSupport = (cacheSupport != null);
        httpCacheSupport = cacheSupport;
        setInvalidationMode();
    }

    public Object compress(Object rawData) throws Throwable{
        return Compress.toGzipByte(((String)rawData).getBytes());
    }

    public void setSupportInvalidateRequest(boolean isSupport){
        invalidationMode=isSupport;
        setInvalidationMode();
    }

    private void setInvalidationMode(){
        isSupportInvalidateRequest=invalidationMode && isHttpCacheSupport;
    }

}
