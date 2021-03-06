/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.gateway.repository;

import cn.denvie.security.gateway.entity.ApiToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * ApiToken Repository。
 *
 * @author denvie
 * @since 2020/8/23
 */
@Repository
public interface ApiTokenRepository extends JpaRepository<ApiToken, Long> {
    /**
     * 根据用户Id查找所有的ApiToken。
     *
     * @param userId 用户Id
     * @return List<ApiToken>
     */
    List<ApiToken> findAllByUserId(String userId);

    /**
     * 根据用户Id、设备类型、设备标识查找ApiToken。
     *
     * @param userId     用户Id
     * @param clientType 客户端类别，android、ios、web...
     * @param clientCode 设备标识
     * @return ApiToken
     */
    ApiToken findByUserIdAndClientTypeAndClientCode(String userId, String clientType, String clientCode);

    /**
     * 根据token值查找ApiToken。
     *
     * @param accessToken token值
     * @return ApiToken
     */
    ApiToken findByAccessToken(String accessToken);

    /**
     * 根据用户Id删除ApiToken。
     *
     * @param userId 用户Id
     * @return int
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM ApiToken AT WHERE AT.userId = :userId")
    int deleteByUserIdEquals(@Param("userId") String userId);

    /**
     * 更新指定Token失效时间
     *
     * @param token      Token
     * @param expireTime 新的失效时间
     */
    @Transactional
    @Modifying
    @Query("UPDATE ApiToken AT SET AT.expireTime = :expireTime WHERE AT.accessToken = :token")
    void updateExpireTimeByToken(@Param("token") String token, @Param("expireTime") long expireTime);
}
