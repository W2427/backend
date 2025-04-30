package com.ose.material.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.material.dto.MmMaterialCertificateDTO;
import com.ose.material.dto.MmMaterialCertificateFileCreateDTO;
import com.ose.material.dto.MmMaterialCertificateSearchDTO;
import com.ose.material.entity.MmMaterialCertificate;
import org.springframework.data.domain.Page;

/**
 * 材料证书。
 */
public interface MmMaterialCertificateInterface {

    Page<MmMaterialCertificate> search(
        Long orgId,
        Long projectId,
        MmMaterialCertificateSearchDTO searchBaseDTO
    );

    void create(
        Long orgId,
        Long projectId,
        MmMaterialCertificateDTO MaterialCertificateDTO,
        ContextDTO contextDTO
    );

    MmMaterialCertificate detail(
        Long orgId,
        Long projectId,
        Long materialCertificateId
    );

    void delete(
        Long orgId,
        Long projectId,
        Long materialCertificateId,
        ContextDTO contextDTO
    );

    void uploadFile(
        Long orgId,
        Long projectId,
        Long id,
        MmMaterialCertificateFileCreateDTO mmMaterialCertificateFileCreateDTO,
        ContextDTO contextDTO
    );

}
