/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.common.annotation;

import java.lang.annotation.*;

/**
 * 安全API注解，用来声明对外暴露的接口。
 *
 * @author denvie
 * @since 2020/8/23
 */
/*
SpringBean被cglib动态代理后导致自定义注解丢失问题解决方案：
1.将spring.aop.proxy-target-class=true 去掉， 自动使用JDK代理。
2.使用注解解析器工具org.springframework.core.annotation.AnnotationUtils
 */
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SecurityApi {
    /**
     * Api Name
     */
    String value();

    /**
     * 是否需要登录鉴权
     */
    boolean needLogin() default true;

    /**
     * 参数名称列表
     */
    String[] paramNames() default {};
}
