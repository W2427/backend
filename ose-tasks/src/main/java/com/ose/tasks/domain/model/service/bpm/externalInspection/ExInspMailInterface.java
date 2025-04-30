package com.ose.tasks.domain.model.service.bpm.externalInspection;

import com.ose.dto.ContextDTO;
import com.ose.tasks.dto.bpm.ExInspMailApplyPreviewDTO;
import com.ose.tasks.dto.bpm.ExInspTaskEmailDTO;
import com.ose.tasks.entity.bpm.BpmExInspMailHistory;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ExInspMailInterface {

    /**
     * 外检邮件申请预览
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param operator
     * @param applyDTO
     * @return
     */
    ExInspMailApplyPreviewDTO externalInspectionMailPreview(Long orgId, Long projectId, Long operator, ExInspMailApplyPreviewDTO applyDTO);


    /**
     * 外检邮件申请预览
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param operator
     * @param applyDTO
     * @param contextDTO
     * @param command    网关
     * @return
     */
    ExInspMailApplyPreviewDTO externalInspectionMailSend(Long orgId,
                                                         Long projectId,
                                                         Long operator,
                                                         ExInspMailApplyPreviewDTO applyDTO,
                                                         ContextDTO contextDTO,
                                                         Map<String, Object> command);


    /**
     * 查询外检邮件历史记录详情
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return
     */
    BpmExInspMailHistory getExternalInspectionEmail(Long orgId, Long projectId, Long id);


    /**
     * 发送外检邮件
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param hisId     报检申请邮件历史
     */
    void sendExternalInspectionMail(Long orgId, Long projectId, Long hisId);

    /**
     * 查询外检邮件历史记录
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return
     */
    List<ExInspTaskEmailDTO> getEmailOperatorList(Long orgId, Long projectId);


    /**
     * 查询外检邮件历史记录
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return
     */
    Page<BpmExInspMailHistory> getExternalInspectionEmail(Long orgId, Long projectId, ExInspTaskEmailDTO criteriaDTO);

}
