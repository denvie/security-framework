/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.gateway.exception;

import cn.denvie.security.gateway.model.ApiCode;

/**
 * API网关异常。
 *
 * @author denvie
 * @since 2020/8/23
 */
public class ApiException extends Exception {
    private String code;

    public ApiException(ApiCode apiCode) {
        super(apiCode.message());
        this.code = apiCode.code();
    }

    public ApiException(String message) {
        super(message);
        this.code = ApiCode.FAILURE.code();
    }

    public ApiException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public int getCodeInt() {
        try {
            return Integer.parseInt(code);
        } catch (NumberFormatException e) {
            return Integer.parseInt(ApiCode.FAILURE.code());
        }
    }
}
