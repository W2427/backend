package com.ose.tasks.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.api.UserSignatureAPI;
import com.ose.tasks.domain.model.service.UserSignatureInterface;
import com.ose.tasks.dto.SignatureDTO;
import com.ose.tasks.entity.UserSignature;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "用户电子签名接口")
@RestController
public class UserSignatureController extends BaseController implements UserSignatureAPI {

    private final UserSignatureInterface userSignatureService;

    private final UploadFeignAPI uploadFeignAPI;

    /**
     * 构造方法。
     */
    @Autowired
    public UserSignatureController(
        UserSignatureInterface userSignatureService,
        UploadFeignAPI uploadFeignAPI
    ) {
        this.userSignatureService = userSignatureService;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    @Override
    @Operation(
        summary = "上传用户电子签名",
        description = "上传用户电子签名"
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<UserSignature> uploadSignature(
        @RequestBody SignatureDTO signatureDTO
    ) {
        return new JsonObjectResponseBody<>(
            userSignatureService.uploadSignature(
                signatureDTO,
                uploadFeignAPI,
                getContext().getOperator()
            )
        );
    }

    @Override
    @Operation(
        summary = "获取用户电子签名",
        description = "获取用户电子签名"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<UserSignature> userSignature(
        @PathVariable @Parameter(description = "用户ID") Long userId
    ) {
        return new JsonObjectResponseBody<>(
            userSignatureService.userSignature(
                userId
            )
        );
    }

    @Override
    @Operation(
        summary = "更新用户电子签名",
        description = "更新用户电子签名"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<UserSignature> updateUserSignature(
        @PathVariable @Parameter(description = "ID") Long id,
        @RequestBody SignatureDTO signatureDTO
    ) {
        return new JsonObjectResponseBody<>(
            userSignatureService.updateUserSignature(
                id,
                signatureDTO,
                uploadFeignAPI,
                getContext().getOperator()
            )
        );
    }
}
