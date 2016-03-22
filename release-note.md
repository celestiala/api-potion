# Release Note

## 2016-03-14
### module_tmon_mobile_api_util_spring 0.1.9

### new features
>    @CacheParam : 매서드 파라미터중 CacheKey로 사용하지 않을 인자 선택 가능
>    CacheStorage 타입 추가 : 로컬 메모리 지원
>    Security Util : Hash / Security utils
>    setOnError() : null 리턴시 캐시 갱신 하지 않도록 하는 기능 (Async 전용)

### Bug fix
>    [fatal] AsyncList Util Timeout bugfix

### Changes
>    CacheType 에서 Memory 제거됨, CacheStorageType으로 변경
>    async list performance improvement
 
## 2016-3-15
### module_tmon_mobile_api_util_jersey 0.1.4
>    [fix] AsyncList Util Timeout bugfix

##  2016-2-5
### module_tmon_mobile_api_util_jersey 0.1.4
>    Spring + jersey 기능통합, 리펙토링
>    Gzip Compress Support
>    Gzip Cache Compress Support
>    HTTP Cache Control (Header) Support 

## 2015-10-23 
### module_tmon_mobile_api_util_common 0.1.1
### module_tmon_mobile_api_util_spring 0.1.5
### module_tmon_mobile_api_util_jersey 0.1.2
 
>    새로운 기능
>    DeviceInfo 추가됨 ClientInfo.getDeviceInfo()
>    API UserAgent 값을 통해 Device Info 가져오는 기능 추가
>    수정, 개선
>    [fatal] 일부 async pool 에서 예기치 않은 오류 발생시 ClientInfo를 초기화하지 못하여 메모리 leak 발생하는 버그 수정
>    일부 로그 Loglevel 수정, 불필요한 로그 제거
>    http://repos.tmonc.net/cgit/cgit.cgi//module_tmon_mobile_api_util.git/commit/?id=ba10d06463f4d3aada5e9f18564e472f8b2609bf
>    http://repos.tmonc.net/cgit/cgit.cgi//module_tmon_mobile_api_util.git/commit/?id=24f8871ce0d1887b37d2e3a84819ab86c801c402