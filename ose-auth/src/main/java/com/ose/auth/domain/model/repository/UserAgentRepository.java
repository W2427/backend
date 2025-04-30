package com.ose.auth.domain.model.repository;

import com.ose.auth.entity.UserAgent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户代理字符串 CRUD 操作接口。
 */
@Transactional
public interface UserAgentRepository extends CrudRepository<UserAgent, Long> {

    /**
     * 根据用户代理字符串取得用户代理字符串信息。
     *
     * @param userAgent 用户代理字符串
     * @return 用户代理字符串信息
     */
    UserAgent findByUserAgent(String userAgent);

}
