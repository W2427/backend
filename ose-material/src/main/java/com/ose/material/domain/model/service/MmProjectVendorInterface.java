package com.ose.material.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.material.dto.MmProjectVendorCreateDTO;
import com.ose.material.dto.MmVendorSearchDTO;
import com.ose.material.entity.MmVendorEntity;
import org.springframework.data.domain.Page;

/**
 * 项目级供货商接口
 */
public interface MmProjectVendorInterface {

    /**
     * 获取项目级供货商。
     */
    Page<MmVendorEntity> search(
        Long orgId,
        Long projectId,
        MmVendorSearchDTO mmVendorSearchDTO
    );

    /**
     * 创建项目级供货商。
     *
     * @param orgId
     * @param projectId
     * @param mmProjectVendorCreateDTO
     * @return
     */
    void create(
        Long orgId,
        Long projectId,
        MmProjectVendorCreateDTO mmProjectVendorCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 项目级供货商详情。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId
     * @return
     */
    MmVendorEntity detail(
        Long orgId,
        Long projectId,
        Long purchasePackageId
    );

    /**
     * 删除项目级供货商。
     *
     * @param orgId
     * @param projectId
     * @param vendorId
     */
    void delete(
        Long orgId,
        Long projectId,
        Long vendorId,
        ContextDTO contextDTO
    );

}
