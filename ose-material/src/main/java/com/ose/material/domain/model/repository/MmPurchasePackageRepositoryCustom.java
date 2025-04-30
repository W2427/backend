package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmPurchasePackageSearchDTO;
import com.ose.material.entity.MmPurchasePackageEntity;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;


/**
 * 采购包。
 */
@Transactional
public interface MmPurchasePackageRepositoryCustom {

    Page<MmPurchasePackageEntity> search(
        Long orgId,
        Long projectId,
        MmPurchasePackageSearchDTO mmPurchasePackageSearchDTO
    );

}
