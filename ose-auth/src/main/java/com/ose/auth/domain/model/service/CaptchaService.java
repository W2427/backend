package com.ose.auth.domain.model.service;

import com.ose.auth.config.SecurityConfiguration;
import com.ose.auth.domain.model.repository.CaptchaRepository;
import com.ose.auth.entity.Captcha;
import com.ose.exception.AccessDeniedError;
import com.ose.exception.AccessTokenExpiredError;
import com.ose.exception.AccessTokenInvalidError;
import com.ose.util.BeanUtils;
import com.ose.util.CaptchaUtils;
import com.ose.util.LongUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 图形验证码服务。
 */
@Component
@EnableConfigurationProperties(SecurityConfiguration.class)
public class CaptchaService implements CaptchaInterface {

    // 图形验证码 TTL（毫秒）
    private final long captchaTTL;

    // 图形验证码数据仓库
    private final CaptchaRepository captchaRepository;

    /**
     * 构造方法。
     *
     * @param securityConfiguration 安全配置
     */
    @Autowired
    public CaptchaService(
        SecurityConfiguration securityConfiguration,
        CaptchaRepository captchaRepository
    ) {
        this.captchaTTL = securityConfiguration.getCaptchaTtlMS();
        this.captchaRepository = captchaRepository;
    }

    /**
     * 取得图形验证码 TTL（毫秒）。
     *
     * @return 图形验证码 TTL（毫秒）
     */
    @Override
    public long getCaptchaTTL() {
        return captchaTTL;
    }

    /**
     * 保存图形验证码数据。
     *
     * @param captchaData 图形验证码数据
     * @return 图形验证码实体数据
     */
    @Override
    public Captcha save(CaptchaUtils.CaptchaData captchaData) {
        Captcha captcha = new Captcha(captchaData.getId());
        BeanUtils.copyProperties(captchaData, captcha);
        return captchaRepository.save(captcha);
    }

    /**
     * 取得（有效的）图形验证码信息。
     *
     * @param captchaId 图形验证码数据实体 ID
     * @return 图形验证码数据实体
     */
    private Captcha get(Long captchaId) {

        Captcha captcha = captchaRepository.findById(captchaId).orElse(null);

        if (captcha == null) {
            throw new AccessTokenInvalidError();
        }

        if (captcha.getValidUntil() < System.currentTimeMillis()) {
            captchaRepository.delete(captcha);
            throw new AccessTokenExpiredError();
        }

        return captcha;
    }

    /**
     * 校验图形验证码。
     * 若图形验证码不存在或已过期或已被校验过则返回错误并删除存在的图形验证码；
     * 否则，将图形验证码更新为已校验状态。
     *
     * @param userAgent  客户端用户代理字符串
     * @param remoteAddr 客户端远程 IP 地址
     * @param captchaDTO 图形验证码验证数据
     */
    @Override
    public void verify(
        String userAgent,
        String remoteAddr,
        CaptchaUtils.CaptchaDTO captchaDTO
    ) {

        Captcha captcha = get(captchaDTO.getId());

        if (captcha.isVerified()) {
            throw new AccessDeniedError();
        }

        captcha.setVerified(true);

        captchaRepository.save(captcha);

        CaptchaUtils.verify(userAgent, remoteAddr, captchaDTO);
    }

    /**
     * 取得并消费已生成的图形验证码。
     *
     * @param userAgent  客户端用户代理字符串
     * @param remoteAddr 客户端远程 IP 地址
     * @param captchaDTO 图形验证码验证数据
     */
    @Override
    public void consume(
        String userAgent,
        String remoteAddr,
        CaptchaUtils.CaptchaDTO captchaDTO
    ) {
        captchaRepository.delete(get(
            LongUtils.parseLong(CaptchaUtils.verify(userAgent, remoteAddr, captchaDTO, false))
        ));
    }

}
