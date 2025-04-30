package com.ose.tasks.domain.model.service.material;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.service.EntityInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.material.FMaterialStructureNestDTO;
import com.ose.tasks.dto.material.FMaterialStructureNestImportDTO;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.material.FMaterialRequisitionEntity;
import com.ose.tasks.entity.material.FMaterialStructureNest;
import org.springframework.data.domain.Page;

public interface FMaterialStructureNestInterface extends EntityInterface {

    /**
     * 创建结构套料方案。
     *
     * @param orgId                     组织id
     * @param projectId                 项目id
     * @param fMaterialStructureNestDTO 创建套料结构对象
     * @param context                   创建者
     */
    void create(
        Long orgId,
        Long projectId,
        FMaterialStructureNestDTO fMaterialStructureNestDTO,
        ContextDTO context,
        OperatorDTO operatorDTO
    );

    /**
     * 查询结构套料方案列表。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param pageDTO   分页参数
     * @return
     */
    Page<FMaterialStructureNest> search(
        Long orgId,
        Long projectId,
        PageDTO pageDTO
    );

    /**
     * 查询结构套料方案详情。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 结构套料清单id
     */
    FMaterialStructureNest detail(
        Long orgId,
        Long projectId,
        Long fMaterialStructureNestId
    );

    /**
     * 更新结构套料方案。
     *
     * @param orgId                     组织id
     * @param projectId                 项目id
     * @param fMaterialStructureNestId  结构套料清单id
     * @param fMaterialStructureNestDTO 更新内容
     * @param contextDTO                操作者
     */
    void update(
        Long orgId,
        Long projectId,
        Long fMaterialStructureNestId,
        FMaterialStructureNestDTO fMaterialStructureNestDTO,
        ContextDTO contextDTO,
        OperatorDTO operatorDTO
    );

    /**
     * 删除结构套料方案。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 结构套料清单id
     */
    void delete(
        Long orgId,
        Long projectId,
        Long fMaterialStructureNestId,
        OperatorDTO operatorDTO
    );

    /**
     * 导入套料结构。
     *
     * @param orgId                           组织id
     * @param projectId                       项目id
     * @param batchTask                       批处理任务信息
     * @param operator                        操作者信息
     * @param project                         项目信息
     * @param fMaterialStructureNestImportDTO 节点导入操作数据传输对象
     * @return 批处理执行结果
     */
    BatchResultDTO importStructureNest(
        Long orgId,
        Long projectId,
        Long fMaterialStructureNestId,
        BatchTask batchTask,
        OperatorDTO operator,
        Project project,
        FMaterialStructureNestImportDTO fMaterialStructureNestImportDTO
    );

    /**
     * 启动结构套料流程。
     *
     * @param orgId
     * @param projectId
     * @param fMaterialStructureNestId
     * @param context
     * @param operatorDTO
     */
    void createActivity(Long orgId,
                        Long projectId,
                        Long fMaterialStructureNestId,
                        ContextDTO context,
                        OperatorDTO operatorDTO);

    /**
     * 更改结构方案流程状态。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 套料结构方案id
     */
    BatchResultDTO changeActivityStatus(
        Long orgId,
        Long projectId,
        Long fMaterialStructureNestId,
        OperatorDTO operatorDTO,
        BatchTask batchTask
    );

    /**
     * 关联领料单。
     *
     * @param orgId                     组织id
     * @param projectId                 项目id
     * @param fMaterialStructureNestId  套料方案id
     * @param fMaterialStructureNestDTO 领料单信息
     */
    void saveMaterialRequisitions(
        Long orgId,
        Long projectId,
        Long fMaterialStructureNestId,
        FMaterialStructureNestDTO fMaterialStructureNestDTO,
        ContextDTO contextDTO,
        OperatorDTO operatorDTO
    );

    /**
     * 通过实体id查找领料单信息。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 结构套料id
     * @param contextDTO
     * @param operatorDTO
     * @return
     */
    FMaterialRequisitionEntity findMaterialRequisitionsByEntityId(
        Long orgId,
        Long projectId,
        Long fMaterialStructureNestId,
        ContextDTO contextDTO,
        OperatorDTO operatorDTO);

    /**
     * 设置分包商。
     *
     * @param orgId
     * @param projectId
     * @param fMaterialStructureNestId
     * @param fMaterialStructureNestDTO
     * @param operatorDTO
     * @return
     */
    FMaterialStructureNest setSubcontractor(
        Long orgId,
        Long projectId,
        Long fMaterialStructureNestId,
        FMaterialStructureNestDTO fMaterialStructureNestDTO,
        OperatorDTO operatorDTO
    );
}
