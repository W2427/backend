package com.ose.material.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.material.dto.*;
import com.ose.material.entity.*;
import org.springframework.data.domain.Page;

/**
 * 出库单接口
 */
public interface MmIssueInterface {

    /**
     * 获取出库单列表。
     */
    Page<MmIssueEntity> search(
        Long orgId,
        Long projectId,
        MmIssueSearchDTO mmIssueSearchDTO
    );

    /**
     * 创建出库单。
     *
     * @param orgId
     * @param projectId
     * @param mmIssueCreateDTO
     * @return
     */
    MmIssueEntity create(
        Long orgId,
        Long projectId,
        MmIssueCreateDTO mmIssueCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 更新出库单。
     *
     * @param orgId
     * @param projectId
     * @param materialIssueEntityId
     * @return
     */
    MmIssueEntity update(
        Long orgId,
        Long projectId,
        Long materialIssueEntityId,
        MmIssueCreateDTO mmIssueCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 出库单详情。
     *
     * @param orgId
     * @param projectId
     * @param materialIssueEntityId
     * @return
     */
    MmIssueEntity detail(
        Long orgId,
        Long projectId,
        Long materialIssueEntityId
    );

    /**
     * 删除出库单。
     *
     * @param orgId
     * @param projectId
     * @param materialIssueEntityId
     */
    void delete(
        Long orgId,
        Long projectId,
        Long materialIssueEntityId,
        ContextDTO contextDTO
    );

    /**
     * 导入出库单详情。
     *
     * @param orgId
     * @param projectId
     * @param materialIssueEntityId
     * @param operator
     * @param batchTask
     * @param importDTO
     * @return
     */
    MmImportBatchResultDTO importDetail(Long orgId,
                                        Long projectId,
                                        Long materialIssueEntityId,
                                        OperatorDTO operator,
                                        MmImportBatchTask batchTask,
                                        MmImportBatchTaskImportDTO importDTO
    );

    /**
     * 出库单详情列表。
     *
     * @param orgId
     * @param projectId
     * @param materialIssueEntityId
     * @return
     */
    Page<MmIssueDetailEntity> searchDetails(
        Long orgId,
        Long projectId,
        Long materialIssueEntityId,
        MmIssueSearchDTO mmIssueSearchDTO
    );

    /**
     * 获取物料明细二维码列表。
     *
     * @param orgId
     * @param projectId
     * @param materialIssueEntityId
     * @return
     */
    Page<MmIssueDetailQrCodeEntity> searchQrCode(
        Long orgId,
        Long projectId,
        Long materialIssueEntityId,
        Long mmIssueDetailEntityId,
        MmIssueSearchDTO mmIssueSearchDTO
    );

    /**
     * 出库盘点。
     *
     * @param orgId
     * @param projectId
     * @param materialIssueEntityId
     * @return
     */
    void inventoryQrCodes(
        Long orgId,
        Long projectId,
        Long materialIssueEntityId,
        MmIssueInventoryQrCodeDTO mmIssueInventoryQrCodeDTO,
        ContextDTO contextDTO
    );

    /**
     * 出库确认。
     *
     * @param orgId
     * @param projectId
     * @param materialIssueEntityId
     * @return
     */
    void confirm(
        Long orgId,
        Long projectId,
        Long materialIssueEntityId,
        ContextDTO contextDTO
    );

    /**
     * 添加出库详情。
     *
     * @param orgId
     * @param projectId
     * @param materialIssueEntityId
     * @return
     */
    void addDetail(
        Long orgId,
        Long projectId,
        Long materialIssueEntityId,
        MmIssueDetailCreateDTO mmIssueDetailCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 删除出库单详情。
     *
     * @param orgId
     * @param projectId
     * @param materialIssueEntityId
     */
    void deleteItem(
        Long orgId,
        Long projectId,
        Long materialIssueEntityId,
        Long mmIssueDetailEntityId,
        ContextDTO contextDTO
    );

    /**
     * 删除出库单详情二维码。
     *
     * @param orgId
     * @param projectId
     * @param materialIssueEntityId
     */
    void deleteQrCode(
        Long orgId,
        Long projectId,
        Long materialIssueEntityId,
        Long mmIssueDetailEntityId,
        Long mmIssueDetailQrCodeId,
        ContextDTO contextDTO
    );
}
