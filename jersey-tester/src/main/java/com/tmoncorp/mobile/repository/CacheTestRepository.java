package com.celestiala.mobile.repository;


public class CacheTestRepository {

	private boolean isSet=false;

	public String makeValue(){
		return String.valueOf(System.currentTimeMillis());
	}

	public String makeOnce(){
		if (!isSet) {
			isSet=true;
			return makeValue();
		}
		throw new RuntimeException();
	}
}
