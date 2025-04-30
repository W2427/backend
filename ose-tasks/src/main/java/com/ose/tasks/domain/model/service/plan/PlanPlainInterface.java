package com.ose.tasks.domain.model.service.plan;

import com.ose.service.EntityInterface;
import com.ose.tasks.dto.MaterialISOMatchSummaryDTO;
import com.ose.tasks.dto.MaterialMatchSummaryDTO;
import com.ose.tasks.dto.WBSEntryDwgSummaryDTO;
import com.ose.tasks.dto.WBSEntryGroupPlainDTO;
import com.ose.tasks.dto.bpm.TaskHierarchyDTO;
import com.ose.tasks.dto.wbs.WBSEntryPlainQueryDTO;
import com.ose.tasks.entity.wbs.entry.WBSEntryPlain;
import org.springframework.data.domain.Page;

/**
 * 扁平计划管理服务接口。
 */
public interface PlanPlainInterface extends EntityInterface {

    /**
     * 返回扁平的 计划数据分页列表
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    Page<WBSEntryPlain> searchWbs(Long orgId, Long projectId, WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO);


    /**
     * 返回扁平的 分组聚合的计划数据
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    Page<WBSEntryGroupPlainDTO> searchGroupWbs(Long orgId, Long projectId, WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO);

    /**
     * 返回扁平计划对应的材料汇总信息
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    MaterialMatchSummaryDTO getMaterialInfo(Long orgId, Long projectId, WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO);


    /**
     * 返回扁平计划对应的图纸信息
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    WBSEntryDwgSummaryDTO getDwgInfo(Long orgId, Long projectId, WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO);

    /**
     * 返回扁平计划对应的ISO材料汇总信息
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    MaterialISOMatchSummaryDTO getIsoMaterialInfo(Long orgId, Long projectId, WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO);

    /**
     * 返回 四级计划数据中 的 工序阶段 和 工序
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param stageId
     * @return
     */
    TaskHierarchyDTO getStageProcess(Long orgId, Long projectId, Long stageId);
}
