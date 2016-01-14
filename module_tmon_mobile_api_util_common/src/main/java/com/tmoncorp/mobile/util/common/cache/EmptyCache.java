package com.tmoncorp.mobile.util.common.cache;

public class EmptyCache {

	private static EmptyCache instance=new EmptyCache();
	public static EmptyCache getInstance(){
		return instance;
	}
}
