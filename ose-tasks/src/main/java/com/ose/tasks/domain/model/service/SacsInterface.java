package com.ose.tasks.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.SacsUploadHistorySearchDTO;
import com.ose.tasks.dto.bpm.BpmExInspConfirmResponseDTO;
import com.ose.tasks.dto.drawing.DrawingUploadDTO;
import com.ose.tasks.entity.SacsUploadHistory;
import org.springframework.data.domain.Page;

import java.io.FileNotFoundException;

public interface SacsInterface {

    /**
     * 查询sacs报告上传历史记录
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param pageDTO    分页DTO
     * @param operatorId
     * @return
     */
    Page<SacsUploadHistory> sacsUploadHistories(Long orgId, Long projectId,
                                                              SacsUploadHistorySearchDTO pageDTO, Long operatorId);

    /**
     * 上传文件
     *
     * @return
     */
    BpmExInspConfirmResponseDTO uploadSacs(
        Long orgId,
        Long projectId,
        DrawingUploadDTO uploadDTO,
        OperatorDTO operatorDTO,
        ContextDTO context
    ) throws FileNotFoundException;
}
