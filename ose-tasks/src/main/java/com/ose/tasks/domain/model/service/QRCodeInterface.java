package com.ose.tasks.domain.model.service;

import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.QRCodePostDTO;
import com.ose.tasks.dto.QrCodePrintDTO;
import com.ose.tasks.dto.material.EntityQrCodeCreateDTO;
import com.ose.tasks.dto.material.EntityQrCodeCriteriaDTO;
import com.ose.tasks.dto.material.SpoolQrCodeCreateDTO;
import com.ose.tasks.entity.QRCode;
import com.ose.tasks.entity.bpm.BpmDeliveryEntity;
import com.ose.tasks.entity.material.EntityQrCodeEntity;
import org.springframework.data.domain.Page;

/**
 * 二维码代码操作服务接口。
 */
public interface QRCodeInterface {

    /**
     * 取得二维码详细信息。
     *
     * @param code 二维码代码
     * @return 二维码数据实体
     */
    QRCode get(String code);

    /**
     * 创建二维码。
     *
     * @param operator      创建者信息
     * @param qrCodePostDTO 二维码信息
     * @return 二维码数据实体
     */
    QRCode add(OperatorDTO operator, QRCodePostDTO qrCodePostDTO);

    /**
     * 查询实体二维码
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param criteriaDTO
     * @return
     */
    Page<EntityQrCodeEntity> getEntityQrCode(Long orgId, Long projectId, EntityQrCodeCriteriaDTO criteriaDTO);

    /**
     * 设置二维码打印flg
     *
     * @param printDTO
     * @return
     */
    boolean setPrintFlgTrue(QrCodePrintDTO printDTO);

    /**
     * 查询实体二维码
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param entityQrCodeCreateDTO
     * @param operatorDTO
     * @return
     */
    EntityQrCodeEntity addEntityQrCode(Long orgId, Long projectId, EntityQrCodeCreateDTO entityQrCodeCreateDTO,OperatorDTO operatorDTO);

    /**
     * 查询单管实体二维码
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param criteriaDTO
     * @return
     */
    Page<BpmDeliveryEntity> getSpoolQrCode(Long orgId, Long projectId,
                                           EntityQrCodeCriteriaDTO criteriaDTO);

    BpmDeliveryEntity addSpoolQrCode(
        Long orgId,
        Long projectId,
        SpoolQrCodeCreateDTO spoolQrCodeCreateDTO,
        OperatorDTO operatorDTO);

    void updatePrintFlagInEntityIds(EntityQrCodeCriteriaDTO entityQrCodeCriteriaDTO);
}
