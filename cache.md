# Cache
Just Put @Cache Annotation to your method. 

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Inherited
    @Documented
    public @interface Cache {
    
       int DEFAULT_EXPIRETIME = 5 * 60; //seconds
    
       int expiration() default DEFAULT_EXPIRETIME;  //Expire time
    
       CacheType type() default CacheType.SYNC; // Type of cache refresh (Sync / Async, AsyncOnly) 
                                                   AsyncOnly type return null if cache is not exist. 
    
       CacheStorage storage() default CacheStorage.MEMCACHED; // Cache Storage Types : Local Memory, Memcached
    
       boolean isPlatformDependent() default true; // Support Client Type 
    
       boolean compress() default false; // Gzip Support
    
       boolean setOnError() default true; // do nothing when exception occur, if this value is false.  
    
       String name() default ""; // manual cache name instead of auto generated name, however name of parameter value will be auto generated whether this value is set or not. 
    
       HttpCacheType browserCache() default HttpCacheType.EXPIRE_TIME; // Type of HTTP cache (https://www.w3.org/Protocols/rfc2616/rfc2616-sec13.html)
       
       Invalidate invalidate() default Invalidate.OFF; // Cache invalidate mode, default value is OFF
                                                       // invalidation is activate when invalidate parameter is true
                                                       // REFRESH : regenerate cache data
                                                       // DELETE : just delete cache value
                                                       // DELETE mode returns null, 0, false by return type of method 
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @Inherited
    @Documented
    public @interface CacheParam {
        boolean ignore() default false;    // sets ignore method parameter or not 
                                           // example: method(String a, @CacheParam(ignore=true) String b) 
                                           
        boolean invalidate() default false; // invalidate parameter. Default : false, ignore == true if invalidate = true ( even set ignore = false)
                                            // invalidate parameter value type should boolean type, invalidate if pass parameter value is true. 
    }
    
    public enum CacheType {
        SYNC, ASYNC, ASYNC_ONLY // synchronous : cache data making thread is same as request thread, 
                                   asynchronous + synchronous: make cache on background thread if cache is expired, make cache on request thread if no cache
                                   asynchronous only : make cache always on background thread
    }
    
    public enum CacheStorage {
        LOCAL,                // Local memory, Java Heap
        MEMCACHED,            // Memcached
        LOCAL_MEMCACHED_BOTH  // 1st Local memory, 2nd memcached
    }
    
    public enum Invalidate {
        OFF,             // invalidate off
        REFRESH,         // regenerate cache when invalidate parameter value is true 
        DELETE           // delete cache when invalidate paramter value is true, returns null, 0, false
    }


>    * Caution
>    Parameter should not very various scope value. If it is, it consume a lot of memory. 
>
>    When you use LocalCache, cache value always refresh, not delete cache when expired. There are no memory limit.  
 
 
     Invalidate example
     @Cache(invalidate = Invalidate.REFRESH)
         public long getCachedTime(@CacheParam(invalidate = true) boolean invalidate){
             try {
                 Thread.sleep(1000);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             return System.currentTimeMillis();
         }
         
     @RequestMapping(method = RequestMethod.GET, value = "/cachedValue")
         @ResponseBody
         public long getCache() {
             return testService.getCachedTime(false);                    //returns cached value
         }
     
     @RequestMapping(method = RequestMethod.GET, value = "/invalidate")
     @ResponseBody
     public long getCacheInvalidate() {
             return testService.getCachedTime(true);                     //return re-generated cache, delete if Invalidate.DELETE
     }
 
## Spring
    <dependency>
     <groupId>com.tmoncorp</groupId>
     <artifactId>api-potion-spring</artifactId>
     <version>0.0.1</version>
    </dependency>

### Cache On/Off
/cache/mode/ON
/cache/mode/OFF
 
### applicationProperty.properties 
cache.prefix=apiprefix
 
## Jersey
    <dependency>
     <groupId>com.tmoncorp</groupId>
     <artifactId>api-potion-jersey</artifactId>
     <version>0.0.1</version>
    </dependency>
 
### Cache On/Off
/v1/cache/mode/ON
/v1/cache/mode/OFF

### Refresh Cache Value
?refresh