# Cache
@Cache를 붙이면 된다. 

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Inherited
    @Documented
    public @interface Cache {
    
       int DEFAULT_EXPIRETIME = 5 * 60; //seconds
    
       int expiration() default DEFAULT_EXPIRETIME;  //캐시 만료시간
    
       CacheType type() default CacheType.SYNC; // 캐시 종류 (Sync / Async, AsyncOnly) AsyncOnly는 요청시 캐시된 값이 없으면 null 을 반환한다. 
    
       CacheStorage storage() default CacheStorage.MEMCACHED; // 캐시 저장공간 종류 : Local Memory, Memcached
    
       boolean isPlatformDependent() default true; // 접속 플랫폼의 종류에 따라 다른 캐시를 구워야 하는지 여부 
    
       boolean compress() default false; // Gzip 압축 지원 여부
    
       boolean setOnError() default true; // 캐시 생성(메소드 실행)시에 문제가 생긴 경우, 기존 캐시를 계속 사용할지 덮어 쓸지 여부, 비동기 모드에서만 동작한다. 
    
       String name() default ""; // 메서드명과 클래스명을 조합해 자동으로 생성하는 캐시 이름 대신에 유저가 지정한 캐시 이름을 사용한다. 단, 전달된 파라미터는 자동으로 만든다. 
    
       HttpCacheType browserCache() default HttpCacheType.EXPIRE_TIME; // 브라우저 캐시헤더 종류 설정 
       
       Invalidate invalidate() default Invalidate.OFF; // Cache invalidate mode 설정, 기본값 OFF, REFRESH 는 invalidate 파라미터로 true 전달시 캐시 재생성하고, DELETE는 삭제한다. 
                                                       // DELETE 모드시에 타입에 따라 널 또는 0, false가 리턴된다. 
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @Inherited
    @Documented
    public @interface CacheParam {
        boolean ignore() default false;    // 전달되는 파라미터를 캐시키로 사용하지 않을지 설정한다
                                           // 예: method(String a, @CacheParam(ignore=true) String b) b값이 어떤 값이 전달되든 a값이 같을때는 같은 캐시 값을 전달
                                           
        boolean invalidate() default false; // invalidate를 위해 전달되는 파라미터인지 여부, 기본값 false, invalidate = true 일때 이 파라미터는 캐시키로 사용되지 않는다. (ignore = false 더라도 true로 인식)
                                            // invalidate를 위해 전달되는 파라미터는 boolean 형이어야 하고, true 전달시 invalidate 한다. 
    }
    
    public enum CacheType {
        SYNC, ASYNC, ASYNC_ONLY // 동기, 비동기(캐시 없을때 동기), 무조건 비동기
    }
    
    public enum CacheStorage {
        LOCAL,                // 로컬 메모리
        MEMCACHED,            // Memcached
        LOCAL_MEMCACHED_BOTH  // 로컬 메모리에 있으면 로컬메모리, 없으면 Memcached (1 - 2차 Cache)
    }
    
    public enum Invalidate {
        OFF,             // invalidate 기능 사용하지 않음
        REFRESH,         // invalidate true 전달시 캐시 재생성하고 해당 값 리턴
        DELETE           // 캐시 삭제하고 널, 0, false 리턴
    }


>    * 캐시 사용시 주의사항
>    Parameter로 전달되는 값은 일정한 범위를 가지는 값이어야 한다. 
>    너무나 다향한 인자가 전달되는 경우 캐시가 의미를 가지지 못하거나, 메모리 사용량이 과도하게 많을 수 있으므로 주의해야 한다. 
>
>    LocalCache의 경우 갱신은 하지만, 만료된 캐시를 따로 제거하지 않는다. 또, 용량제한이 없으므로 메모리 관리에 있어서 유의해야 한다. 
 
 
     Invalidate 예제 
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
             return testService.getCachedTime(false);                    //캐시된 값 리턴
         }
     
     @RequestMapping(method = RequestMethod.GET, value = "/invalidate")
     @ResponseBody
     public long getCacheInvalidate() {
             return testService.getCachedTime(true);                     //캐시를 새로 생성하여 리턴, Invalidate.DELETE 모드인경우 삭제만 수행
     }
 
## Spring
    <dependency>
     <groupId>com.tmoncorp</groupId>
     <artifactId>module_tmon_mobile_api_util_spring</artifactId>
     <version>0.1.13-SNAPSHOT</version>
    </dependency>

### Cache On/Off
/cache/mode/ON
/cache/mode/OFF
 
### applicationProperty.properties 파일에서 설정할 내용
cache.prefix=apiprefix
ClassName이 같은 경우 cache key가 겹칠 수 있어 구분하기 위해 cache.prefix를 지정해 두어야 한다.
지정하지 않은경우 key는 기본적으로 cache가 붙으나 권장하지 않음 
 
## Jersey
    <dependency>
     <groupId>com.tmoncorp</groupId>
     <artifactId>module_tmon_mobile_api_util_jersey</artifactId>
     <version>0.1.5-SNAPSHOT</version>
    </dependency>
 
### Cache On/Off
/v1/cache/mode/ON
/v1/cache/mode/OFF

### 캐시 갱신
?refresh