/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.gateway.context;

import cn.denvie.security.gateway.model.ApiRequest;
import cn.denvie.security.gateway.model.InvokeCode;

/**
 * API接口请求拦截器。
 *
 * @author denvie
 * @since 2020/8/23
 */
public interface ApiInvokeInterceptor {
    /**
     * 请求前回调。
     *
     * @param request 请求的封装Bean
     * @param args    请求参数
     * @return 如果返回的InvokeCode不为null将中断接口的调用
     */
    InvokeCode before(ApiRequest request, Object[] args);

    /**
     * 请求出错回调。
     *
     * @param request 请求的封装Bean
     * @param t       错误实例
     */
    void error(ApiRequest request, Throwable t);

    /**
     * 请求结束回调。
     *
     * @param request 请求的封装Bean
     * @param result  请求结果
     */
    void after(ApiRequest request, Object result);
}
