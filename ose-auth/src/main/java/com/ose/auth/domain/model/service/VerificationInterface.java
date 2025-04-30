package com.ose.auth.domain.model.service;

import com.ose.auth.vo.VerificationPurpose;

/**
 * 验证码接口。
 */
public interface VerificationInterface {

    /**
     * 发送电子邮件验证信息。
     *
     * @param email   电子邮箱地址
     * @param purpose 验证目的
     */
    void sendEmailVerification(String email, VerificationPurpose purpose);

    /**
     * 发送短信验证信息。
     *
     * @param mobile  手机号码
     * @param purpose 验证目的
     */
    void sendSMSVerification(String mobile, VerificationPurpose purpose);

    /**
     * 校验电子邮件/短信验证码。
     *
     * @param account 账号
     * @param code    验证码
     * @param purpose 验证目的
     */
    void verifyVerification(String account, String code, VerificationPurpose purpose);

}
