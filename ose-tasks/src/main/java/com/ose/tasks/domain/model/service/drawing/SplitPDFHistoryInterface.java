package com.ose.tasks.domain.model.service.drawing;

import com.ose.dto.ContextDTO;
import com.ose.tasks.dto.drawing.DrawingCriteriaPageDTO;
import com.ose.tasks.dto.drawing.DrawingUploadDTO;
import com.ose.tasks.entity.drawing.SplitPDFHistory;
import org.springframework.data.domain.Page;

/**
 * 拆分PDF接口。
 *
 * @auth DengMing
 * @date 2021/8/3 13:36
 */
public interface SplitPDFHistoryInterface {


    /**
     * 拆分图纸插件。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param uploadDTO  文件名
     */
    void splitPDF(Long orgId, Long projectId, DrawingUploadDTO uploadDTO, ContextDTO context);

    /**
     * 查询拆分历史列表。
     *
     * @param orgId
     * @param projectId
     * @param criteriaDTO
     * @return
     */
    Page<SplitPDFHistory> search(Long orgId, Long projectId, DrawingCriteriaPageDTO criteriaDTO);

    void splitPdfZip(Long orgId, Long projectId, DrawingCriteriaPageDTO criteriaDTO,ContextDTO context);
}
