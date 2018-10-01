package com.celestiala.apipotion.jersey.cache;

import com.celestiala.apipotion.core.cache.CacheRepository;
import com.celestiala.apipotion.core.cache.CacheService;

public class JerseyCacheRepository extends CacheService {

    public JerseyCacheRepository(CacheRepository repository) {
        super(repository, (run) -> CacheAsyncRunner.getInstance().submitAsync(run));
    }

}
