package com.tmoncorp.apipotion.spring.cache;

import com.tmoncorp.apipotion.core.async.AsyncWorker;
import com.tmoncorp.apipotion.core.cache.Cache;
import com.tmoncorp.apipotion.core.cache.CacheRepository;
import com.tmoncorp.apipotion.core.cache.CacheService;
import com.tmoncorp.apipotion.core.clientinfo.ClientInfo;
import com.tmoncorp.apipotion.spring.clientinfo.ClientInfoService;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringCacheService extends CacheService {

    private static final Logger LOG = LoggerFactory.getLogger(SpringCacheService.class);
    private final AsyncWorker asyncWorker;

    public SpringCacheService(CacheRepository repo, AsyncWorker worker) {
        super(repo, worker);
        asyncWorker = worker;

    }

    @Override
    protected void appendEnvName(StringBuilder builder, Cache cacheInfo) {
        if (cacheInfo.isPlatformDependent()) {
            builder.append(KEY_SEPERATOR);
            builder.append(ClientInfoService.getInfo().getPlatform());
        }
    }

    @Override
    protected void beforeMakeAsyncCache(Object info){
        ClientInfoService.setInfo((ClientInfo)info);
    }

    protected void afterMakeAsyncCache(){
        ClientInfoService.clean();
    }

    @Override
    protected void makeAsyncCache(final String keyName, final Cache cacheinfo, MethodInvocation mi) {
        final ClientInfo info = ClientInfoService.getInfo();
        makeAsyncCache(keyName,cacheinfo,mi,info);

    }
}
