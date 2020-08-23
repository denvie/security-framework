/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.gateway.service;

import cn.denvie.security.gateway.model.ApiRequest;

/**
 * 签名服务，定义签名生成的规则及实现。
 *
 * @author denvie
 * @since 2020/8/23
 */
public interface SubSignatureService {
    /**
     * 执行签名。
     *
     * @param param 请求参数
     * @return 经过签名后的字符串
     */
    String sign(ApiRequest param);
}
