package com.ose.tasks.domain.model.service;

import com.ose.dto.OperatorDTO;
import com.ose.service.EntityInterface;
import com.ose.tasks.dto.*;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;

import jakarta.persistence.Tuple;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 项目层级结构管理服务接口。
 */
public interface HierarchyInterface extends EntityInterface {

    /**
     * 添加节点。
     *
     * @param operatorDTO 操作者信息
     * @param project     所属项目信息
     * @param entryDTO    节点信息
     * @return 节点信息
     */
    HierarchyNode add(
        OperatorDTO operatorDTO,
        Project project,
        HierarchyNodeModifyDTO entryDTO
    );

    /**
     * 更新节点。
     *
     * @param operatorDTO 操作者信息
     * @param project     所属项目信息
     * @param entryId     节点 ID
     * @param entryDTO    节点信息
     * @return 节点信息
     */
    HierarchyNode update(
        OperatorDTO operatorDTO,
        Project project,
        Long entryId,
        HierarchyNodeModifyDTO entryDTO
    );

    /**
     * 删除节点。
     *
     * @param operatorDTO 操作者信息
     * @param project     所属项目信息
     * @param nodeId      节点 ID
     */
    ProjectNode delete(
        OperatorDTO operatorDTO,
        Project project,
        Long nodeId
    );

    /**
     * 通过实体ID 删除节点。（暂时只有实体删除时候调用）
     *
     * @param operatorDTO 操作者信息
     * @param project     所属项目信息
     * @param orgId       组织 ID
     * @param entityId    实体 ID
     */
    void delete(
        OperatorDTO operatorDTO,
        Project project,
        Long orgId,
        Long entityId
    );

    /**
     * 通过实体ID和层级类别 删除所有层级信息（只删除层级信息，project_nodes和entity_*表数据不动）。
     *
     * @param operatorDTO   操作者信息
     * @param project       所属项目信息
     * @param orgId         组织 ID
     * @param entityId      实体 ID
     * @param hierarchyType 层级类别
     */
    void deleteOnlyHierarchyInfoByHierarchyType(
        OperatorDTO operatorDTO,
        Project project,
        Long orgId,
        Long entityId,
        String hierarchyType
    );

    /**
     * 通过实体ID 删除所有维度所有层级信息（只删除层级信息，project_nodes和entity_*表数据不动）。
     * （SPOOL，PIPE_PIECE, WELD_JOINT, COMPONENT实体删除所有层级信息时用）
     *
     * @param operatorDTO 操作者信息
     * @param project     所属项目信息
     * @param orgId       组织 ID
     * @param entityId    实体 ID
     */
    void deleteAllHierarchyInfoByEntityId(
        OperatorDTO operatorDTO,
        Project project,
        Long orgId,
        Long entityId
    );

    /**
     * 取得项目节点列表。
     *
     * @param orgId      组织 ID
     * @param projectId  所属项目 ID
     * @param rootNodeId 根节点 ID
     * @param depth      查询深度
     * @return 项目节点列表
     */
    List<HierarchyNode> getFlatList(Long orgId, Long projectId, Long rootNodeId, int depth);

    /**
     * 将数据实体层级列表转换为数据传输对象层级列表。
     *
     * @param nodes 数据实体层级列表
     * @return 数据传输对象层级列表
     */
    List<HierarchyNodePutDTO> convertToHierarchicalDTOList(List<HierarchyNode> nodes);

    /**
     * 取得节点。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entryId   节点 ID
     * @return 节点信息
     */
    HierarchyNode get(Long orgId, Long projectId, Long entryId);

    /**
     * 取得项目层级结构。
     *
     * @param orgId        组织 ID
     * @param projectId    项目 ID
     * @param rootNodeId   根节点 ID
     * @param depth        取得深度
     * @param hierarchyType    维度类型
     * @param needEntity   是否要显示实体
     * @param hierarchical 是否要按层级返回
     * @return 层级结构列表
     */
    List<HierarchyNodeDTO> getHierarchy(
        Long orgId,
        Long projectId,
        Long rootNodeId,
        int depth,
        String hierarchyType,
        String drawingType,
        Boolean needEntity,
        Boolean hierarchical
    );

    /**
     * 保存项目层级结构。
     *
     * @param operatorDTO       操作者信息
     * @param project           项目信息
     * @param parentNodeId      上级节点 ID
     * @param hierarchyDtoMap   多维度数据列表Map
     * @param merge             是否合并旧数据
     */
    List<String> saveHierarchy(
        OperatorDTO operatorDTO,
        Project project,
        Long parentNodeId,
        LinkedHashMap<String,List<HierarchyNodePutDTO>> hierarchyDtoMap,
        boolean merge
    );

    /**
     * 导入层级结构。
     *
     * @param batchTask     批处理任务信息
     * @param operator      操作者信息
     * @param project       项目信息
     * @param rootNodeId    根节点 ID
     * @param nodeImportDTO 节点导入操作数据传输对象
     * @return 批处理执行结果
     */
    BatchResultDTO importHierarchy(
        BatchTask batchTask,
        OperatorDTO operator,
        Project project,
        Long rootNodeId,
        HierarchyNodeImportDTO nodeImportDTO
    );

    /**
     * 将节点移动到层级结构的指定位置。
     *
     * @param operator                操作者信息
     * @param project                 项目信息
     * @param nodeType                移动对象节点类型
     * @param entityType              移动对象节点的实体类型
     * @param entityId                移动对象节点的实体 ID
     * @param targetHierarchyNodeType 移动目标层级节点的类型
     * @param targetHierarchyNodeId   移动目标层级节点的 ID
     */
    void moveTo(
        OperatorDTO operator,
        Project project,
        String entityType,
        Long entityId,
        String targetEntityType,
        Long targetHierarchyNodeId
    );

    /**
     * 更新层级节点上的计划实施进度信息。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    void refreshWBSProgress(
        OperatorDTO operator,
        Long orgId,
        Long projectId
    );


    /**
     * 通过实体ID，层级类型取得模块下的第一个节点信息。
     * 例：
     * 维度     层级类型                        取得节点的NODE_TYPE      取得节点的ENTITY_TYPE
     * 区域     ISO                            ENTITY                  ISO
     * 试压包   PRESSURE_TEST_PACKAGE          PRESSURE_TEST_PACKAGE    --
     * 子系统   SUB_SYSTEM                     SUB_SYSTEM               --
     * 层      LAYER_PACKAGE                  LAYER_PACKAGE            --
     * 清洁包   CLEAN_PACKAGE                  CLEAN_PACKAGE            --
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param entityId      实体ID
     * @param hierarchyType 层级类型
     * @return HierarchyNodeDTO
     */
    HierarchyNodeDTO getHierarchyNodeByEntityIdAndHierarchyType(
        Long orgId,
        Long projectId,
        Long entityId,
        String hierarchyType
    );

    /**
     * 设置实体信息及项目节点的所属管线及单管实体的 ID。
     *
     * @param projectId 项目 ID
     */
    void setParentISOAndSpoolEntityIDs(Long projectId);

    /**
     * 根据实体ID取得所有的层级节点。
     *
     * @param projectId 项目 ID
     */
    List<Tuple> getHierarchyNodeByEntityId(Long orgId, Long projectId, Long hierarchyId);

    /**
     * 设置删除一个WP0？之后的WP01/02/03/04/05
     *
     * @param projectId 项目ID
     * @param wpName    wp01 wp02 wp03 wp04 wp05
     * @param entityId  实体ID
     */
    void setDeletedParentEntityIdOnStructureEntities(
        Long projectId,
        String wpName,
        Long entityId);

    /**
     * 取得任务包选择需要的 项目层级结构。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param depth         取得深度
     * @param nodeType      叶子节点类型
     * @param tpcEntityType 任务包类型 中的 实体类型
     * @return 层级结构列表
     */
    List<HierarchyNodeDTO> getTaskPackageHierarchy(Long orgId,
                                                   Long projectId,
                                                   int depth,
                                                   String tpcEntityType);


    /**
     * 保存项目层级结构。
     *
     * @param operatorDTO       操作者信息
     * @param project           项目信息
     * @param parentNodeId      上级节点 ID
     * @param children          多维度数据列表
     * @param merge             是否合并旧数据
     */
    boolean saveHierarchy(OperatorDTO operatorDTO,
                          Project project,
                          Long parentNodeId,
                          List<HierarchyNodePutDTO> children,
                          String hierarchyType,
                          boolean merge);

    /**
     * 删除冗余的 hierarchy Node 节点
     * @param projectId
     */
    void deleteRedundantNodes(Long projectId);

    void cancel(OperatorDTO operator, Project project, Long orgId, Long entityId);
}
