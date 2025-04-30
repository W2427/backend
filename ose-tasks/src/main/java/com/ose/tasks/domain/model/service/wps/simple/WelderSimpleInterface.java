package com.ose.tasks.domain.model.service.wps.simple;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.service.EntityInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.HierarchyNodeImportDTO;
import com.ose.tasks.dto.wps.simple.*;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wps.simple.WelderGradeWelderSimplifiedRelation;
import com.ose.tasks.entity.wps.simple.WelderSimplified;
import org.springframework.data.domain.Page;

import java.io.File;

public interface WelderSimpleInterface extends EntityInterface {

    /**
     * 创建焊工
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param context
     * @param welderSimpleCreateDTO 创建信息
     */
    WelderSimplified create(
        Long orgId,
        Long projectId,
        ContextDTO context,
        WelderSimpleCreateDTO welderSimpleCreateDTO
    );

    /**
     * 编辑焊工
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param context
     * @param welderSimpleUpdateDTO 创建信息
     */
    void update(
        Long orgId,
        Long projectId,
        Long welderGradeId,
        ContextDTO context,
        WelderSimpleUpdateDTO welderSimpleUpdateDTO
    );

    /**
     * 打开焊工账户
     *
     * @param orgId 组织ID
     * @param projectId 项目ID
     * @param welderSimplifiedId 焊工ID
     * @param contextDTO
     */
    void open(
        Long orgId,
        Long projectId,
        Long welderSimplifiedId,
        ContextDTO contextDTO
    );

    /**
     * 停用焊工账户
     *
     * @param orgId 组织ID
     * @param projectId 项目ID
     * @param welderSimplifiedId 焊工ID
     * @param contextDTO
     */
    void deactivate(
        Long orgId,
        Long projectId,
        Long welderSimplifiedId,
        ContextDTO contextDTO
    );

    /**
     * 焊工详情
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param welderSimpleId 焊工id
     */
    WelderSimplified detail(
        Long orgId,
        Long projectId,
        Long welderSimpleId
    );

    /**
     * 焊工列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    Page<WelderSimplified> search(
        Long orgId,
        Long projectId,
        WelderSimpleSearchDTO wpsSimpleSearchDTO
    );

    /**
     * 删除焊工
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param welderSimpleId 焊工id
     */
    void delete(
        Long orgId,
        Long projectId,
        Long welderSimpleId,
        ContextDTO context
    );

    /**
     * 添加焊工证。
     *
     * @param orgId
     * @param projectId
     * @param welderSimpleId
     * @param wpsSimpleRelationDTO
     * @param context
     */
    void addWelderGrade(
        Long orgId,
        Long projectId,
        Long welderSimpleId,
        WpsSimpleRelationDTO wpsSimpleRelationDTO,
        ContextDTO context
    );

    /**
     * 查找焊工证列表。
     *
     * @param orgId
     * @param projectId
     * @param welderSimpleId
     * @param wpsSimpleRelationSearchDTO
     * @return
     */
    Page<WelderGradeWelderSimplifiedRelation> searchWelderGrade(
        Long orgId,
        Long projectId,
        Long welderSimpleId,
        WpsSimpleRelationSearchDTO wpsSimpleRelationSearchDTO
    );

    /**
     * 删除焊工证。
     *
     * @param orgId
     * @param projectId
     * @param welderSimpleId
     * @param welderGradeRelationSimpleId
     * @param context
     */
    void deleteWelderGrade(
        Long orgId,
        Long projectId,
        Long welderSimpleId,
        Long welderGradeRelationSimpleId,
        ContextDTO context
    );

    /**
     * 保存实体下载临时文件。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param welderSimpleSearchD 查询条件
     * @param operatorId  项目ID
     * @return 实体下载临时文件
     */
    File saveDownloadFile(Long orgId, Long projectId, WelderSimpleSearchDTO welderSimpleSearchD, Long operatorId);

    /**
     * 导入焊工
     * @param batchTask
     * @param operator
     * @param project
     * @param context
     * @param welderImportDTO
     * @return
     */
    BatchResultDTO importWelder(BatchTask batchTask, OperatorDTO operator, Project project, ContextDTO context, HierarchyNodeImportDTO welderImportDTO);
}
