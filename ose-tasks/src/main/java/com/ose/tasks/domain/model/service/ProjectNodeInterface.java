package com.ose.tasks.domain.model.service;

import com.ose.tasks.dto.MaterialInfoDTO;
import com.ose.tasks.entity.drawing.SubDrawing;

import java.util.List;
import java.util.Set;

/**
 * 项目节点管理服务接口。
 */
public interface ProjectNodeInterface {


    /**
     * 取得项目节点图纸信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    List<SubDrawing> getDrawingInfo(Long orgId,
                                    Long projectId,
                                    Long entityId);


    /**
     * 取得多个项目节点图纸信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityIds 实体ID
     */
    List<SubDrawing> getDrawingsInfo(Long orgId,
                                     Long projectId,
                                     Set<Long> entityIds);

    /**
     * 取得项目节点材料信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    List<MaterialInfoDTO> getMaterialInfo(Long orgId,
                                          Long projectId,
                                          Long entityId);

    /**
     * 取得多个项目节点材料信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityIds 实体ID
     */
    List<MaterialInfoDTO> getMaterialsInfo(Long orgId,
                                           Long projectId,
                                           Set<Long> entityIds);


}
