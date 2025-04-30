package com.ose.tasks.domain.model.repository.wbs;

import com.ose.tasks.dto.WBSModuleTaskPackageDTO;
import com.ose.tasks.dto.WBSScreenResultDTO;
import com.ose.tasks.dto.wbs.WBSEntryDwgDTO;
import com.ose.tasks.dto.wbs.WBSEntryIsoDTO;
import com.ose.tasks.dto.wbs.WBSEntryMaterialDTO;
import com.ose.tasks.dto.wbs.WBSEntryPlainQueryDTO;
import com.ose.tasks.entity.wbs.entity.ISOEntity;
import com.ose.tasks.entity.wbs.entry.WBSEntryPlain;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * WBS 条目 CRUD 操作接口。
 */
public interface WBSEntryPlainCustomRepository {

    /**
     * 查询扁平计划。
     *
     * @param projectId             项目 ID
     * @param orgId                 查询条件
     * @param wbsEntryPlainQueryDTO 分页参数
     * @return 扁平计划分页数据
     */
    Page<WBSEntryPlain> search(
        final Long projectId,
        final Long orgId,
        final WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO
    );

    List<ISOEntity> getModules(
        Long orgId,
        Long projectId
    );

    /**
     * 取得ISO匹配的整体的材料匹配率
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    Double getOverallMatchPercent(Long orgId,
                                  Long projectId,
                                  WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO);


    /**
     * 根据模块号查询任务包或根据任务包查询模块号
     *
     * @param orgId
     * @param projectId  项目ID
     * @param wbsModuleTaskPackageDTO
     * @return
     */
    WBSScreenResultDTO getModulesTaskPackages(
        Long orgId,
        Long projectId,
        WBSModuleTaskPackageDTO wbsModuleTaskPackageDTO
    );


    /**
     * 获得ISO 材料匹配情况
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    Page<WBSEntryIsoDTO> getMatchedIsos(Long orgId, Long projectId, WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO);

    /**
     * 获得匹配的 材料详情
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    Page<WBSEntryMaterialDTO> getMatchedMaterials(Long orgId, Long projectId, WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO);

    /**
     * 取得计划中 图纸发图率
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    Double getOverallIssuePercent(Long orgId, Long projectId, WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO);

    /**
     * 获得子图纸详情
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    Page<WBSEntryDwgDTO> getSubDwgIssedList(Long orgId, Long projectId, WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO);

    /**
     * 获得工序阶段列表
     * @param orgId 组织ID
     * @param projectId 项目ID
     */

}
