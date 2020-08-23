/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.gateway.service;

import cn.denvie.security.gateway.model.ApiRequest;
import cn.denvie.security.gateway.model.ApiResponse;

/**
 * API调用异常处理器。
 *
 * @author denvie
 * @since 2020/8/23
 */
public interface InvokeExceptionHandler {
    /**
     * 处理API调用异常。
     *
     * @param apiRequest API请求参数
     * @param e          异常实例
     */
    ApiResponse handle(ApiRequest apiRequest, Throwable e);
}
