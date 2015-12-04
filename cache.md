#Cache
@Cache를 붙이면 된다. 

>public @interface Cache {
>   
> public static final int DEFAULT_EXPIRETIME = 5*60; //seconds
> int expiration() default DEFAULT_EXPIRETIME;
> CacheType type() default CacheType.SYNC;
> boolean isPlatformDependent() default true;
> 
> String name() default ""; //name 은 지정하지 않으면, 자동으로 생성하고 지정한 경우 해당 이름으로 생성한다. 
>
>}
 
 
>public enum CacheType {
>   
>   SYNC,ASYNC,ASYNC_ONLY, // Spring 
> MEMCACHE, MEMORY, COMPOSITE; // Jersey
>
>}
 
 
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