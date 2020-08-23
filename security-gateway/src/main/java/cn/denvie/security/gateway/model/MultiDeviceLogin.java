/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.gateway.model;

/**
 * 多设备登录策略。
 *
 * @author denvie
 * @since 2020/8/23
 */
public enum MultiDeviceLogin {
    /**
     * 允许多设备登录
     */
    ALLOW,
    /**
     * 将旧用户挤掉
     */
    REPLACE,
    /**
     * 用户已在一台设备登录，并且Token未失效，拒绝再次登录
     */
    REFUSE
}
