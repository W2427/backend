package com.ose.material.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.material.dto.MmMaterialCodeTypeCreateDTO;
import com.ose.material.dto.MmMaterialCodeTypeSearchDTO;
import com.ose.material.entity.MmMaterialCodeTypeEntity;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 材料编码类型接口
 */
public interface MmMaterialCodeTypeProjectInterface {

    /**
     * 获取材料编码类型。
     */
    Page<MmMaterialCodeTypeEntity> search(
        Long orgId,
        Long projectId,
        MmMaterialCodeTypeSearchDTO mmMaterialCodeTypeSearchDTO
    );

    /**
     * 创建材料编码类型。
     *
     * @param orgId
     * @param projectId
     * @param mmMaterialCodeTypeCreateDTO
     * @return
     */
    MmMaterialCodeTypeEntity create(
        Long orgId,
        Long projectId,
        MmMaterialCodeTypeCreateDTO mmMaterialCodeTypeCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 更新材料编码类型。
     *
     * @param orgId
     * @param projectId
     * @param wareHouseId
     * @param mmMaterialCodeTypeCreateDTO
     * @return
     */
    MmMaterialCodeTypeEntity update(
        Long orgId,
        Long projectId,
        Long wareHouseId,
        MmMaterialCodeTypeCreateDTO mmMaterialCodeTypeCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 材料编码类型详情。
     *
     * @param orgId
     * @param projectId
     * @param wareHouseId
     * @return
     */
    MmMaterialCodeTypeEntity detail(
        Long orgId,
        Long projectId,
        Long wareHouseId
    );

    /**
     * 删除材料编码类型。
     *
     * @param orgId
     * @param projectId
     * @param wareHouseId
     */
    void delete(
        Long orgId,
        Long projectId,
        Long wareHouseId,
        ContextDTO contextDTO
    );

    /**
     * 获取某类材料编码类型。
     */
    List<MmMaterialCodeTypeEntity> searchType(
        Long orgId,
        Long projectId,
        String materialOrganizationType
    );
}
