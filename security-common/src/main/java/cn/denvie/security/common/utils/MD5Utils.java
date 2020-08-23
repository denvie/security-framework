/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.common.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;

/**
 * MD5加密工具。
 *
 * @author denvie
 * @since 2020/8/23
 */
public class MD5Utils {
    private static final String ALGORITHM = "MD5";
    private static final String CHARSET = "UTF-8";

    /**
     * 对字符串md5加密。
     *
     * @param text 传入要加密的字符串
     * @return 32位MD5加密后的字符串
     */
    public static String md5(String text) {
        return md5(text, 32);
    }

    /**
     * 对字符串md5加密。
     *
     * @param text 需要加密的字符串
     * @param bit  加密的类型（16：16位；32：32位；64：Base64）
     * @return MD5加密后的字符串
     */
    public static String md5(String text, Integer bit) {
        String md5 = "";
        try {
            // 创建加密对象
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            if (bit == 64) {
                byte[] digest = md.digest(text.getBytes(CHARSET));
                md5 = Base64.encodeBase64String(digest);
            } else {
                // 计算MD5函数
                md.update(text.getBytes());
                byte[] digestBytes = md.digest();
                int i;
                StringBuilder sb = new StringBuilder();
                for (byte digestByte : digestBytes) {
                    i = digestByte;
                    if (i < 0) {
                        i += 256;
                    }
                    if (i < 16) {
                        sb.append("0");
                    }
                    sb.append(Integer.toHexString(i));
                }
                md5 = sb.toString();
                if (bit == 16) {
                    // 截取32位md5为16位
                    md5 = md5.substring(8, 24);
                    return md5;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Test
    ///////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        String pwd = "security password";
        System.out.println(md5(pwd, 16));
        System.out.println(md5(pwd, 32));
        System.out.println(md5(pwd, 64));
    }
}
