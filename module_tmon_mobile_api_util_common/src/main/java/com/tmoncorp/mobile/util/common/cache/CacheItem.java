package com.tmoncorp.mobile.util.common.cache;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CacheItem implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7575221851155451839L;
	private LocalDateTime expireTime;
	private String etag;
	private Object value;

	public LocalDateTime getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(LocalDateTime expireTime) {
		this.expireTime = expireTime;
	}

	public String getEtag() {
		return etag;
	}

	public void setEtag(String etag) {
		this.etag = etag;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
