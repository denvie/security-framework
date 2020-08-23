/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.gateway.model;

/**
 * 加密方式。
 *
 * @author denvie
 * @since 2020/8/23
 */
public enum EncryptType {
    /**
     * 不加密
     */
    NONE,
    /**
     * Base64加密
     */
    BASE64,
    /**
     * AES加密
     */
    AES,
    /**
     * RSA加密
     */
    RSA
}
