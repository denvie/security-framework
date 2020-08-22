/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.api.autoconfigure;

import cn.denvie.security.api.config.SecurityApiConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Security auto configuration.
 *
 * @author denvie
 * @since 2020/8/22
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SecurityApiProperties.class)
@Import({SecurityApiConfig.class})
public class SecurityApiAutoConfiguration {
}
