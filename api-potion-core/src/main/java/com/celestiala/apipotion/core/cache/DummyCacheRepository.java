package com.celestiala.apipotion.core.cache;

public class DummyCacheRepository implements CacheRepository {

    private final CacheStorage type;

    public DummyCacheRepository(CacheStorage type){
        this.type=type;
    }

    @Override
    public void setRaw(String keyName, Object value, int expire) {

    }

    @Override
    public Object getRaw(String keyName) {
        return null;
    }

    @Override
    public void removeRaw(String keyName) {

    }

    @Override
    public CacheStorage getStorageType() {
        return type;
    }
}
