package com.tmoncorp.apipotion.core.cache;

public class EmptyCache {

    private static EmptyCache instance = new EmptyCache();

    private EmptyCache() {

    }

    public static EmptyCache getInstance() {
        return instance;
    }
}
