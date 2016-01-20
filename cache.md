#Cache
@Cache를 붙이면 된다. 

> public @interface Cache {
> 
>    int DEFAULT_EXPIRETIME = 5 * 60; //seconds
>
>    int expiration() default DEFAULT_EXPIRETIME;  //캐시 만료시간
>
>    CacheType type() default CacheType.SYNC; // 캐시 종류 (Sync / Async, AsyncOnly) AsyncOnly는 요청시 캐시된 값이 없으면 null 을 반환한다. 
>
>    CacheStorage storage() default CacheStorage.MEMCACHED; // 캐시 저장공간 종류 : Local Memory, Memcached
>
>    boolean isPlatformDependent() default true; // 접속 플랫폼의 종류에 따라 다른 캐시를 구워야 하는지 여부 
>
>    boolean compress() default false; // Gzip 압축 지원 여부
>
>    boolean setOnError() default true; // 캐시 생성(메소드 실행)시에 문제가 생긴 경우, 기존 캐시를 계속 사용할지 덮어 쓸지 여부, 비동기 모드에서만 동작한다. 
>
>    String name() default ""; // 메서드명과 클래스명을 조합해 자동으로 생성하는 캐시 이름 대신에 유저가 지정한 캐시 이름을 사용한다. 단, 전달된 파라미터는 자동으로 만든다. 
>
>    HttpCacheType browserCache() default HttpCacheType.EXPIRE_TIME; // 브라우저 캐시헤더 종류 설정 
> }


> public enum CacheType {
>     SYNC, ASYNC, ASYNC_ONLY // 동기, 비동기(캐시 없을때 동기), 무조건 비동기
> }

> public enum CacheStorage {
>     LOCAL,                // 로컬 메모리
>     MEMCACHED,            // Memcached
>     LOCAL_MEMCACHED_BOTH  // 로컬 메모리에 있으면 로컬메모리, 없으면 Memcached (1 - 2차 Cache)
> }

* 캐시 사용시 주의사항
Parameter로 전달되는 값은 일정한 범위를 가지는 값이어야 한다. 
너무나 다향한 인자가 전달되는 경우 캐시가 의미를 가지지 못하거나, 메모리 사용량이 과도하게 많을 수 있으므로 주의해야 한다. 

LocalCache의 경우 갱신은 하지만, 만료된 캐시를 따로 제거하지 않는다. 또, 용량제한이 없으므로 메모리 관리에 있어서 유의해야 한다. 
 
 
## Spring
><dependency>
> <groupId>com.tmoncorp</groupId>
> <artifactId>module_tmon_mobile_api_util_spring</artifactId>
> <version>0.1.7-SNAPSHOT</version>
></dependency>

### Cache On/Off
/cache/mode/ON
/cache/mode/OFF
 
### applicationProperty.properties 파일에서 설정할 내용
cache.prefix=apiprefix
ClassName이 같은 경우 cache key가 겹칠 수 있어 구분하기 위해 cache.prefix를 지정해 두어야 한다.
지정하지 않은경우 key는 기본적으로 cache가 붙으나 권장하지 않음 
 
## Jersey
> <dependency>
>  <groupId>com.tmoncorp</groupId>
>  <artifactId>module_tmon_mobile_api_util_jersey</artifactId>
>  <version>0.1.2-SNAPSHOT</version>
> </dependency>
 
### Cache On/Off
/v1/cache/mode/ON
/v1/cache/mode/OFF