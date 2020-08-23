/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.common.utils;

import java.util.UUID;

/**
 * 随机数生成工具。
 *
 * @author denvie
 * @since 2020/8/23
 */
public class RandomUtils {
    private static final String[] CHARS = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    /**
     * 生成UUID。
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成16位密钥。
     */
    public static String generateSecret() {
        return MD5Utils.md5(generateUuid(), 16);
    }

    /**
     * 生成8位短UUID。
     */
    public static String generateShortUuid() {
        StringBuilder shortBuilder = new StringBuilder();
        String uuid = generateUuid();
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuilder.append(CHARS[x % 0x3E]);
        }
        return shortBuilder.toString();
    }

    /**
     * 使用UUID加8位随机数盐生成Token。
     */
    public static String generateToken() {
        // 生成UUID
        String uuid = generateUuid();
        // 生成8位随机数盐
        String salt = generateShortUuid();
        return uuid + salt;
    }

    public static void main(String[] args) {
        System.out.println(generateToken());
    }
}
