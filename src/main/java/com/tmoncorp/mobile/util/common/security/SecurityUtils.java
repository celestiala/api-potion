package com.tmoncorp.mobile.util.common.security;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SecurityUtils {

	public static String MD5="MD5";
	public static String SHA1="SHA-1";

	public static StringBuffer byteToString(byte[] bytes){
		StringBuffer hexString = new StringBuffer();
		for (int i=0;i<bytes.length;i++) {
			String hex=Integer.toHexString(0xff & bytes[i]);
			if(hex.length()==1) hexString.append('0');
			hexString.append(hex);
		}
		return hexString;

	}

	public static StringBuffer getHash(CharSequence str,String algorithm){
		CharBuffer buffer=CharBuffer.wrap(str);
		Charset charset = StandardCharsets.UTF_16;
		CharsetEncoder encoder = charset.newEncoder();
		try {
			MessageDigest md=MessageDigest.getInstance(algorithm);
			byte[] byteData = md.digest(encoder.encode(buffer).array());
			return byteToString(byteData);
		} catch (Exception e) {
			return null;
		}
	}

	public static String getMD5String(String str){
		return getHash(str,MD5).toString();
	}

	public static String getSHA1String(String str){
		return getHash(str,SHA1).toString();
	}
}
