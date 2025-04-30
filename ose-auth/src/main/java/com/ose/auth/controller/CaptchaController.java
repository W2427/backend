package com.ose.auth.controller;

import com.ose.auth.api.CaptchaAPI;
import com.ose.auth.domain.model.service.CaptchaInterface;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.util.CaptchaUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "人机测试接口")
@RestController
public class CaptchaController extends BaseController implements CaptchaAPI {

    // 图形验证码操作服务
    private final CaptchaInterface captchaService;

    /**
     * 构造方法。
     * @param captchaService 图形验证码操作服务
     */
    @Autowired
    public CaptchaController(CaptchaInterface captchaService) {
        this.captchaService = captchaService;
    }

    /**
     * 刷新图形验证码。
     */
    @Override
    @Operation(
        summary = "刷新图形验证码",
        description = "生成的验证码针对客户端用户代理软件及远程 IP 地址有效，因此更换浏览器或场所可能会导致验证失败。"
    )
    @RequestMapping(
        method = POST,
        value = "/captcha",
        produces = APPLICATION_JSON_VALUE
    )
    public JsonObjectResponseBody<CaptchaUtils.CaptchaData> refresh() {

        ContextDTO context = getContext();

        CaptchaUtils.CaptchaData captchaData = CaptchaUtils.generateImageOfText(
            context.getUserAgent(),
            context.getRemoteAddr(),
            captchaService.getCaptchaTTL()
        );

        captchaService.save(captchaData);

        return new JsonObjectResponseBody<>(captchaData);
    }

    /**
     * 校验图形验证码。
     */
    @Override
    @Operation(
        summary = "校验图形验证码",
        description = "客户端仅可执行一次校验。<br>"
            + "若识别错误或图形验证码信息不存在则返回访问令牌无效错误；"
            + "若已过期则返回访问令牌过期错误；"
            + "若图形验证码已被校验过则返回访问被拒绝错误。"
    )
    @RequestMapping(
        method = POST,
        value = "/captcha/{captchaId}/verify",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody verify(
        @PathVariable @Parameter(description = "图形验证码 ID") Long captchaId,
        @RequestBody @Parameter(description = "图形验证码校验数据")
            CaptchaUtils.CaptchaDTO captchaDTO
    ) {

        ContextDTO context = getContext();

        captchaDTO.setId(captchaId);

        captchaService.verify(
            context.getUserAgent(),
            context.getRemoteAddr(),
            captchaDTO
        );

        return new JsonResponseBody();
    }

}
