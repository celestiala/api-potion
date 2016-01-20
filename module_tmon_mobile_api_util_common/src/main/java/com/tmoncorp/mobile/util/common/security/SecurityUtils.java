package com.tmoncorp.mobile.util.common.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

public class SecurityUtils {

    public final static String MD5 = "MD5";
    public final static String SHA1 = "SHA-1";

    private final static String CHIPER_ALGORITHM = "AES";
    private final static String CHIPER_NAME = "AES/ECB/PKCS5Padding";
    private final static String CHAR_ENCODING = "UTF-8";

    private static final Logger LOG = LoggerFactory.getLogger(SecurityUtils.class);

    private SecurityUtils() {
        throw new AssertionError("static utility class");
    }

    public static StringBuilder byteToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xff & bytes[i]);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString;
    }

    public static StringBuilder getHash(String str, String algorithm) {

        return getHash(str.getBytes(), algorithm);
    }

    public static StringBuilder getHash(byte[] str, String algorithm) {
        return byteToHex(getHashByte(str, algorithm));
    }

    public static byte[] getHashByte(byte[] str, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            return md.digest(str);
        } catch (Exception e) {
            LOG.warn("could not make hash string, {}", e.getMessage());
            return null;
        }
    }

    private static byte[] getAesCipher(byte[] input, byte[] key, int mode) {

        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, CHIPER_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CHIPER_NAME);
            cipher.init(mode, keySpec);
            return cipher.doFinal(input);
        } catch (Exception e) {
            LOG.warn("AES Cipher Exception occurred, {}", e.getMessage());
            return null;
        }
    }

    public static String encryptAES(String input, byte[] key) {
        return Base64.getEncoder().encodeToString(encryptAES(input.getBytes(), key));
    }

    public static String decryptAES(String input, byte[] key) {
        byte[] inputRaw = Base64.getDecoder().decode(input);
        return new String(decryptAES(inputRaw, key));
    }

    public static byte[] encryptAES(byte[] input, byte[] key) {
        return getAesCipher(input, key, Cipher.ENCRYPT_MODE);
    }

    public static byte[] decryptAES(byte[] input, byte[] key) {
        return getAesCipher(input, key, Cipher.DECRYPT_MODE);
    }

    public static String getMD5String(String str) {
        return getHash(str, MD5).toString();
    }

    public static String getSHA1String(String str) {
        return getHash(str, SHA1).toString();
    }
}
