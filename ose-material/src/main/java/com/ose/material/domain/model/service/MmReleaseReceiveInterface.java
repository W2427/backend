package com.ose.material.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.material.dto.*;
import com.ose.material.entity.MmImportBatchTask;
import com.ose.material.entity.MmReleaseReceiveDetailEntity;
import com.ose.material.entity.MmReleaseReceiveEntity;
import com.ose.material.entity.MmReleaseReceiveDetailQrCodeEntity;
import org.springframework.data.domain.Page;

import java.io.File;
import java.util.List;

/**
 * 入库单接口
 */
public interface MmReleaseReceiveInterface {

    /**
     * 获取入库单列表。
     */
    Page<MmReleaseReceiveEntity> search(
        Long orgId,
        Long projectId,
        MmReleaseReceiveSearchDTO mmReleaseReceiveSearchDTO
    );

    /**
     * 获取入库单待办列表。
     */
    Page<MmReleaseReceiveEntity> searchByAssignee(
        Long orgId,
        Long projectId,
        MmReleaseReceiveSearchDTO mmReleaseReceiveSearchDTO,
        OperatorDTO operatorDTO
    );

    /**
     * 创建入库单。
     *
     * @param orgId
     * @param projectId
     * @param mmReleaseReceiveCreateDTO
     * @return
     */
    MmReleaseReceiveEntity create(
        Long orgId,
        Long projectId,
        MmReleaseReceiveCreateDTO mmReleaseReceiveCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 更新入库单。
     *
     * @param orgId
     * @param projectId
     * @param mmReleaseReceiveCreateDTO
     * @return
     */
    MmReleaseReceiveEntity update(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        MmReleaseReceiveCreateDTO mmReleaseReceiveCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 更新入库单。
     *
     * @param orgId
     * @param projectId
     * @param mmReleaseReceiveUpdateRunningStatusDTO
     * @return
     */
    MmReleaseReceiveEntity updateRunningStatus(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        MmReleaseReceiveUpdateRunningStatusDTO mmReleaseReceiveUpdateRunningStatusDTO,
        ContextDTO contextDTO
    );

    /**
     * 入库单详情。
     *
     * @param orgId
     * @param projectId
     * @param materialReceiveNoteId
     * @return
     */
    MmReleaseReceiveEntity detail(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId
    );

    /**
     * 删除入库单。
     *
     * @param orgId
     * @param projectId
     * @param materialReceiveNoteId
     */
    void delete(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        ContextDTO contextDTO
    );

    /**
     * 导入入库单详情。
     *
     * @param orgId
     * @param projectId
     * @param operator
     * @param batchTask
     * @param importDTO
     * @return
     */
    MmImportBatchResultDTO importDetail(Long orgId,
                                        Long projectId,
                                        Long materialReceiveNoteId,
                                        OperatorDTO operator,
                                        MmImportBatchTask batchTask,
                                        MmImportBatchTaskImportDTO importDTO
    );

    /**
     * 获取入库单明细列表。
     */
    Page<MmReleaseReceiveDetailSearchDetailDTO> searchDetails(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        MmReleaseReceiveDetailSearchDTO mmReleaseReceiveDetailSearchDTO
    );

    /**
     * 查询入库单是否完成盘库。
     */
    MmReleaseReceiveInventoryStatusDTO searchInventoryStatus(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId
    );

    /**
     * 删除入库单明细。
     *
     * @param orgId
     * @param projectId
     * @param materialReceiveNoteDetailId
     */
    void deleteItem(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteDetailId,
        ContextDTO contextDTO
    );

    /**
     * 删除入库单明细二维码。
     *
     * @param orgId
     * @param projectId
     * @param materialReceiveNoteDetailId
     * @param qrCodeId
     */
    void deleteQrCode(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteDetailId,
        Long qrCodeId,
        ContextDTO contextDTO
    );

    /**
     * 查询入库单明细二维码。
     */
    Page<MmReleaseReceiveDetailQrCodeEntity> searchDetailQrCodes(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        Long materialReceiveNoteDetailEntityDetailId,
        MmReleaseReceiveQrCodeSearchDTO mmReleaseReceiveQrCodeSearchDTO
    );

    /**
     * 生成入库单明细二维码。
     */
    void createDetailQrCodes(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        Long materialReceiveNoteDetailEntityDetailId,
        MmReleaseReceiveQrCodeCreateDTO mmReleaseReceiveQrCodeCreateDTO,
        ContextDTO contextDTO
    );

    void inventoryQrCodes(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        Long materialReceiveNoteDetailId,
//        Long materialReceiveNoteDetailQrCodeId,
        MmReleaseReceiveInventoryQrCodeDTO mmReleaseReceiveInventoryQrCodeDTO,
        ContextDTO contextDTO
    );

    void batchInventoryQrCodes(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        MmReleaseReceiveInventoryQrCodeDTO mmReleaseReceiveInventoryQrCodeDTO,
        ContextDTO contextDTO
    );


    void cancelInventoryQrCodes(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        MmReleaseReceiveInventoryQrCodeDTO mmReleaseReceiveInventoryQrCodeDTO,
        ContextDTO contextDTO
    );

    MmReleaseReceiveQrCodeResultDTO qrCodeSearch(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        String qrCode
    );

    void startReceive(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        String mmReleaseReceiveNo,
        MmReleaseReceiveReceiveDTO mmReleaseReceiveReceiveDTO,
        ContextDTO contextDTO,
        Boolean inExternalQuality
    );

    void detailReceive(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        Long materialReceiveNoteDetailId,
        MmReleaseReceiveReceiveDTO mmReleaseReceiveReceiveDTO,
        ContextDTO contextDTO,
        Boolean inExternalQuality
    );

    void updateDetail(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        Long materialReceiveNoteDetailId,
        MmReleaseReceiveInventoryQrCodeDTO mmReleaseReceiveInventoryQrCodeDTO,
        ContextDTO contextDTO
    );


    List<MmReleaseNotePrintDTO> getQrCodeByRelnItemId(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        Long materialReceiveNoteDetailId);

    void batchUpdateDetail(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        MmReleaseReceiveBatchUpdateDTO mmReleaseReceiveBatchUpdateDTO,
        ContextDTO contextDTO
    );

    /**
     * 保存实体下载临时文件。
     *
     * @param orgId                     组织 ID
     * @param projectId                 项目 ID
     * @param mmReleaseReceiveSearchDTO 查询条件
     * @param operatorId                项目ID
     * @return 实体下载临时文件
     */
    File download(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        MmReleaseReceiveSearchDTO mmReleaseReceiveSearchDTO,
        Long operatorId);

}
