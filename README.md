# api-potion
API Development tool : Drop api potion to your API servers

[Cache](cache.md) : Cache Utility (Memory, Memcache)
You can use local memory / memcached cache easily. Put @Cache annotation is needed.

[Async Util](async.md) : Asynchronous Utility

[ClientInfo](client.md) : Request Handler Util

[Http Client](http-client.md): HTTP Client for API Request
Http Client helper

## Build
You can test with snapshot, official release will be soon

### Maven build

#### Spring framework
     <dependency>
       <groupId>com.github.tmoncorp</groupId>
       <artifactId>api-potion-spring</artifactId>
       <version>0.0.1-SNAPSHOT</version>
     </dependency>

#### Jersey framework
     <dependency>
       <groupId>com.github.tmoncorp</groupId>
       <artifactId>api-potion-jersey</artifactId>
       <version>0.0.1-SNAPSHOT</version>
     </dependency>

## Copyright and license
Code released under [the MIT license](LICENSE). Docs released under [Creative Commons](https://creativecommons.org/licenses/by/4.0/)