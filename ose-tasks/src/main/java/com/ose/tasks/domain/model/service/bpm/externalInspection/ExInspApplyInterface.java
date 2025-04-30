package com.ose.tasks.domain.model.service.bpm.externalInspection;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.bpm.ExInspApplyCriteriaDTO;
import com.ose.tasks.dto.bpm.ExInspApplyDTO;
import com.ose.tasks.dto.bpm.ExInspApplyFilterConditionDTO;
import com.ose.tasks.entity.bpm.BpmExInspApply;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ExInspApplyInterface {

    /**
     * 外检申请
     *
     * @param context     上下文
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param operatorDTO 操作者DTO
     * @param applyDTO    申请DTO
     * @return boolean
     */
    boolean externalInspectionApply(ContextDTO context, Long orgId, Long projectId, OperatorDTO operatorDTO, ExInspApplyDTO applyDTO);


    /**
     * 查询外检申请列表
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param criteriaDTO 查询条件DTO
     * @return 分页数据
     */
    Page<BpmExInspApply> getExternalInspectionApplyList(Long orgId, Long projectId, List<Long> assignees, ExInspApplyCriteriaDTO criteriaDTO);


    /**
     * 获取外检申请查询页面检索数据
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param assignees 指定人员
     * @return 外检申请条件DTO
     */
    ExInspApplyFilterConditionDTO getExternalInspectionApplyFilterCondition(Long orgId, Long projectId,
                                                                            List<Long> assignees);

}
