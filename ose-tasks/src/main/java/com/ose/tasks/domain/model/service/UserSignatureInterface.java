package com.ose.tasks.domain.model.service;

import com.ose.docs.api.UploadFeignAPI;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.SignatureDTO;
import com.ose.tasks.entity.UserSignature;

/**
 * service接口
 */
public interface UserSignatureInterface {

    /**
     * 上传用户电子签名
     *
     * @param signatureDTO
     * @param uploadFeignAPI
     * @param operatorDTO
     * @return
     */
    UserSignature uploadSignature(
        SignatureDTO signatureDTO,
        UploadFeignAPI uploadFeignAPI,
        OperatorDTO operatorDTO
    );

    /**
     * 查询用户电子签名
     *
     * @param userId
     * @return
     */
    UserSignature userSignature(
        Long userId
    );

    /**
     * 更新电子签名
     *
     * @param id
     * @param signatureDTO
     * @param uploadFeignAPI
     * @param operator
     * @return
     */
    UserSignature updateUserSignature(
        Long id,
        SignatureDTO signatureDTO,
        UploadFeignAPI uploadFeignAPI,
        OperatorDTO operator
    );
}
