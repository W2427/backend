package com.ose.auth.domain.model.repository;

import com.ose.auth.entity.Verification;
import com.ose.auth.vo.VerificationPurpose;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 电子邮件/短信验证码 CRUD 操作接口。
 */
@Transactional
public interface VerificationRepository extends CrudRepository<Verification, Long> {

    /**
     * 删除验证码信息。
     *
     * @param account 账号
     * @param purpose 验证目的
     */
    void deleteByAccountAndPurpose(String account, VerificationPurpose purpose);

    /**
     * 取得电子邮件/短信验证码。
     *
     * @param account 账号
     * @param code    验证码
     * @param purpose 验证目的
     * @return 电子邮件/短信验证码数据实体
     */
    Verification findByAccountAndCodeAndPurpose(String account, String code, VerificationPurpose purpose);

}
