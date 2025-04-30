package com.ose.material.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.material.dto.MmMaterialCodeCreateDTO;
import com.ose.material.dto.MmMaterialCodeSearchDTO;
import com.ose.material.entity.MmMaterialCodeEntity;
import org.springframework.data.domain.Page;

/**
 * 物料单接口
 */
public interface MmMaterialCodeCompanyInterface {

    /**
     * 获取仓库。
     */
    Page<MmMaterialCodeEntity> search(
        Long companyId,
        MmMaterialCodeSearchDTO mmMaterialCodeSearchDTO
    );

    /**
     * 创建仓库。
     *
     * @param companyId
     * @param mmMaterialCodeCreateDTO
     * @return
     */
    MmMaterialCodeEntity create(
        Long companyId,
        MmMaterialCodeCreateDTO mmMaterialCodeCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 更新仓库。
     *
     * @param companyId
     * @param tagNumberId
     * @param mmMaterialCodeCreateDTO
     * @return
     */
    MmMaterialCodeEntity update(
        Long companyId,
        Long tagNumberId,
        MmMaterialCodeCreateDTO mmMaterialCodeCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 仓库详情。
     *
     * @param companyId
     * @param tagNumberId
     * @return
     */
    MmMaterialCodeEntity detail(
        Long companyId,
        Long tagNumberId
    );

    /**
     * 删除仓库。
     *
     * @param companyId
     * @param tagNumberId
     */
    void delete(
        Long companyId,
        Long tagNumberId,
        MmMaterialCodeCreateDTO mmMaterialCodeCreateDTO,
        ContextDTO contextDTO
    );
}
