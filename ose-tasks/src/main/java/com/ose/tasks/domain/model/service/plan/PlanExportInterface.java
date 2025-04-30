package com.ose.tasks.domain.model.service.plan;

import com.ose.dto.OperatorDTO;
import com.ose.service.EntityInterface;

import java.io.File;

/**
 * 计划信息导出服务接口。
 */
public interface PlanExportInterface extends EntityInterface {

    /**
     * 导出三级计划。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param rootId    根节点 ID
     * @return 导出文件
     */
    File exportWBSWorkEntries(OperatorDTO operator, Long orgId, Long projectId, Long rootId);

    /**
     * 导出四级计划。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param workId    三级计划节点 ID
     * @return 导出文件
     */
    File exportWBSEntityEntries(OperatorDTO operator, Long orgId, Long projectId, Long workId);

}
