package com.ose.material.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.material.dto.MmCompanyVendorCreateDTO;
import com.ose.material.dto.MmVendorSearchDTO;
import com.ose.material.entity.MmVendorEntity;
import org.springframework.data.domain.Page;

/**
 * 公司级供货商接口
 */
public interface MmCompanyVendorInterface {

    /**
     * 获取公司级供货商。
     */
    Page<MmVendorEntity> search(
        Long companyId,
        MmVendorSearchDTO mmVendorSearchDTO
    );

    /**
     * 创建公司级供货商。
     *
     * @param companyId
     * @param mmCompanyVendorCreateDTO
     * @return
     */
    MmVendorEntity create(
        Long companyId,
        MmCompanyVendorCreateDTO mmCompanyVendorCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 更新公司级供货商。
     *
     * @param companyId
     * @param vendorId
     * @param mmCompanyVendorCreateDTO
     * @return
     */
    MmVendorEntity update(
        Long companyId,
        Long vendorId,
        MmCompanyVendorCreateDTO mmCompanyVendorCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 公司级供货商详情。
     *
     * @param companyId
     * @param purchasePackageId
     * @return
     */
    MmVendorEntity detail(
        Long companyId,
        Long purchasePackageId
    );

    /**
     * 删除公司级供货商。
     *
     * @param companyId
     * @param vendorId
     */
    void delete(
        Long companyId,
        Long vendorId,
        ContextDTO contextDTO
    );

}
