package com.ose.tasks.domain.model.service.taskpackage;

import com.ose.dto.OperatorDTO;
import com.ose.service.EntityInterface;
import com.ose.tasks.dto.taskpackage.*;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.taskpackage.TaskPackageCategory;
import com.ose.tasks.entity.taskpackage.TaskPackageCategoryProcessRelation;
import com.ose.tasks.entity.taskpackage.TaskPackageCategoryProcessRelationBasic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * 任务包类型管理服务接口。
 */
public interface TaskPackageCategoryInterface extends EntityInterface {

    /**
     * 创建任务包类型。
     *
     * @param operator               操作者信息
     * @param orgId                  组织 ID
     * @param projectId              项目 ID
     * @param taskPackageCategoryDTO 任务包类型信息
     * @return 任务包类型数据实体
     */
    TaskPackageCategory create(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        TaskPackageCategoryCreateDTO taskPackageCategoryDTO
    );

    /**
     * 查询任务包类型。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param pageable    分页参数
     * @return 任务包类型分页数据
     */
    Page<TaskPackageCategory> search(
        Long orgId,
        Long projectId,
        TaskPackageCategoryCriteriaDTO criteriaDTO,
        Pageable pageable
    );

    /**
     * 取得任务包类型详细信息。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param categoryId 任务包类型 ID
     * @return 任务包类型
     */
    TaskPackageCategory get(Long orgId, Long projectId, Long categoryId);

    /**
     * 更新任务包类型。
     *
     * @param operator               操作者信息
     * @param orgId                  组织 ID
     * @param projectId              项目 ID
     * @param categoryId             任务包分类 ID
     * @param version                任务包更新版本号
     * @param taskPackageCategoryDTO 任务包类型信息
     * @return 任务包类型数据实体
     */
    TaskPackageCategory update(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Long categoryId,
        Long version,
        TaskPackageCategoryUpdateDTO taskPackageCategoryDTO
    );

    /**
     * 删除任务包类型。
     *
     * @param operator   操作者信息
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param categoryId 任务包分类 ID
     * @param version    任务包分类更新版本号
     */
    void delete(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Long categoryId,
        Long version
    );

    /**
     * 创建任务包类型-工序关系。
     *
     * @param operator    操作者信息
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param categoryId  任务包类型 ID
     * @param relationDTO 关系数据
     * @return 关系数据
     */
    TaskPackageCategoryProcessRelationBasic addProcess(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Long categoryId,
        TaskPackageCategoryProcessRelationCreateDTO relationDTO
    );

    /**
     * 查询任务包类型-工序关系。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param categoryId 任务包类型 ID
     * @param pageable   分页参数
     * @return 关系分页数据
     */
    Page<TaskPackageCategoryProcessRelation> getProcesses(
        Long orgId,
        Long projectId,
        Long categoryId,
        Pageable pageable
    );

    /**
     * 删除任务包类型-工序关系。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param categoryId 任务包类型 ID
     * @param processId  工序 ID
     */
    void deleteProcess(
        Long orgId,
        Long projectId,
        Long categoryId,
        Long processId
    );

    /**
     * 取得所有已添加到指定类型任务包的实体的 ID。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param categoryId 任务包类型 ID
     * @return 实体 ID 集合
     */
    Set<Long> entityIDs(
        Long orgId,
        Long projectId,
        Long categoryId
    );

    /**
     * 取得任务包类型对应的实体类型。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param categoryId 任务包类型 ID
     * @return 实体类型列表
     */
    List<BpmEntitySubType> entityTypes(
        Long orgId,
        Long projectId,
        Long categoryId
    );

    TaskPackageTypeEnumDTO getTypeEnums(Long orgId, Long projectId);
}
