package com.tmoncorp.apipotion.core.async;

public interface AsyncTask<K, V> {
    public V async(K key);

}
