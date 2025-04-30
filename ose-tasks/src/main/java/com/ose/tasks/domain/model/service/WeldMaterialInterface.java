package com.ose.tasks.domain.model.service;

import com.ose.dto.PageDTO;
import com.ose.service.EntityInterface;
import com.ose.tasks.dto.WeldMaterialDTO;
import com.ose.tasks.dto.WpsWeldMaterialDTO;
import com.ose.tasks.entity.WeldMaterial;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WeldMaterialInterface extends EntityInterface {

    void create(
        Long operatorId,
        Long orgId,
        Long projectId,
        WeldMaterialDTO weldMaterialDTO
    );

    /**
     * 获取焊材列表（分页）。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param pageDTO   分页参数
     * @return 带分页的焊材列表
     */
    Page<WeldMaterial> search(Long orgId, Long projectId, String batchNo, PageDTO pageDTO);

    /**
     * 获取焊材详情
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param batchNo   批次号
     */
    WeldMaterial get(
        Long orgId,
        Long projectId,
        String batchNo
    );

    /**
     * 根据焊材类型获取焊材列表
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     */
    List<WeldMaterial> getList(
        Long orgId,
        Long projectId,
        WeldMaterialDTO weldMaterialDTO
    );

    /**
     * 根据焊材类型获取焊材列表
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     */
    List<WeldMaterial> getDetail(
        Long orgId,
        Long projectId,
        WpsWeldMaterialDTO wpsWeldMaterialDTO
    );

    /**
     * 删除焊材。
     *
     * @param operatorId 操作人ID
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param batchNo   批次号
     */
    void delete(
        Long operatorId,
        Long orgId,
        Long projectId,
        String batchNo
    );

    /**
     * 更新焊材信息。
     *
     * @param operatorId 操作人ID
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param batchNo    批次号
     * @param weldMaterialDTO  更新信息
     */
    void update(
        Long operatorId,
        Long orgId,
        Long projectId,
        String batchNo,
        WeldMaterialDTO weldMaterialDTO
    );
}
