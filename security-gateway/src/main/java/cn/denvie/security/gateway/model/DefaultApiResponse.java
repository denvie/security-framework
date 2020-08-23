/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.gateway.model;

import java.io.Serializable;

/**
 * Api请求响应结果的默认定义。
 *
 * @author denvie
 * @since 2020/8/23
 */
public class DefaultApiResponse<Data> implements ApiResponse, Serializable {
    private String code;
    private String message;
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
