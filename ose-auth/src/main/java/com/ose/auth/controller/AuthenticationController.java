package com.ose.auth.controller;

import com.ose.auth.annotation.CheckPrivilege;
import com.ose.auth.api.AuthenticationAPI;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.domain.model.repository.UserRepository;
import com.ose.auth.domain.model.service.AccessTokenInterface;
import com.ose.auth.domain.model.service.UserAgentInterface;
import com.ose.auth.domain.model.service.UserInterface;
import com.ose.auth.dto.CredentialDTO;
import com.ose.auth.entity.User;
import com.ose.auth.entity.UserProfile;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.exception.SharePointConnectionParamException;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.util.DateUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Date;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "用户认证/授权接口")
@RestController
public class AuthenticationController extends BaseController implements AuthenticationAPI {

    // 临时访问令牌有效时长（秒）
    @Value("${application.security.temporary-access-token-ttl}")
    private int temporaryAccessTokenTTL;

    // 用户代理字符串服务
    private final UserAgentInterface userAgentService;

    // 用户操作服务
    private final UserInterface userService;

    // 用户访问令牌服务
    private final AccessTokenInterface accessTokenService;

    private final UserRepository userRepository;

    private final UserFeignAPI userFeignAPI;

    /**
     * 构造方法。
     *
     * @param userAgentService   用户代理字符串服务
     * @param userService        用户操作服务
     * @param accessTokenService 用户访问令牌服务
     * @param repository
     * @param userFeignAPI
     */
    @Autowired
    public AuthenticationController(
        UserAgentInterface userAgentService,
        UserInterface userService,
        AccessTokenInterface accessTokenService,
        UserRepository repository, UserFeignAPI userFeignAPI) {
        this.userAgentService = userAgentService;
        this.userService = userService;
        this.accessTokenService = accessTokenService;
        this.userRepository = repository;
        this.userFeignAPI = userFeignAPI;
    }

    /**
     * 用户登录。
     *
     * @param credentials 用户登录凭证
     * @return 登录授权数据
     */
    @Override
    @Operation(
        summary = "用户登录认证",
        description = "获取访问令牌。"
    )
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<UserProfile> authenticate(
        @RequestBody @Valid CredentialDTO credentials
    ) throws SharePointConnectionParamException {


        User user = userRepository.findByUsernameAndDeletedIsFalse(credentials.getUsername());
//        if (user == null) {
//            boolean success;
//            String domain = "oceanstarmo";
//            Pair<String, String> token = SharePointUtil.login(credentials.getUsername(), credentials.getPassword(), domain);
//            success = token != null;
//
//            // 调用sharePointUtil类的connect方法，查看用户信息是否能够登录
//            if (success) {
//                UserDTO userDTO = new UserDTO();
//                userDTO.setName(credentials.getUsername());
//                userDTO.setUsername(credentials.getUsername());
//                userDTO.setPassword(credentials.getPassword());
//                userDTO.setType("administrator");
//                userFeignAPI.create(userDTO);
//                CredentialDTO credentialDTO = new CredentialDTO();
//                credentialDTO.setUsername(credentials.getUsername());
//                credentialDTO.setPassword(credentials.getPassword());
//                authenticate(credentialDTO);
//            }
//        }

        ContextDTO context = getContext();

        Long userAgentId = userAgentService.fetchId(context.getUserAgent());

        UserProfile userProfile = userService.authenticate(credentials);

        return (new JsonObjectResponseBody<>(
            context,
            userProfile
        ))
            .setAccessToken(
                accessTokenService.create(
                    userProfile,
                    context.getRemoteAddr(),
                    context.getUserAgent(),
                    userAgentId
                )
            )
            .setIncluded(userService);
    }

    /**
     * 注销登录。
     *
     * @param accessToken 用户访问令牌
     */
    @Override
    @Operation(
        summary = "用户注销登录",
        description = "销毁访问令牌。"
    )
    public JsonResponseBody destroyAuthorization(
        String accessToken
    ) {

        ContextDTO context = getContext();

        accessTokenService.destroy(
            (accessTokenService.claim(
                context.getRemoteAddr(),
                context.getUserAgent(),
                userAgentService.fetchId(context.getUserAgent()),
                accessToken
            )).getAccessTokenId()
        );

        return new JsonResponseBody();
    }

    /**
     * 生成临时访问令牌。
     */
    @Override
    @Operation(
        summary = "生成临时访问令牌",
        description = "临时访问令牌将通过 <code>X-Temp-Access-Token</code> 响应头返回，"
            + "临时访问令牌的有效截止时间将通过 <code>X-Temp-Access-Token-Expires-At</code> 响应头返回。"
            + "在后续请求中通过 <code>access-token</code> 查询参数传递临时访问令牌。"
    )
    @CheckPrivilege
    public JsonResponseBody generateTemporaryAccessToken() {

        ContextDTO context = getContext();

        HttpServletResponse response = context.getResponse();

        Date expireAt = new Date(System.currentTimeMillis() + temporaryAccessTokenTTL);

        response.setHeader(
            "X-Temp-Access-Token",
            context.generateTemporaryAccessToken(expireAt)
        );

        response.setHeader(
            "X-Temp-Access-Token-Expires-At",
            DateUtils.toISOString(expireAt)
        );

        response.setHeader(
            "Access-Control-Expose-Headers",
            "X-Temp-Access-Token,X-Temp-Access-Token-Expires-At"
        );

        return new JsonResponseBody();
    }

}
