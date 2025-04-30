package com.ose.auth.api;

import com.ose.response.JsonResponseBody;
import com.ose.util.CaptchaUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 用户信息接口。
 */
@FeignClient(name = "ose-auth", contextId = "captchaFeign")
public interface CaptchaAPI {

    /**
     * 刷新图形验证码。
     */
    @RequestMapping(
        method = POST,
        value = "/captcha",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody refresh();

    /**
     * 校验图形验证码。
     */
    @RequestMapping(
        method = POST,
        value = "/captcha/{captchaId}/verify",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody verify(
        @PathVariable("captchaId") Long captchaId,
        @RequestBody CaptchaUtils.CaptchaDTO captchaDTO
    );

}
