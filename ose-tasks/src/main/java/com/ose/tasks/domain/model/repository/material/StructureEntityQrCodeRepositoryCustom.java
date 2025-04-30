package com.ose.tasks.domain.model.repository.material;

import com.ose.tasks.dto.material.StructureEntityQrCodeCriteriaDTO;
import com.ose.tasks.entity.material.StructureEntityQrCode;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 * 结构实体二维码查询接口。
 */
@Transactional
public interface StructureEntityQrCodeRepositoryCustom {
    Page<StructureEntityQrCode> search(Long orgId, Long projectId, StructureEntityQrCodeCriteriaDTO criteriaDTO);
}
