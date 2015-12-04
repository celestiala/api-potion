#Async Util

## Spring - AsyncService
 
public <T,K> List<T> processAsyncList(List<K> datas,AsyncTask<K,T> call)
List<K> 받아서, AsyncTask<K,T> 수행하고 List<K>리스트 순서대로 정렬된 <T,K> List<T>를 반환
 
public <K,T> List<T> processAsyncMergeList(List<K> datas,AsyncTask<K,List<T>> call)
List<K> 받아서, AsyncTask<K,List<T> 수행하고 List<T>가 머지된 List<T> 반환 
 
사용 예)
하나의 key값을 받아서, 수행된 값을 반환하는 매서드가 있을때,
키값의 리스트와 매서드 호출 방법을 전달하여,  여러번 수행된 리스트를 얻는다. 이때 이 작업은 병렬로 수행되며, 순서가 보장된다.  
@Autowired
private AsyncService asyncService;

public List<MartDeal> getDealInfo(List<Long> dealNoList) {

   return asyncService.processAsyncList(dealNoList,key -> martDealService.getDealInfo(key));
}
 
## Jersey 
사용 방법은 위와 동일
AsyncRunner 사용