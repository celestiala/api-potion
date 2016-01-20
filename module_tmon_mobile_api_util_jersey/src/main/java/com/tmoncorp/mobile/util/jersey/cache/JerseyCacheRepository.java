package com.tmoncorp.mobile.util.jersey.cache;

import com.tmoncorp.mobile.util.common.cache.CacheRepository;
import com.tmoncorp.mobile.util.common.cache.CacheService;
import com.tmoncorp.mobile.util.jersey.async.AsyncRunner;

public class JerseyCacheRepository extends CacheService {

    public JerseyCacheRepository(CacheRepository repository) {
        super(repository, (run) -> AsyncRunner.getInstance().submitAsync(run));
    }

}
