package com.ose.material.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.material.dto.MmMaterialCodeCreateDTO;
import com.ose.material.dto.MmMaterialCodeSearchDTO;
import com.ose.material.entity.MmMaterialCodeEntity;
import org.springframework.data.domain.Page;

/**
 * 物料单接口
 */
public interface MmMaterialCodeInterface {

    /**
     * 获取仓库。
     */
    Page<MmMaterialCodeEntity> search(
        Long orgId,
        Long projectId,
        MmMaterialCodeSearchDTO mmMaterialCodeSearchDTO
    );

    /**
     * 创建仓库。
     *
     * @param orgId
     * @param projectId
     * @param mmMaterialCodeCreateDTO
     * @return
     */
    MmMaterialCodeEntity create(
        Long orgId,
        Long projectId,
        MmMaterialCodeCreateDTO mmMaterialCodeCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 更新仓库。
     *
     * @param orgId
     * @param projectId
     * @param materialCodeId
     * @param mmMaterialCodeCreateDTO
     * @return
     */
    MmMaterialCodeEntity update(
        Long orgId,
        Long projectId,
        Long materialCodeId,
        MmMaterialCodeCreateDTO mmMaterialCodeCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 仓库详情。
     *
     * @param orgId
     * @param projectId
     * @param materialCodeId
     * @return
     */
    MmMaterialCodeEntity detail(
        Long orgId,
        Long projectId,
        Long materialCodeId
    );

    /**
     * 删除仓库。
     *
     * @param orgId
     * @param projectId
     * @param materialCodeId
     */
    void delete(
        Long orgId,
        Long projectId,
        Long materialCodeId,
        ContextDTO contextDTO
    );

    /**
     * 仓库详情。
     *
     * @param orgId
     * @param projectId
     * @param tagNumberCompanySearchDTO
     * @return
     */
    MmMaterialCodeEntity searchDetail(
        Long orgId,
        Long projectId,
        MmMaterialCodeSearchDTO tagNumberCompanySearchDTO
    );
}
