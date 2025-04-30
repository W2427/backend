package com.ose.auth.domain.model.repository;

import com.ose.auth.entity.AccessToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户访问令牌 CRUD 操作接口。
 */
@Transactional
public interface AccessTokenRepository extends CrudRepository<AccessToken, Long> {

    /**
     * 根据所有者和用户代理字符串取得用户访问令牌。
     *
     * @param userId       所有者用户 ID
     * @param userAgentMd5 用户代理字符串（不包含版本信息）MD5 摘要
     * @return 访问令牌信息
     */
    AccessToken findByUserIdAndUserAgentMd5(Long userId, String userAgentMd5);

}
