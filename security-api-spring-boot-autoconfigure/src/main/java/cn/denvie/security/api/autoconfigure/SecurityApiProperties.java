/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.api.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Security properties.
 *
 * @author denvie
 * @since 2020/8/22
 */
@ConfigurationProperties(prefix = "security.api")
public class SecurityApiProperties {
}
