package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmMaterialCertificateSearchDTO;
import com.ose.material.entity.MmMaterialCertificate;
import org.springframework.data.domain.Page;

public interface MmMaterialCertificateRepositoryCustom {
    Page<MmMaterialCertificate> search(
        Long orgId,
        Long projectId,
        MmMaterialCertificateSearchDTO searchBaseDTO
        );
}
