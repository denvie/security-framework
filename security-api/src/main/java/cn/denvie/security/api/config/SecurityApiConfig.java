/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.api.config;

import cn.denvie.security.api.annotation.SecurityApiMethodArgumentResolver;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Security Api WebMvcConfigurer.
 *
 * @author denvie
 * @since 2020/8/23
 */
@Configuration
public class SecurityApiConfig implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RequestMappingHandlerAdapter handlerAdapter =
                applicationContext.getBean(RequestMappingHandlerAdapter.class);

        // fixed @RequestBody request:
        //     HttpMediaTypeNotSupportedException: Content type 'application/json;charset=UTF-8' not supported
        handlerAdapter.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        // 将自定义的HandlerMethodArgumentResolver添加到RequestMappingHandlerAdapter最前面
        SecurityApiMethodArgumentResolver securityApiMethodArgumentResolver =
                new SecurityApiMethodArgumentResolver(applicationContext);
        List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();
        argumentResolvers.add(0, securityApiMethodArgumentResolver);
        if (handlerAdapter.getArgumentResolvers() != null) {
            argumentResolvers.addAll(handlerAdapter.getArgumentResolvers());
        }
        handlerAdapter.setArgumentResolvers(argumentResolvers);
    }
}
