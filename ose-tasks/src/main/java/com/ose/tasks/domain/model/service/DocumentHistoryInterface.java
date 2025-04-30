package com.ose.tasks.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.DocumentUploadHistorySearchDTO;
import com.ose.tasks.dto.bpm.BpmExInspConfirmResponseDTO;
import com.ose.tasks.dto.drawing.DocumentUploadDTO;
import com.ose.tasks.dto.drawing.ProofreadSubDrawingPreviewDTO;
import com.ose.tasks.entity.DocumentHistory;
import org.springframework.data.domain.Page;

import java.io.FileNotFoundException;

public interface DocumentHistoryInterface {

    /**
     * 查询上传历史记录
     *
     * @param pageDTO    分页DTO
     * @param operatorId
     * @return
     */
    Page<DocumentHistory> uploadHistories(DocumentUploadHistorySearchDTO pageDTO, Long operatorId);

    /**
     * 上传文件
     *
     * @return
     */
    BpmExInspConfirmResponseDTO uploadDocument(
        DocumentUploadDTO uploadDTO,
        OperatorDTO operatorDTO,
        ContextDTO context
    ) throws FileNotFoundException;

    void delete(OperatorDTO operatorDTO, Long id);

    DocumentHistory modify(OperatorDTO operatorDTO, ContextDTO context, Long id, DocumentUploadDTO documentUploadDTO);

    /**
     * 查询文件信息
     *
     * @param id
     * @return
     */
    DocumentHistory get(Long id);

    /**
     * 文件预览
     *
     * @param id
     * @return
     */
    ProofreadSubDrawingPreviewDTO preview(
        Long id
    );
}
