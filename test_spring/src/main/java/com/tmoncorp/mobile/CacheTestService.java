package com.tmoncorp.mobile;

import com.tmoncorp.mobile.util.common.cache.Cache;
import com.tmoncorp.mobile.util.common.cache.CacheParam;
import com.tmoncorp.mobile.util.common.cache.Invalidate;
import org.springframework.stereotype.Service;

/**
 * Created by joshua on 16. 5. 24.
 */
@Service
public class CacheTestService {

    @Cache(invalidate = Invalidate.REFRESH)
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
