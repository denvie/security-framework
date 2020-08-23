/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.gateway.client;

import cn.denvie.security.gateway.exception.ApiException;

/**
 * 安全网关客户端服务。
 *
 * @author denvie
 * @since 2020/8/23
 */
public interface SecurityGatewayClientService {
    /**
     * post请求。
     */
    String post(ApiInvokeParam.Builder paramBuilder) throws ApiException;

    /**
     * post请求。
     */
    <T> T post(ApiInvokeParam.Builder paramBuilder, Class<T> clazz) throws ApiException;
}
