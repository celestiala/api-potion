package com.celestiala.mobile;

import com.celestiala.apipotion.core.cache.Cache;
import com.celestiala.apipotion.core.cache.CacheParam;
import com.celestiala.apipotion.core.cache.CacheStorage;
import com.celestiala.apipotion.core.cache.Invalidate;
import org.springframework.stereotype.Service;

/**
 * Created by joshua on 16. 5. 24.
 */
@Service
public class CacheTestService {

    @Cache(invalidate = Invalidate.REFRESH,storage = CacheStorage.LOCAL)
    public long getCachedTime(@CacheParam(invalidate = true) boolean invalidate){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    @Cache(invalidate = Invalidate.DELETE)
    public long getCachedTime2(@CacheParam(invalidate = true) boolean invalidate){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }
}
