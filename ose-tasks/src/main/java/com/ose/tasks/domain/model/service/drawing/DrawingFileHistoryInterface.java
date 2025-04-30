package com.ose.tasks.domain.model.service.drawing;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.drawing.DrawingCriteriaDTO;
import com.ose.tasks.dto.drawing.DrawingFileHistoryCreateDTO;
import com.ose.tasks.dto.drawing.DrawingFileHistorySearchDTO;
import com.ose.tasks.entity.drawing.DrawingFileHistory;
import org.springframework.data.domain.Page;

import java.io.File;
import java.util.List;

/**
 * Drawing File History service接口
 */
public interface DrawingFileHistoryInterface {

    /**
     * 图纸文件历史详情
     *
     * @param drawingFileHistoryId 图纸文件历史ID
     * @return 图纸文件历史
     */
    DrawingFileHistory detail(Long orgId, Long projectId, Long drawingFileHistoryId);

    /**
     * 图纸文件历史列表
     *
     * @param orgId                       组织ID
     * @param projectId                   项目ID
     * @param criteriaDTO 查询参数
     * @return 图纸文件历史
     */
    Page<DrawingFileHistory> search(Long orgId, Long projectId, Long drawingFileId, DrawingFileHistorySearchDTO criteriaDTO);


    /**
     * 图纸文件历史创建
     *
     * @param orgId                       组织ID
     * @param projectId                   项目ID
     * @param drawingFileHistoryCreateDTO 创建数据
     * @return 图纸文件历史
     */
    DrawingFileHistory create(
        Long orgId,
        Long projectId,
        Long drawingDetailId,
        DrawingFileHistoryCreateDTO drawingFileHistoryCreateDTO,
        OperatorDTO operatorDTO
    );

    /**
     * 图纸文件历史更新
     *
     * @param orgId                       组织ID
     * @param projectId                   项目ID
     * @param drawingFileHistoryCreateDTO 创建数据
     * @return 图纸文件历史
     */
    DrawingFileHistory update(
        Long orgId,
        Long projectId,
        Long drawingFileId,
        Long drawingFileHistoryId,
        DrawingFileHistoryCreateDTO drawingFileHistoryCreateDTO,
        OperatorDTO operatorDTO
    );

    List<DrawingFileHistory> getDrawingFileHistory(Long orgId, Long projectId, Long drawingFileId, Long procInstId);

    File saveDownloadFile(Long orgId, Long projectId, Long drawingFileHistoryId, Long operatorId);
}
