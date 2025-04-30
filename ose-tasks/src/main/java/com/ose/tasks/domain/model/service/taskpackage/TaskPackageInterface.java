package com.ose.tasks.domain.model.service.taskpackage;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.TaskPackageEntityImportDTO;
import com.ose.tasks.dto.TaskPackageImportResultDTO;
import com.ose.tasks.dto.process.EntityProcessDTO;
import com.ose.tasks.dto.taskpackage.*;
import com.ose.tasks.dto.wbs.WBSEntryCriteriaDTO;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.taskpackage.*;
import com.ose.tasks.entity.wbs.entry.WBSEntryPlain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 任务包服务器接口。
 */
public interface TaskPackageInterface {

    /**
     * 创建任务包。
     *
     * @param operator       操作者信息
     * @param orgId          组织 ID
     * @param projectId      项目 ID
     * @param taskPackageDTO 任务包信息
     * @return 任务包信息
     */
    TaskPackageBasic create(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        TaskPackageCreateDTO taskPackageDTO
    );

    /**
     * 查询任务包。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param pageable    分页参数
     * @return 任务包分页数据
     */
    Page<TaskPackagePercent> search(
        Long orgId,
        Long projectId,
        TaskPackageCriteriaDTO criteriaDTO,
        Pageable pageable
    );

    /**
     * 查询任务包关联人员。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @return 任务包分页数据
     */
    List<TaskPackageAUthCriteriaDTO> searchModifyName(
        Long orgId,
        Long projectId
    );

    /**
     * 取得任务包详细信息。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @return 任务包信息
     */
    TaskPackage get(
        Long orgId,
        Long projectId,
        Long taskPackageId
    );

    /**
     * 更新任务包信息。
     *
     * @param operator       操作者信息
     * @param orgId          组织 ID
     * @param projectId      项目 ID
     * @param taskPackageId  任务包 ID
     * @param version        任务包更新版本号
     * @param taskPackageDTO 任务包信息
     * @return 任务包信息
     */
    TaskPackageBasic update(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Long taskPackageId,
        Long version,
        TaskPackageUpdateDTO taskPackageDTO
    );

    /**
     * 设置工作组。
     *
     * @param operator      操作者信息
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param processTeams  工序工作组设置
     */
    void setTeams(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Long taskPackageId,
        List<TaskPackageTeamDTO.TeamDTO> processTeams
    );

    /**
     * 取得工序工作组设置。
     *
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     */
    List<TaskPackageProcessTeamDTO> getTeams(
        Long projectId,
        Long taskPackageId
    );

    /**
     * 设置工作组。
     *
     * @param operator         操作者信息
     * @param projectId        项目 ID
     * @param taskPackageId    任务包 ID
     * @param processDelegates 工序任务分配设置
     */
    void delegate(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Long taskPackageId,
        List<TaskPackageDelegateDTO.DelegateDTO> processDelegates
    );

    /**
     * 取得工序任务委派设置。
     *
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     */
    List<TaskPackageProcessDelegateDTO> getDelegates(
        Long projectId,
        Long taskPackageId
    );

    /**
     * 删除任务包。
     *
     * @param operator      操作者信息
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param version       任务包更新版本号
     */
    void delete(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Long taskPackageId,
        Long version
    );

    /**
     * 导入任务包关联实体。
     *
     * @param orgId
     * @param projectId
     * @param taskPackageEntityImportDTO
     * @param contextDTO
     */
    TaskPackageImportResultDTO importEntities(Long orgId,
                                              Long projectId,
                                              Long taskPackageId,
                                              TaskPackageEntityImportDTO taskPackageEntityImportDTO,
                                              ContextDTO contextDTO
    );

    /**
     * 向任务包添加实体。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param entities      实体列表
     * @return 任务包-实体关系列表
     */
    List<TaskPackageEntityRelation> addEntities(
        Long orgId,
        Long projectId,
        Long taskPackageId,
        List<ProjectNode> entities
    );

    /**
     * 取得任务包关联实体。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param taskPackageEntityRelationSearchDTO      分页参数
     * @return 实体分页数据
     */
    Page<TaskPackageEntityRelation> entities(
        Long orgId,
        Long projectId,
        Long taskPackageId,
        TaskPackageEntityRelationSearchDTO taskPackageEntityRelationSearchDTO
    );

    /**
     * 从任务包中删除实体。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param entityId      实体 ID
     * @return 删除实体的数量
     */
    int deleteEntity(
        Long orgId,
        Long projectId,
        Long taskPackageId,
        String entityId
    );

    /**
     * 设置 WBS 任务包 ID。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    void setWBSTaskPackageInfo(OperatorDTO operator,
                               Long orgId, Long projectId);

    /**
     * 设置 WBS 任务包 ID。
     *
     * @param operator      操作者信息
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     */
    void setWBSTaskPackageInfo(OperatorDTO operator,
                               Long orgId,
                               Long projectId,
                               Long taskPackageId);

    /**
     * 向任务包添加图纸。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param createDTO     图纸信息数据传输对象
     * @return 任务包-图纸关系列表
     */
    List<TaskPackageDrawingRelation> addDrawings(
        Long orgId,
        Long projectId,
        Long taskPackageId,
        TaskPackageDrawingRelationCreateDTO createDTO
    );

    /**
     * 查询任务包图纸信息。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param criteriaDTO   查询条件数据传输对象
     * @param pageable      分页参数
     * @return 任务包-图纸关系分页数据
     */
    Page<TaskPackageDrawingRelation> drawings(
        Long orgId,
        Long projectId,
        Long taskPackageId,
        TaskPackageDrawingRelationCriteriaDTO criteriaDTO,
        Pageable pageable
    );

    /**
     * 查询任务包图纸信息。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param pageDTO      分页参数
     * @return 任务包-图纸关系分页数据
     */
    Page<TaskPackageDrawingDTO> subDrawings(
        Long orgId,
        Long projectId,
        Long taskPackageId,
        PageDTO pageDTO
    );

    /**
     * 取得所有相关图纸。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @return 图纸 ID 列表
     */
    List<Long> allDrawings(
        Long orgId,
        Long projectId,
        Long taskPackageId
    );

    /**
     * 删除任务包中的图纸。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param relationId    关系 ID
     */
    void deleteDrawing(
        Long orgId,
        Long projectId,
        Long taskPackageId,
        Long relationId
    );

    /**
     * 查询任务包关联四级计划。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param criteriaDTO   查询条件
     * @param pageable      分页参数
     * @return 四级计划分页数据
     */
    Page<WBSEntryPlain> wbsEntries(
        Long orgId,
        Long projectId,
        Long taskPackageId,
        WBSEntryCriteriaDTO criteriaDTO,
        Pageable pageable
    );

    /**
     * 取得四级计划所有工序列表。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param criteriaDTO   查询条件
     * @return 四级计划信息列表
     */
    List<EntityProcessDTO> processes(
        Long orgId,
        Long projectId,
        Long taskPackageId,
        WBSEntryCriteriaDTO criteriaDTO
    );

    /**
     * 查询任务包关联四级计划 及汇总信息。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param criteriaDTO   查询条件
     * @return 四级计划分页数据及汇总信息
     */
    TaskPackageDTO wbsEntries(
        Long orgId,
        Long projectId,
        Long taskPackageId,
        WBSEntryCriteriaDTO criteriaDTO
    );

    TaskPackageDTO wbsEntriesSectors(
        Long orgId,
        Long projectId,
        Long taskPackageId
    );

    void updateTaskPackageCount(Long taskPackageId, Double workLoad);

    void addTaskPackageCount(Long taskPackageId, Double workLoad);

    void reCalcTaskPackageCount(Long projectId, Long entityId);

    /**
     * 查询任务包中可添加的实体。
     *
     * @param orgId
     * @param projectId
     * @param taskPackageProjectNodeEntitySearchDTO
     * @return
     */
    Page<ProjectNode> projectNodeEntities(
        final Long orgId,
        final Long projectId,
        TaskPackageProjectNodeEntitySearchDTO taskPackageProjectNodeEntitySearchDTO
    );
}
