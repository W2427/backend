package com.ose.tasks.domain.model.service.bpm;

import com.ose.service.EntityInterface;
import com.ose.tasks.dto.bpm.ExInspDocDTO;
import com.ose.tasks.entity.bpm.BpmEntityDocsMaterials;
import com.ose.tasks.vo.bpm.ActInstDocType;

import java.io.File;
import java.util.List;

/**
 * 实体文档管理service接口
 */
public interface EntityDocInterface extends EntityInterface {

    /**
     * 根据实体ID，文档类型获取实体文档列表。
     *
     * @param entityIds 实体ID
     * @param type      文档类型
     * @return 实体文档列表
     */
    List<BpmEntityDocsMaterials> getBpmEntityDocsList(List<Long> entityIds,
                                                      ActInstDocType type);

    /**
     * 打包下载外检报告。
     *
     * @param orgId         组织ID
     * @param projectId     工程ID
     * @param exInspDocDTOS 外检报告列表
     * @return zip文件
     */
    File createDownloadZipFile(Long orgId,
                               Long projectId,
                               List<ExInspDocDTO> exInspDocDTOS);
}
