package com.ose.tasks.api;

import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.dto.SignatureDTO;
import com.ose.tasks.entity.UserSignature;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 用户电子签名接口
 */
public interface UserSignatureAPI {

    /*
     * 上传用户电子签名
     */
    @RequestMapping(
        method = POST,
        value = "user-signatures",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<UserSignature> uploadSignature(
        @RequestBody SignatureDTO signatureDTO
    );

    /*
     * 获取用户电子签名
     */
    @RequestMapping(
        method = GET,
        value = "user-signatures/users/{userId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<UserSignature> userSignature(
        @PathVariable @Parameter(description = "用户ID") Long userId
    );

    /*
     * 更新用户电子签名
     */
    @RequestMapping(
        method = PATCH,
        value = "user-signatures/{id}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<UserSignature> updateUserSignature(
        @PathVariable @Parameter(description = "签名ID") Long id,
        @RequestBody SignatureDTO signatureDTO
    );

}
