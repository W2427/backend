package com.ose.tasks.domain.model.service.drawing.externalDrawing;

import com.ose.tasks.entity.drawing.externalDrawing.ExternalDrawingHistory;

import java.util.List;

/**
 * service接口
 */
public interface ExternalDrawingHistoryInterface {


    /**
     * 保存图纸文件历史记录
     *
     * @param his 图纸历史
     * @return 图纸历史
     */
    ExternalDrawingHistory save(ExternalDrawingHistory his);

    /**
     * 根据图纸条目id获取图纸最近的一条历史记录
     *
     * @param id 图纸ID
     * @return 图纸历史
     */
    ExternalDrawingHistory findDrawingPipingHistoryByDrawingId(Long id);

    /**
     * 获取历史记录
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id        图纸ID
     * @return 图纸历史
     */
    List<ExternalDrawingHistory> getHistory(Long orgId, Long projectId, Long id);




}
