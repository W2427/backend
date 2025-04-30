package com.ose.auth.domain.model.service;

import com.ose.auth.entity.Captcha;
import com.ose.util.CaptchaUtils;

/**
 * 图形验证码接口。
 */
public interface CaptchaInterface {

    /**
     * 取得图形验证码 TTL（毫秒）。
     *
     * @return 图形验证码 TTL（毫秒）
     */
    long getCaptchaTTL();

    /**
     * 保存图形验证码数据。
     *
     * @param captchaData 图形验证码数据
     * @return 图形验证码实体数据
     */
    Captcha save(CaptchaUtils.CaptchaData captchaData);

    /**
     * 校验图形验证码。
     * 若图形验证码不存在或已过期或已被校验过则返回错误并删除存在的图形验证码；
     * 否则，将图形验证码更新为已校验状态。
     *
     * @param userAgent  客户端用户代理字符串
     * @param remoteAddr 客户端远程 IP 地址
     * @param captchaDTO 图形验证码验证数据
     */
    void verify(
        String userAgent,
        String remoteAddr,
        CaptchaUtils.CaptchaDTO captchaDTO
    );

    /**
     * 取得并消费已生成的图形验证码。
     *
     * @param userAgent  客户端用户代理字符串
     * @param remoteAddr 客户端远程 IP 地址
     * @param captchaDTO 图形验证码验证数据
     */
    void consume(
        String userAgent,
        String remoteAddr,
        CaptchaUtils.CaptchaDTO captchaDTO
    );

}
