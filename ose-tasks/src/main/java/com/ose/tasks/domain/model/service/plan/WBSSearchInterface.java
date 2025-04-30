package com.ose.tasks.domain.model.service.plan;

import com.ose.dto.jpql.VersionQLDTO;
import com.ose.service.EntityInterface;
import com.ose.tasks.dto.WBSEntryDTO;
import com.ose.tasks.dto.WBSEntryDelegateDTO;
import com.ose.tasks.dto.process.EntityProcessRelationsDTO;
import com.ose.tasks.dto.wbs.WBSEntryQueryDTO;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.entity.wbs.entry.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 计划管理服务接口。
 */
public interface WBSSearchInterface extends EntityInterface {

    /**
     * 取得工序-实体类型映射表。
     * 映射表键值对形式：
     * 键：【工序阶段/工序/实体类型】字符串
     * 值：实体子类型集合
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return 工序-实体类型映射表
     */
    EntityProcessRelationsDTO getProcessEntityTypeRelations(Long orgId, Long projectId);

    /**
     * 取得 WBS 更新版本号。
     *
     * @param projectId 项目 ID
     * @return WBS 更新版本号 DTO
     */
    VersionQLDTO getWBSVersionDTO(Long projectId);

    /**
     * 取得 WBS 更新版本号。
     *
     * @param projectId 项目 ID
     * @return WBS 更新版本号
     */
    Long getWBSVersion(Long projectId);

    /**
     * 取得 WBS 层级结构。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param wbsEntryId WBS 条目 ID
     * @param queryDTO   查询条件
     * @return WBS 条目列表
     */
    List<WBSEntryDTO> getWBSHierarchy(
        Long orgId,
        Long projectId,
        Long wbsEntryId,
        WBSEntryQueryDTO queryDTO
    );

    /**
     * 取得前/后置任务信息。
     *
     * @param projectId 项目 ID
     * @param entries   计划条目
     * @return 前/后置任务信息
     */
    Map<Long, Object> getReferencedWBSEntries(Long projectId, List<WBSEntryDTO> entries);

    /**
     * 取得 WBS 条目详细信息。
     *
     * @param projectId  项目 ID
     * @param wbsEntryId WBS 条目 ID
     */
    WBSEntry get(Long projectId, Long wbsEntryId);

    /**
     * 取得 WBS 条目前置任务列表。
     *
     * @param projectId  项目 ID
     * @param wbsEntryId WBS 条目 ID
     * @param pageable   分页参数
     */
    Page<WBSEntryPredecessorDetail> predecessors(Long projectId, Long wbsEntryId, Pageable pageable);

    /**
     * 取得 WBS 条目后置任务列表。
     *
     * @param projectId  项目 ID
     * @param wbsEntryId WBS 条目 ID
     * @param pageable   分页参数
     */
    Page<WBSEntrySuccessorDetail> successors(Long projectId, Long wbsEntryId, Pageable pageable);

    /**
     * 取得 WBS 条目的所有上级的信息。
     *
     * @param projectId  项目 ID
     * @param wbsEntryId WBS 条目 ID
     * @param appendSelf 是否返回条目自身信息
     */
    List<WBSEntryPlain> getParents(Long projectId, Long wbsEntryId, boolean appendSelf);

    /**
     * 取得 WBS 实体条目。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param moduleId  模块层级 ID
     */
    List<WBSEntry> getWBSEntityEntries(Long orgId, Long projectId, Long moduleId);

    /**
     * 取得 WBS 三级计划跟四级计划设定的用户跟施工组的信息。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param wbsEntryId 计划 ID
     */
    WBSEntryDelegateDTO getWBSEntryDelegateAndTeam(Long orgId, Long projectId, Long wbsEntryId);

    /**
     * 取得可执行的计划任务。
     *
     * @param userId         用户 ID
     * @param userPrivileges 用户权限映射表（组织 - 权限集合）
     * @param projectId      项目 ID
     * @param wbsEntryId     计划条目 ID
     * @param pageable       分页参数
     * @return 计划任务分页参数
     */
    Page<WBSEntryActivityInstance> getWBSTasks(
        Long userId,
        Map<Long, Set<String>> userPrivileges,
        Long projectId,
        Long wbsEntryId,
        Pageable pageable
    );

    /**
     * 查询四级计划。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param wbsEntryId 计划 ID
     * @param queryDTO   查询条件
     * @param pageable   分页参数
     * @return 四级计划分页数据
     */
    Page<WBSEntryWithRelations> searchEntityProcesses(
        Long orgId,
        Long projectId,
        Long wbsEntryId,
        WBSEntryQueryDTO queryDTO,
        Pageable pageable
    );

    /**
     * 取得尚未开始的四级计划。
     *
     * @param projectId   项目 ID
     * @param timestamp   时间戳
     * @param fromEntryId 查询开始条目 ID
     * @param pageable    分页参数
     * @return 四级计划列表
     */
    List<WBSEntry> getNotStartedEntityProcesses(
        Long projectId,
        Double timestamp,
        Long fromEntryId,
        Pageable pageable
    );

    /**
     * 取得尚未开始的计划条目的数量。
     *
     * @param projectId 项目 ID
     * @param timestamp Unix 时间戳（秒）
     * @return 尚未开始的计划条目的数量
     */
    int countNotStartedEntityProcesses(
        Long projectId,
        Double timestamp
    );

    /**
     * 根据实体和工序获取四级计划。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param entityId  实体ID
     * @param process   工序英文代码
     * @return 四级计划列表
     */
    List<WBSEntry> getByEntityAndProcess(
        Long orgId,
        Long projectId,
        Long entityId,
        String process
    );

    /**
     * 取得实体信息。
     *
     * @param entityType 实体类型
     * @param entityId   实体 ID
     * @param discipline 专业
     * @return 实体信息
     */
    WBSEntityBase getEntity(Long projectId, String entityType, Long entityId, String discipline);

}
