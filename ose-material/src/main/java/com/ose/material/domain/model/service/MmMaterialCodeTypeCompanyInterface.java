package com.ose.material.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.material.dto.MmMaterialCodeTypeCreateDTO;
import com.ose.material.dto.MmMaterialCodeTypeSearchDTO;
import com.ose.material.entity.MmMaterialCodeTypeEntity;
import org.springframework.data.domain.Page;

/**
 * 公司仓库接口
 */
public interface MmMaterialCodeTypeCompanyInterface {

    /**
     * 获取公司仓库。
     */
    Page<MmMaterialCodeTypeEntity> search(
        Long companyId,
        MmMaterialCodeTypeSearchDTO mmMaterialCodeTypeSearchDTO
    );

    /**
     * 创建公司仓库。
     *
     * @param companyId
     * @param mmMaterialCodeTypeCreateDTO
     * @return
     */
    MmMaterialCodeTypeEntity create(
        Long companyId,
        MmMaterialCodeTypeCreateDTO mmMaterialCodeTypeCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 更新公司仓库。
     *
     * @param companyId
     * @param wareHouseId
     * @param mmMaterialCodeTypeCreateDTO
     * @return
     */
    MmMaterialCodeTypeEntity update(
        Long companyId,
        Long wareHouseId,
        MmMaterialCodeTypeCreateDTO mmMaterialCodeTypeCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 公司仓库详情。
     *
     * @param companyId
     * @param wareHouseId
     * @return
     */
    MmMaterialCodeTypeEntity detail(
        Long companyId,
        Long wareHouseId
    );

    /**
     * 删除公司仓库。
     *
     * @param companyId
     * @param wareHouseId
     */
    void delete(
        Long companyId,
        Long wareHouseId,
        MmMaterialCodeTypeCreateDTO mmMaterialCodeTypeCreateDTO,
        ContextDTO contextDTO
    );
}
