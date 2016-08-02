# api-potion
API Development tool : Drop api potion to your API servers

[Async Util](async.md) : Asynchronous Utility
 
[Cache](cache.md) : Cache Utility (Memory, Memcache)
You can use local memory / memcached cache easily. Put @Cache annotation is needed.

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
Code and documentation copyright 2016 Ticketmonster, Inc. Code released under [the MIT license](LICENSE). Docs released under [Creative Commons](https://creativecommons.org/licenses/by/4.0/)