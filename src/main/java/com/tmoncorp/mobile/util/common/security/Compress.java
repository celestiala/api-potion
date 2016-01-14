package com.tmoncorp.mobile.util.common.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class Compress {

	public static byte[] toGzipByte(String data){
		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());

		try {
			GZIPOutputStream gzip = new GZIPOutputStream(bos);
			gzip.write(data.getBytes());
			gzip.close();
			byte[] compressed = bos.toByteArray();
			bos.close();
			return compressed;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
