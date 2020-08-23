/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.gateway.client;

import cn.denvie.security.gateway.context.SecurityGatewayConfig;
import cn.denvie.security.gateway.exception.ApiException;
import cn.denvie.security.gateway.model.ApiCode;
import cn.denvie.security.gateway.model.ApiRequest;
import cn.denvie.security.gateway.properties.SecurityGatewayProperties;
import cn.denvie.security.gateway.service.SubSignatureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 安全网关客户端服务实现。
 *
 * @author denvie
 * @since 2020/8/23
 */
@Service
@Slf4j
public class SecurityGatewayClientServiceImpl implements SecurityGatewayClientService {

    @Autowired
    private SecurityGatewayProperties securityGatewayProperties;

    private SubSignatureService subSignatureService;
    private RestTemplate restTemplate;

    public SecurityGatewayClientServiceImpl() {
        subSignatureService = new SecurityGatewayConfig().subSignatureService();
    }

    @Override
    public String post(ApiInvokeParam.Builder paramBuilder) throws ApiException {
        return post(paramBuilder, String.class);
    }

    @Override
    public <T> T post(ApiInvokeParam.Builder paramBuilder, Class<T> clazz) throws ApiException {
        // 处理请求地址
        if (StringUtils.isEmpty(paramBuilder.baseUrl())) {
            paramBuilder.baseUrl(securityGatewayProperties.getClientBaseUrl());
        }
        // 处理私钥
        if (StringUtils.isEmpty(paramBuilder.secret())) {
            paramBuilder.secret(securityGatewayProperties.getClientSecret());
        }

        // 参数校验
        validate(paramBuilder);

        // 构建调用参数
        ApiInvokeParam apiInvokeParam = paramBuilder.build();

        // 传进来的签名为空，则使用SubSignatureService生成签名
        if (StringUtils.isEmpty(apiInvokeParam.getSign())) {
            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setApiName(apiInvokeParam.getName());
            apiRequest.setParams(apiInvokeParam.getParams());
            apiRequest.setSecret(paramBuilder.secret());
            apiRequest.setTimestamp(apiInvokeParam.getTimestamp());
            String sign = subSignatureService.sign(apiRequest);
            apiInvokeParam.setSign(sign);
        }

        // 添加Header
        HttpHeaders headers = new HttpHeaders();
        if (paramBuilder.getHeaderMap() != null && !paramBuilder.getHeaderMap().isEmpty()) {
            for (Map.Entry<String, String> entry : paramBuilder.getHeaderMap().entrySet()) {
                headers.add(entry.getKey(), entry.getValue());
            }
        }

        // 发起请求
        ResponseEntity<T> responseEntity = getRestTemplate()
                .postForEntity(apiInvokeParam.getBaseUrl(), new HttpEntity<>(apiInvokeParam, headers), clazz);

        return responseEntity.getBody();
    }

    private void validate(ApiInvokeParam.Builder builder) throws ApiException {
        if (builder == null) {
            throw new ApiException(ApiCode.CLIENT_PARAM_ERROR);
        }
        if (StringUtils.isEmpty(builder.baseUrl())) {
            throw new ApiException(ApiCode.CLIENT_API_URL_NULL);
        }
        if (StringUtils.isEmpty(builder.name())) {
            throw new ApiException(ApiCode.CLIENT_API_NAME_NULL);
        }
        if (StringUtils.isEmpty(builder.secret())) {
            throw new ApiException(ApiCode.CLIENT_SECRET_NULL);
        }
    }

    private RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            synchronized (this) {
                if (restTemplate == null) {
                    // 设置超时时间
                    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
                    requestFactory.setReadTimeout(securityGatewayProperties.getClientReadTimeout());
                    requestFactory.setConnectTimeout(securityGatewayProperties.getClientConnectTimeout());

                    // 添加转换器
                    List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
                    messageConverters.add(new StringHttpMessageConverter(Charset.forName(securityGatewayProperties.getClientRequestCharset())));
                    messageConverters.add(new FormHttpMessageConverter());
                    messageConverters.add(new MappingJackson2HttpMessageConverter());

                    restTemplate = new RestTemplate(messageConverters);
                    restTemplate.setRequestFactory(requestFactory);
                    restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
                }
            }
        }
        return restTemplate;
    }

}
