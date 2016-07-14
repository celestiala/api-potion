package com.tmoncorp.apipotion.jersey.cache;

import com.tmoncorp.apipotion.core.cache.CacheRepository;
import com.tmoncorp.apipotion.core.cache.CacheService;

public class JerseyCacheRepository extends CacheService {

    public JerseyCacheRepository(CacheRepository repository) {
        super(repository, (run) -> CacheAsyncRunner.getInstance().submitAsync(run));
    }

}
