/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.gateway.service;

import cn.denvie.security.gateway.model.ApiResponse;

/**
 * 请求响应服务。
 *
 * @author denvie
 * @since 2020/8/23
 */
public interface ResponseService {
    /**
     * 请求成功响应。
     */
    ApiResponse success(Object data);

    /**
     * 请求成功响应。
     */
    ApiResponse success(String code, String message, Object data);

    /**
     * 请求失败响应。
     */
    ApiResponse error(String code, String message, Object data);
}
