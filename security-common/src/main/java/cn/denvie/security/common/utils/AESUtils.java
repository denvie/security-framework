/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * AES对称加解密工具。
 *
 * @author denvie
 * @since 2020/8/23
 */
public class AESUtils {
    private static final String ALGORITHM = "AES";
    private static final String CHARSET = "UTF-8";

    /**
     * AES加密算法加密（结果为16进制编码）
     */
    public static String encrypt(String seed, String key) throws Exception {
        byte[] rawData = seed.getBytes(CHARSET);
        byte[] rawKey = key.getBytes(CHARSET);
        byte[] result = encrypt(rawData, rawKey);
        return Hex.encodeHexString(result);
    }

    /**
     * AES加密算法加密（结果为Base64编码）
     */
    public static String encryptToBase64(String seed, String key) throws Exception {
        byte[] rawKey = key.getBytes(CHARSET);
        byte[] result = encrypt(seed.getBytes(CHARSET), rawKey);
        return Base64.encodeBase64String(result);
    }

    /**
     * AES解密（解密16进制字符串）
     */
    public static String decryptString(String content, String key) throws Exception {
        byte[] byteData = Hex.decodeHex(content);
        byte[] byteKey = key.getBytes(CHARSET);
        byte[] result = decrypt(byteData, byteKey);
        return new String(result, CHARSET);
    }

    /***
     * AES解密（解密Base64字符串）
     */
    public static String decryptStringFromBase64(String base64Content, String key) throws Exception {
        byte[] byteData = Base64.decodeBase64(base64Content.getBytes(CHARSET));
        byte[] byteKey = key.getBytes(CHARSET);
        byte[] result = decrypt(byteData, byteKey);
        return new String(result, CHARSET);
    }

    private static byte[] encrypt(byte[] byteData, byte[] byteKey) throws Exception {
        return doFinal(byteData, byteKey, Cipher.ENCRYPT_MODE);
    }

    private static byte[] decrypt(byte[] byteData, byte[] byteKey) throws Exception {
        return doFinal(byteData, byteKey, Cipher.DECRYPT_MODE);
    }

    private static byte[] doFinal(byte[] byteData, byte[] byteKey, int opmode) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(byteKey, ALGORITHM);
        cipher.init(opmode, keySpec);
        return cipher.doFinal(byteData);
    }

    /**
     * 根据种子生成AES密钥。
     */
    public static byte[] generateAESKey(byte[] seedBytes) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(seedBytes);
        keyGenerator.init(128, secureRandom); // 192 and 256 bits may not be available
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    /**
     * 根据种子生成AES密钥。
     */
    public static byte[] generateAESKey(String seed) throws Exception {
        return generateAESKey(seed.getBytes(CHARSET));
    }

    ///////////////////////////////////////////////////////////////////////////
    // Test
    ///////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) throws Exception {
        String keySrc = "Denvie";
        String text = "AES Test By Denvie";

        // 16进制加解密
        byte[] keyBytes = generateAESKey(keySrc);
        System.out.println("HexKey: " + Hex.encodeHexString(keyBytes));
        String key = new String(keyBytes, CHARSET);
        String encrypt = encrypt(text, key);
        System.out.println("加密：" + encrypt);
        System.out.println("解密：" + decryptString(encrypt, key));

        // Base64加解密
        String keyString = MD5Utils.md5(keySrc, 16);
        System.out.println("StringKey: " + keyString);
        String encrypt2 = encryptToBase64(text, keyString);
        System.out.println("加密：" + encrypt2);
        System.out.println("解密：" + decryptStringFromBase64(encrypt2, keyString));
    }
}