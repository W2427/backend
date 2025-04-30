package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmPurchasePackageFileSearchDTO;
import com.ose.material.entity.MmPurchasePackageFileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 采购包文件数据仓库。
 */
public interface MmPurchasePackageFileCustom {

    /**
     * 查询采购包文件。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param purchasePackageId   采购包 ID
     * @param criteriaDTO 查询条件
     * @param pageable    分页参数
     * @return 任务包分页数据
     */
    Page<MmPurchasePackageFileEntity> search(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        MmPurchasePackageFileSearchDTO criteriaDTO,
        Pageable pageable
    );

}
