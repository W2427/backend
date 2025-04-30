package com.ose.auth.domain.model.repository;

import com.ose.auth.entity.User;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户 CRUD 操作接口。
 */
@Transactional
public interface UserRepository extends PagingAndSortingWithCrudRepository<User, Long> {

    /**
     * 通过 ID 取得用户信息。
     *
     * @param id 用户 ID
     * @return 用户信息
     */
    User findByIdAndDeletedIsFalse(Long id);

    /**
     * 通过登录用户名取得用户信息。
     *
     * @param username 登录用户名
     * @return 用户信息
     */
    User findByUsernameAndDeletedIsFalse(String username);

    User findFirstByDeletedIsFalse();

    /**
     * 通过手机号码取得用户信息。
     *
     * @param mobile 手机号码
     * @return 用户信息
     */
    User findByMobileAndDeletedIsFalse(String mobile);

    /**
     * 通过电子邮箱地址取得用户信息。
     *
     * @param email 电子邮箱地址
     * @return 用户信息
     */
    User findByEmailAndDeletedIsFalse(String email);

}
