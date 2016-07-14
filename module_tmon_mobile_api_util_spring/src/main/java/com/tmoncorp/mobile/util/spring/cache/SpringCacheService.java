package com.tmoncorp.mobile.util.spring.cache;

import com.tmoncorp.mobile.util.common.async.AsyncWorker;
import com.tmoncorp.mobile.util.common.cache.Cache;
import com.tmoncorp.mobile.util.common.cache.CacheRepository;
import com.tmoncorp.mobile.util.common.cache.CacheService;
import com.tmoncorp.mobile.util.common.clientinfo.ClientInfo;
import com.tmoncorp.mobile.util.spring.clientinfo.ClientInfoService;
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
    protected void makeAsyncCache(final String keyName, final Cache cacheinfo, MethodInvocation mi) {
        final ClientInfo info = ClientInfoService.getInfo();
        Runnable cacheRequest = () -> {
            ClientInfoService.setInfo(info);
            try {
                makeExpiredCache(keyName, cacheinfo, mi);
            } catch (Exception e) {
                LOG.warn("Cache set exception {}", e);
            } finally {
                ClientInfoService.clean();
            }
        };

        asyncWorker.submitAsync(cacheRequest);

    }
}
