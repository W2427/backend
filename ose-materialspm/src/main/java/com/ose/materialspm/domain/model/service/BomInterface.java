package com.ose.materialspm.domain.model.service;

import com.ose.docs.api.UploadFeignAPI;
import com.ose.materialspm.dto.BomNodeDTO;
import com.ose.materialspm.dto.BomNodeResultsDTO;
import com.ose.materialspm.dto.ExportFileDTO;
import com.ose.materialspm.entity.MvMxjListNodesEntity;
import com.ose.materialspm.entity.ViewMxjPosEntity;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Demoservice接口
 */
public interface BomInterface {

    /**
     * 获取全部实体类型
     *
     * @param spmProjId  SPM项目ID
     * @param parentLnId 子节点ID
     */
    List<MvMxjListNodesEntity> getByProjIdAndParentLnId(String spmProjId, String parentLnId);

    List<BomNodeResultsDTO> getByProjId(String spmProjId);

    /**
     * 获取全部实体类型
     */
    Page<ViewMxjPosEntity> getByProjIdAndBompath(BomNodeDTO bomNodeDTO);

    /**
     * 导出BOM List
     *
     * @param orgId          组织id
     * @param projectId      SPM项目ID
     * @param bomNodeDTO
     * @param uploadFeignAPI
     * @return
     */
    ExportFileDTO exportBoms(Long orgId, Long projectId, BomNodeDTO bomNodeDTO, UploadFeignAPI uploadFeignAPI);
}
