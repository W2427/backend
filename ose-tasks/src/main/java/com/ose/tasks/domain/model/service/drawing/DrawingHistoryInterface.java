package com.ose.tasks.domain.model.service.drawing;

import com.ose.tasks.entity.drawing.DrawingHistory;

import java.util.List;

/**
 * service接口
 */
public interface DrawingHistoryInterface {


    /**
     * 保存图纸文件历史记录
     *
     * @param his 图纸历史
     * @return 图纸历史
     */
    DrawingHistory save(DrawingHistory his);

    /**
     * 根据图纸条目id获取图纸最近的一条历史记录
     *
     * @param id 图纸ID
     * @return 图纸历史
     */
    DrawingHistory findDrawingPipingHistoryByDrawingId(Long id);

    /**
     * 获取历史记录
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id        图纸ID
     * @return 图纸历史
     */
    List<DrawingHistory> getHistory(Long orgId, Long projectId, Long id);




}
