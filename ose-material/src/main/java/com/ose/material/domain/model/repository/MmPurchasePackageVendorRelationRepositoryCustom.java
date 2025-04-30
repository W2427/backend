package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmPurchasePackageVendorReturnDTO;
import com.ose.material.dto.MmPurchasePackageVendorSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;


/**
 * 采购包和供货商关系。
 */
@Transactional
public interface MmPurchasePackageVendorRelationRepositoryCustom {

    Page<MmPurchasePackageVendorReturnDTO> search(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        MmPurchasePackageVendorSearchDTO mmPurchasePackageVendorSearchDTO);

}
