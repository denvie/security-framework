/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.gateway.model;

/**
 * 调用方使用的Code。
 *
 * @author denvie
 * @since 2020/8/23
 */
public class InvokeCode {
    private String code;
    private String message;

    public InvokeCode() {
    }

    public InvokeCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
