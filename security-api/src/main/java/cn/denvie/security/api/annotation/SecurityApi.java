/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.api.annotation;

import java.lang.annotation.*;

/**
 * 安全Api注解。
 *
 * @author denvie
 * @since 2020/8/23
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SecurityApi {
}
