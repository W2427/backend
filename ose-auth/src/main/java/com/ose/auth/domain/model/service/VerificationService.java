package com.ose.auth.domain.model.service;

import com.ose.util.*;
import com.ose.auth.config.SecurityConfiguration;
import com.ose.auth.domain.model.repository.UserProfileRepository;
import com.ose.auth.domain.model.repository.VerificationRepository;
import com.ose.auth.entity.UserProfile;
import com.ose.auth.entity.Verification;
import com.ose.auth.vo.VerificationPurpose;
import com.ose.exception.AccessDeniedError;
import com.ose.exception.AccessTokenExpiredError;
import com.ose.exception.AccessTokenInvalidError;
import com.ose.exception.NotFoundError;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证码服务。
 */
@Component
@EnableConfigurationProperties(SecurityConfiguration.class)
public class VerificationService implements VerificationInterface {

    // 电子邮件验证码 TTL（毫秒）
    private final long emailVerificationTTL;

    // 短信验证码 TTL（毫秒）
    private final long smsVerificationTTL;

    // 用户数据仓库
    private final UserProfileRepository userProfileRepository;

    // 验证码数据仓库
    private final VerificationRepository verificationRepository;

    @Value("${application.base-url}")
    private String baseUrl;

    /**
     * 构造方法。
     */
    @Autowired
    public VerificationService(
        SecurityConfiguration securityConfiguration,
        UserProfileRepository userProfileRepository,
        VerificationRepository verificationRepository
    ) {
        emailVerificationTTL = securityConfiguration.getEmailVerificationTtlMS();
        smsVerificationTTL = securityConfiguration.getSmsVerificationTtlMS();
        this.userProfileRepository = userProfileRepository;
        this.verificationRepository = verificationRepository;
    }

    /**
     * 生成验证码。
     *
     * @param account    目标账号
     * @param purpose    验证目的
     * @param code       验证码
     * @param validUntil 有效截止时间
     * @return 验证码数据实体
     */
    private Verification create(
        String account,
        VerificationPurpose purpose,
        String code,
        long validUntil
    ) {

        verificationRepository.deleteByAccountAndPurpose(account, purpose);

        return verificationRepository.save(new Verification(
            account, purpose, code, validUntil
        ));

    }

    /**
     * 发送电子邮件验证信息。
     *
     * @param email   电子邮箱地址
     * @param purpose 验证目的
     */
    @Override
    public void sendEmailVerification(
        String email,
        VerificationPurpose purpose
    ) {

        if (VerificationPurpose.SIGN_UP != purpose) {

            UserProfile user = userProfileRepository
                .findByEmailAndDeletedIsFalse(email);

            if (user == null) {
                throw new NotFoundError("error.user-account.not-found");
            }

            if (user.getStatus() != EntityStatus.ACTIVE) {
                throw new AccessDeniedError("error.user-account.disabled");
            }

        }

        Verification verification = create(
            email,
            purpose,
            RandomUtils.getHexString(16),
            System.currentTimeMillis() + emailVerificationTTL
        );

        Map<String, Object> parameters = new HashMap<>();

        parameters.put(
            "passwordResetURL",
            String.format(
                "%s/password/reset?account=%s&verification=%s",
                baseUrl,
                StringUtils.encodeURIComponent(email),
                verification.getCode()
            )
        );

        MailUtils.send(
            email,
            "Reset Password - OSE",
            TemplateUtils.render(
                "reset password by Email",
                getClass().getResourceAsStream("/templates/email/reset-password.html"),
                parameters
            )
        );

    }

    /**
     * 发送短信验证信息。
     *
     * @param mobile  手机号码
     * @param purpose 验证目的
     */
    @Override
    public void sendSMSVerification(
        String mobile,
        VerificationPurpose purpose
    ) {

        if (VerificationPurpose.SIGN_UP != purpose) {

            UserProfile user = userProfileRepository
                .findByMobileAndDeletedIsFalse(mobile);

            if (user == null) {
                throw new NotFoundError("error.user-account.not-found");
            }

            if (user.getStatus() != EntityStatus.ACTIVE) {
                throw new AccessDeniedError("error.user-account.disabled");
            }

        }

        Verification verification = create(
            mobile,
            purpose,
            StringUtils.pad(RandomUtils.between(0, 999999), 6),
            System.currentTimeMillis() + smsVerificationTTL
        );

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("smsVerificationCode", verification.getCode());

        SMSUtils.send(
            mobile,
            TemplateUtils.render(
                "reset password by SMS",
                getClass().getResourceAsStream("/templates/sms/reset-password.txt"),
                parameters
            )
        );

    }

    /**
     * 校验电子邮件/短信验证码。
     *
     * @param account 账号
     * @param code    验证码
     * @param purpose 验证目的
     */
    @Override
    public void verifyVerification(
        String account,
        String code,
        VerificationPurpose purpose
    ) {

        Verification verification = verificationRepository
            .findByAccountAndCodeAndPurpose(account, code, purpose);

        if (verification == null) {
            throw new AccessTokenInvalidError();
        }

        if (verification.getValidUntil() < System.currentTimeMillis()) {
            throw new AccessTokenExpiredError();
        }

        verificationRepository.delete(verification);
    }

}
