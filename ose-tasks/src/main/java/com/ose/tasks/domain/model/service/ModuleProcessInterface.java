package com.ose.tasks.domain.model.service;

import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.process.EntityProcessRelationsDTO;
import com.ose.tasks.entity.process.ModuleProcessDefinition;
import com.ose.tasks.entity.process.ModuleProcessDefinitionBasic;

import java.util.List;

/**
 * 模块工作流定义管理服务接口。
 */
public interface ModuleProcessInterface {

    /**
     * 部署项目模块工作流。
     *
     * @param operator                  操作者信息
     * @param orgId                     组织 ID
     * @param projectId                 项目 ID
     * @param version                   模块工作流部署更新版本号
     * @param bpmnFilename              工作流定义文件文件名
     * @param entityProcessRelationsDTO 工序-实体类型映射表
     * @return 模块工作流定义
     */
    ModuleProcessDefinition deploy(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        long version,
        final String funcPart,
        String bpmnFilename,
        EntityProcessRelationsDTO entityProcessRelationsDTO,
        String bpmnName
    );

    /**
     * 部署项目模块工作流。（包含专业信息）
     *
     * @param operator                  操作者信息
     * @param orgId                     组织 ID
     * @param projectId                 项目 ID
     * @param version                   模块工作流部署更新版本号
     * @param bpmnFilename              工作流定义文件文件名
     * @param entityProcessRelationsDTO 工序-实体类型映射表
     * @return 模块工作流定义
     */
    ModuleProcessDefinition deploy(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        long version,
        String bpmnFilename,
        EntityProcessRelationsDTO entityProcessRelationsDTO,
        final String bpmnName
    );

    /**
     * 保存项目模块工作流部署信息。
     *
     * @param moduleProcessDefinition 项目模块工作流部署信息
     * @return 项目模块工作流部署信息
     */
    ModuleProcessDefinition save(ModuleProcessDefinition moduleProcessDefinition);

    /**
     * 取得项目的模块工作流部署信息列表。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return 项目的模块工作流部署信息列表
     */
    List<ModuleProcessDefinitionBasic> list(Long orgId, Long projectId);

    /**
     * 取得项目的指定类型模块的工作流定义部署的历史列表。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param moduleType 模块类型
     * @return 项目的指定类型模块的工作流定义部署的历史列表
     */
    List<ModuleProcessDefinitionBasic> history(Long orgId, Long projectId, String moduleType);

    /**
     * 删除模块工作流部署。
     *
     * @param operator   操作者信息
     * @param orgId      组织信息
     * @param projectId  项目 ID
     * @param version    工作流部署信息更新版本号
     */
    void delete(OperatorDTO operator, Long orgId, Long projectId, long version);


    /**
     * 删除模块工作流部署。
     *
     * @param operator   操作者信息
     * @param orgId      组织信息
     * @param projectId  项目 ID
     * @param funcPart   功能块
     * @param version    工作流部署信息更新版本号
     */
    void delete(OperatorDTO operator, Long orgId, Long projectId, String funcPart, long version);

}
