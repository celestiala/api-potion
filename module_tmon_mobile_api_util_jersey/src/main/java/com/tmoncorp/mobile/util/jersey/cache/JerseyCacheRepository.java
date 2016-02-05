package com.tmoncorp.mobile.util.jersey.cache;

import com.tmoncorp.mobile.util.common.cache.CacheRepository;
import com.tmoncorp.mobile.util.common.cache.CacheService;

public class JerseyCacheRepository extends CacheService {

    public JerseyCacheRepository(CacheRepository repository) {
        super(repository, (run) -> CacheAsyncRunner.getInstance().submitAsync(run));
    }

}
