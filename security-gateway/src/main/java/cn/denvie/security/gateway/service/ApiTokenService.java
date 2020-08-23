/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.gateway.service;

import cn.denvie.security.gateway.entity.ApiToken;
import cn.denvie.security.gateway.exception.ApiException;
import cn.denvie.security.gateway.model.TokenParam;

/**
 * ApiToken服务。
 *
 * @author denvie
 * @since 2020/8/23
 */
public interface ApiTokenService {
    /**
     * 生成Token并持久化存储。
     *
     * @param param 参数
     * @return ApiToken
     */
    ApiToken createToken(TokenParam param) throws ApiException;

    /**
     * 根据token获取对应的ApiToken实例。
     *
     * @param token token值
     * @return ApiToken
     */
    ApiToken getToken(String token);

    /**
     * 更新指定Token失效时间
     *
     * @param token Token
     */
    void updateExpireTimeByToken(String token);
}
