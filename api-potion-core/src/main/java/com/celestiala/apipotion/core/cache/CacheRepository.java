package com.celestiala.apipotion.core.cache;

public interface CacheRepository {
    void setRaw(String keyName, Object value, int expire);

    Object getRaw(String keyName);

    void removeRaw(String keyName);

    CacheStorage getStorageType();
}
