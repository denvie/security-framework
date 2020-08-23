/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.gateway.service;

import cn.denvie.security.common.utils.RSAUtils;
import cn.denvie.security.common.utils.RandomUtils;
import cn.denvie.security.gateway.entity.ApiToken;
import cn.denvie.security.gateway.exception.ApiException;
import cn.denvie.security.gateway.model.ApiCode;
import cn.denvie.security.gateway.model.EncryptType;
import cn.denvie.security.gateway.model.MultiDeviceLogin;
import cn.denvie.security.gateway.model.TokenParam;
import cn.denvie.security.gateway.properties.SecurityGatewayProperties;
import cn.denvie.security.gateway.repository.ApiTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * ApiToken服务实现。
 *
 * @author denvie
 * @since 2020/8/23
 */
@Service
@Transactional
public class ApiTokenServiceImpl implements ApiTokenService {

    private static final Logger logger = LoggerFactory.getLogger(ApiTokenService.class);

    @Autowired
    private ApiTokenRepository apiTokenRepository;
    @Autowired
    SecurityGatewayProperties securityGatewayProperties;

    @Override
    public ApiToken createToken(TokenParam param) throws ApiException {
        logger.debug("Create ApiToken, param = " + param);

        // 参数校验
        validateTokenParam(param);

        // 多设备登录检测
        ApiToken apiToken = checkMultiDeviceLogin(param);

        if (apiToken == null) {
            apiToken = new ApiToken();
            apiToken.setCreateTime(System.currentTimeMillis());
        }

        apiToken.setUserId(param.getUserId());
        apiToken.setUserName(param.getUserName());
        apiToken.setClientType(param.getClientType());
        apiToken.setClientCode(param.getClientCode());
        apiToken.setAccessToken(RandomUtils.generateUuid());
        apiToken.setClientIp(param.getClientIp());
        apiToken.setExpireTime(System.currentTimeMillis() + securityGatewayProperties.getTokenValidTime());
        apiToken.setExt1(param.getExt1());
        apiToken.setExt2(param.getExt2());

        // 生成密钥
        generateSecretKey(apiToken);

        // 保存到数据库
        try {
            apiTokenRepository.save(apiToken);
        } catch (Exception e) {
            logger.error(ApiCode.TOKEN_SAVE_TO_DB_ERROR.message(), e);
            throw new ApiException(ApiCode.TOKEN_SAVE_TO_DB_ERROR);
        }

        logger.debug("Create ApiToken Success, token = " + apiToken.getAccessToken());

        return apiToken;
    }

    @Override
    public ApiToken getToken(String token) {
        if (StringUtils.isEmpty(token)) return null;

        return apiTokenRepository.findByAccessToken(token);
    }

    @Override
    public void updateExpireTimeByToken(String token) {
        if (StringUtils.isEmpty(token)) return;

        long expireTime = System.currentTimeMillis() + securityGatewayProperties.getTokenValidTime();
        apiTokenRepository.updateExpireTimeByToken(token, expireTime);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private Method
    ///////////////////////////////////////////////////////////////////////////

    private void validateTokenParam(TokenParam param) throws ApiException {
        if (param == null) {
            throw new ApiException(ApiCode.TOKEN_PARAM_NULL);
        } else if (StringUtils.isEmpty(param.getUserId())) {
            throw new ApiException(ApiCode.TOKEN_PARAM_USER_ID_NULL);
        } else if (StringUtils.isEmpty(param.getUserName())) {
            throw new ApiException(ApiCode.TOKEN_PARAM_USER_NAME_NULL);
        } else if (StringUtils.isEmpty(param.getClientType())) {
            throw new ApiException(ApiCode.TOKEN_PARAM_CLIENT_TYPE_NULL);
        } else if (StringUtils.isEmpty(param.getClientCode())) {
            throw new ApiException(ApiCode.TOKEN_PARAM_CLIENT_CODE_NULL);
        }
    }

    private ApiToken checkMultiDeviceLogin(TokenParam param) throws ApiException {
        ApiToken apiToken = null;
        if (securityGatewayProperties.getMultiDeviceLogin() == MultiDeviceLogin.REPLACE) {
            // 删除原有ApiToken数据，挤掉原来的登录信息
            int deleteCount = apiTokenRepository.deleteByUserIdEquals(param.getUserId());
            logger.debug("DELETE FROM ApiToken count: " + deleteCount);
        } else if (securityGatewayProperties.getMultiDeviceLogin() == MultiDeviceLogin.REFUSE) {
            // 如果用户已登录，拒绝新的登录请求
            boolean isAlreadyLogin = false;
            List<ApiToken> allApiTokenEntities = apiTokenRepository.findAllByUserId(param.getUserId());
            if (allApiTokenEntities != null && !allApiTokenEntities.isEmpty()) {
                for (ApiToken at : allApiTokenEntities) {
                    if (!at.isExpired()) {
                        isAlreadyLogin = true;
                        break;
                    }
                }
            }
            if (isAlreadyLogin) {
                throw new ApiException(ApiCode.TOKEN_DUPLICATE_LOGIN);
            }
        } else {
            // 允许多台设备同时登录
            // 根据用户Id、设备类型、设备标识查找ApiToken
            apiToken = apiTokenRepository.findByUserIdAndClientTypeAndClientCode(
                    param.getUserId(), param.getClientType(), param.getClientCode());
        }
        return apiToken;
    }

    private void generateSecretKey(ApiToken apiToken) throws ApiException {
        if (securityGatewayProperties.getEncryptType() == EncryptType.AES) {
            // 如果没配置aesKey，则由程序生成
            if (StringUtils.isEmpty(securityGatewayProperties.getAesKey())) {
                apiToken.setSecret(RandomUtils.generateSecret());
            } else {
                apiToken.setSecret(securityGatewayProperties.getAesKey());
            }
        } else if (securityGatewayProperties.getEncryptType() == EncryptType.RSA) {
            // 如果没配置rsaPublicKey和rsaPrivateKey，则由程序生成
            if (StringUtils.isEmpty(securityGatewayProperties.getRsaPublicKey())
                    || StringUtils.isEmpty(securityGatewayProperties.getRsaPrivateKey())) {
                try {
                    Map<String, String> keyMap = RSAUtils.generateRSAKeyBase64();
                    String privateKey = keyMap.get(RSAUtils.KEY_PRIVATE);
                    String publicKey = keyMap.get(RSAUtils.KEY_PUBLIC);
                    apiToken.setSecret(publicKey);
                    apiToken.setPrivateSecret(privateKey);
                } catch (NoSuchAlgorithmException e) {
                    logger.error(ApiCode.TOKEN_SECRET_KEY_CREATE_ERROR.message(), e);
                    throw new ApiException(ApiCode.TOKEN_SECRET_KEY_CREATE_ERROR);
                }
            } else {
                apiToken.setSecret(securityGatewayProperties.getRsaPublicKey());
                apiToken.setPrivateSecret(securityGatewayProperties.getRsaPrivateKey());
            }
        }
    }

}
