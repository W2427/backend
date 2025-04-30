package com.ose.material.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.material.dto.*;
import com.ose.material.entity.*;
import org.springframework.data.domain.Page;

/**
 * 发货单接口
 */
public interface MmShippingInterface {

    /**
     * 获取列表。
     */
    Page<MmShippingEntity> search(
        Long orgId,
        Long projectId,
        MmShippingSearchDTO mmShippingSearchDTO
    );

    /**
     * 创建。
     *
     * @param orgId
     * @param projectId
     * @param mmShippingCreateDTO
     */
    void create(
        Long orgId,
        Long projectId,
        MmShippingCreateDTO mmShippingCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 更新。
     *
     * @param orgId
     * @param projectId
     * @param shippingId
     * @param mmShippingCreateDTO
     * @return
     */
    MmShippingEntity update(
        Long orgId,
        Long projectId,
        Long shippingId,
        MmShippingCreateDTO mmShippingCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 定版。
     *
     * @param orgId
     * @param projectId
     * @param shippingId
     * @param mmShippingUpdateStatusDTO
     * @return
     */
    MmShippingEntity updateStatus(
        Long orgId,
        Long projectId,
        Long shippingId,
        MmShippingUpdateStatusDTO mmShippingUpdateStatusDTO,
        ContextDTO contextDTO
    );

    /**
     * 详情。
     *
     * @param orgId
     * @param projectId
     * @param shippingId
     * @return
     */
    MmShippingEntity detail(
        Long orgId,
        Long projectId,
        Long shippingId
    );

    /**
     * 删除。
     *
     * @param orgId
     * @param projectId
     * @param poContractId
     */
    void delete(
        Long orgId,
        Long projectId,
        Long poContractId,
        ContextDTO contextDTO
    );

    /**
     * 删除发货单明细。
     *
     * @param orgId
     * @param projectId
     * @param shippingId
     * @param shippingDetailId
     */
    void deleteItem(
        Long orgId,
        Long projectId,
        Long shippingId,
        Long shippingDetailId,
        ContextDTO contextDTO
    );

    /**
     * 删除发货单明细关系。
     *
     * @param orgId
     * @param projectId
     * @param shippingId
     * @param shippingDetailId
     * @param shippingDetailRelationId
     */
    void deleteItemRelation(
        Long orgId,
        Long projectId,
        Long shippingId,
        Long shippingDetailId,
        Long shippingDetailRelationId,
        ContextDTO contextDTO
    );

    Page<MmShippingDetailSearchDetailDTO> searchDetails(
        Long orgId,
        Long projectId,
        Long shippingId,
        MmShippingSearchDTO mmShippingSearchDTO
    );

    Page<MmShippingDetailsDTO> searchRequisitionDetails(
        Long orgId,
        Long projectId,
        Long shippingId,
        MmShippingSearchDTO mmShippingSearchDTO
    );

    /**
     * 发货单详情添加。
     *
     * @param orgId
     * @param projectId
     * @param shippingId
     * @param mmShippingDetailAddDTO
     * @return
     */
    void addDetails(
        Long orgId,
        Long projectId,
        Long shippingId,
        MmShippingDetailAddDTO mmShippingDetailAddDTO,
        ContextDTO contextDTO
    );

    Page<MmReleaseReceiveEntity> searchReleaseReceive(
        Long orgId,
        Long projectId,
        Long shippingId,
        MmShippingSearchDTO mmShippingSearchDTO
    );

    /**
     * 修改发货量。
     *
     * @param orgId
     * @param projectId
     * @param shippingId
     * @param shippingDetailId
     * @param shippingDetailRelationId
     * @param mmShippingDetailUpdateQtyDTO
     * @param contextDTO
     * @return
     */
    void updateQty(
        Long orgId,
        Long projectId,
        Long shippingId,
        Long shippingDetailId,
        Long shippingDetailRelationId,
        MmShippingDetailUpdateQtyDTO mmShippingDetailUpdateQtyDTO,
        ContextDTO contextDTO
    );

    /**
     * 批量修改发货量。
     *
     * @param orgId
     * @param projectId
     * @param shippingId
     * @param shippingDetailId
     * @param mmShippingDetailUpdateQtyDTO
     * @param contextDTO
     * @return
     */
    void batchUpdateQty(
        Long orgId,
        Long projectId,
        Long shippingId,
        Long shippingDetailId,
        MmShippingDetailUpdateQtyDTO mmShippingDetailUpdateQtyDTO,
        ContextDTO contextDTO
    );

     MmImportBatchResultDTO importDetailItem(Long orgId,
                                                   Long projectId,
                                                   Long shippingId,
                                                   OperatorDTO operator,
                                                   MmImportBatchTask batchTask,
                                                   MmImportBatchTaskImportDTO importDTO
    );
}
