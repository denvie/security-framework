/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.api.context;

import cn.denvie.security.common.annotation.SecurityApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.*;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全Api方法参数解析器（{@link HandlerMethodArgumentResolver}）。
 *
 * @author denvie
 * @since 2020/8/23
 */
@Slf4j
public class SecurityApiMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final ApplicationContext applicationContext;
    private final List<HttpMessageConverter<?>> messageConverters;
    private final HandlerMethodArgumentResolverComposite argumentResolvers;

    public SecurityApiMethodArgumentResolver(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;

        this.messageConverters = new ArrayList<>(4);
        this.messageConverters.add(new ByteArrayHttpMessageConverter());
        this.messageConverters.add(new StringHttpMessageConverter());
        this.messageConverters.add(new MappingJackson2HttpMessageConverter());
        try {
            this.messageConverters.add(new SourceHttpMessageConverter<>());
        } catch (Error err) {
            // Ignore when no TransformerFactory implementation is available
        }
        this.messageConverters.add(new AllEncompassingFormHttpMessageConverter());

        List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
        this.argumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
    }

    /**
     * 只处理标记了{@link SecurityApi}注解的Controller方法。
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasMethodAnnotation(SecurityApi.class);
    }

    /**
     * 解析参数，进行解密验签操作。
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Object result = this.argumentResolvers.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        log.debug("{}={}", parameter.getParameterName(), result);
        return result;
    }

    /**
     * Return the configured message body converters.
     */
    public List<HttpMessageConverter<?>> getMessageConverters() {
        return this.messageConverters;
    }

    /**
     * Return the list of argument resolvers to use built-in resolvers.
     * Copy from {@link RequestMappingHandlerAdapter}.getDefaultArgumentResolvers().
     */
    private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>(30);

        // Annotation-based argument resolution
        resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), false));
        resolvers.add(new RequestParamMapMethodArgumentResolver());
        resolvers.add(new PathVariableMethodArgumentResolver());
        resolvers.add(new PathVariableMapMethodArgumentResolver());
        resolvers.add(new MatrixVariableMethodArgumentResolver());
        resolvers.add(new MatrixVariableMapMethodArgumentResolver());
        resolvers.add(new ServletModelAttributeMethodProcessor(false));
        resolvers.add(new RequestResponseBodyMethodProcessor(getMessageConverters()));
        resolvers.add(new RequestPartMethodArgumentResolver(getMessageConverters()));
        resolvers.add(new RequestHeaderMethodArgumentResolver(getBeanFactory()));
        resolvers.add(new RequestHeaderMapMethodArgumentResolver());
        resolvers.add(new ServletCookieValueMethodArgumentResolver(getBeanFactory()));
        resolvers.add(new ExpressionValueMethodArgumentResolver(getBeanFactory()));
        resolvers.add(new SessionAttributeMethodArgumentResolver());
        resolvers.add(new RequestAttributeMethodArgumentResolver());

        // Type-based argument resolution
        resolvers.add(new ServletRequestMethodArgumentResolver());
        resolvers.add(new ServletResponseMethodArgumentResolver());
        resolvers.add(new HttpEntityMethodProcessor(getMessageConverters()));
        resolvers.add(new RedirectAttributesMethodArgumentResolver());
        resolvers.add(new ModelMethodProcessor());
        resolvers.add(new MapMethodProcessor());
        resolvers.add(new ErrorsMethodArgumentResolver());
        resolvers.add(new SessionStatusMethodArgumentResolver());
        resolvers.add(new UriComponentsBuilderMethodArgumentResolver());

        // Catch-all
        resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), true));
        resolvers.add(new ServletModelAttributeMethodProcessor(true));

        return resolvers;
    }

    /**
     * Return the owning factory of this bean instance, or {@code null} if none.
     */
    @Nullable
    private ConfigurableBeanFactory getBeanFactory() {
        if (this.applicationContext instanceof ConfigurableApplicationContext) {
            return ((ConfigurableApplicationContext) this.applicationContext).getBeanFactory();
        }
        return null;
    }
}
