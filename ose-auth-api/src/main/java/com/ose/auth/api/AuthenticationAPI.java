package com.ose.auth.api;

import com.ose.auth.dto.CredentialDTO;
import com.ose.auth.entity.UserProfile;
import com.ose.exception.SharePointConnectionParamException;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 用户登录/授权接口。
 */
@FeignClient(name = "ose-auth", contextId = "authFeign")
public interface AuthenticationAPI {

    /**
     * 用户登录。
     *
     * @param credentialDTO 用户登录凭证
     * @return 登录授权数据
     */
    @RequestMapping(
        method = POST,
        value = "/authorizations",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<UserProfile> authenticate(
        @RequestBody CredentialDTO credentialDTO
    ) throws SharePointConnectionParamException;

    /**
     * 用户登录。
     *
     * @param accessToken 用户访问令牌
     */
    @RequestMapping(
        method = DELETE,
        value = "/authorizations/",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody destroyAuthorization(
      String accessToken
    );


    /**
     * 生成临时访问令牌。
     */
    @RequestMapping(
        method = GET,
        value = "/temporary-access-token",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody generateTemporaryAccessToken();

}
