package com.ose.tasks.domain.model.service.material;

import com.ose.dto.ContextDTO;
import com.ose.tasks.dto.material.StructureEntityQrCodeCreateDTO;
import com.ose.tasks.dto.material.StructureEntityQrCodeCriteriaDTO;
import com.ose.tasks.entity.material.StructureEntityQrCode;
import org.springframework.data.domain.Page;

/**
 * 结构实体二维码查询接口
 */
public interface StructureEntityQrCodeInterface {

    /**
     * 查询结构零件二维码列表。
     *
     * @param orgId
     * @param projectId
     * @param criteriaDTO
     * @return
     */
    Page<StructureEntityQrCode> search(
        Long orgId,
        Long projectId,
        StructureEntityQrCodeCriteriaDTO criteriaDTO);

    /**
     * 创建结构零件二维码。
     *
     * @param orgId
     * @param projectId
     * @param structureEntityQrCodeCreateDTO
     * @param context
     * @return
     */
    StructureEntityQrCode create(
        Long orgId,
        Long projectId,
        StructureEntityQrCodeCreateDTO structureEntityQrCodeCreateDTO,
        ContextDTO context);

    /**
     * 更新结构两件二维码旧数据。
     *
     * @param orgId
     * @param projectId
     * @param context
     */
    void generate(
        Long orgId,
        Long projectId,
        ContextDTO context);
}
