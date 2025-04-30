package com.ose.tasks.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.HierarchyWBSDTO;
import com.ose.tasks.dto.ProjectInfoDTO;
import com.ose.tasks.dto.ProjectModifyDTO;
import com.ose.tasks.dto.StageVersionDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.StageVersion;
import org.springframework.data.domain.Page;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 项目管理服务接口。
 */
public interface ProjectInterface {

    public static final String TASK_DB = "ose_tasks";
    public static final String ISSUE_DB = "ose_issues";
    String IMPORT_FILE_TYPE_AREA = "Area";

    List<String> AREA_NODE_TYPES = Arrays.asList(
        "AREA",
        "SUB_AREA"
//        "WP01",
//        "WP02",
//        "WP03"
    );

    // 建造视图可选节点类型列表
    // 区域模块下可选节点类型添加LAYER_PACKAGE
    List<String> AREA_OPTIONAL_NODE_TYPES = Arrays.asList(
        "SUB_AREA"
//        "WP02",
//        "WP03"
    );


    String IMPORT_FILE_TYPE_SYSTEM = "System";

    String IMPORT_FILE_TYPE_ENGINEERING = "Engineering";


    // 系统视图节点类型列表
    List<String> SYSTEM_NODE_TYPES = Arrays.asList(
        "SECTOR",
        "SUB_SECTOR",
        "SYSTEM"
    );

    // 系统视图可选节点类型列表
    List<String> SYSTEM_OPTIONAL_NODE_TYPES = Collections.singletonList(
        "SUB_SECTOR"
    );

    List<String> ENGINEERING_NODE_TYPES = Arrays.asList(
        "SECTOR",
        "FUNCTION",
        "TYPE"
    );

    List<String> ENGINEERING_OPTIONAL_NODE_TYPES = Collections.singletonList(
        "SECTOR"
    );

    boolean set(Project project);

    Project get(Long projectId);

    /**
     * 取得项目信息。
     *
     * @param orgId     所属组织 ID
     * @param projectId 项目 ID
     * @return 项目信息
     */
    Project get(Long orgId, Long projectId);

    /**
     * 取得项目信息。
     *
     * @param orgId     所属组织 ID
     * @param projectId 项目 ID
     * @param version   项目更新版本号
     * @return 项目信息
     */
    Project get(Long orgId, Long projectId, long version);

    /**
     * 取得指定组织的所有项目。
     *
     * @param orgId   组织 ID
     * @param pageDTO 分页参数
     * @return 项目分页数据
     */
    Page<Project> search(Long orgId, PageDTO pageDTO);

    /**
     * 取得所有尚未结束的项目的 ID。
     *
     * @return 项目 ID 列表
     */
    List<Project> getNotFinishedProjects();

    /**
     * 创建项目。
     *
     * @param operatorDTO 操作者信息
     * @param companyId   公司 ID
     * @param orgId       所属组织 ID
     * @param projectDTO  项目信息
     * @return 项目数据实体
     */
    Project create(
        OperatorDTO operatorDTO,
        Long companyId,
        Long orgId,
        ProjectModifyDTO projectDTO
    );

    /**
     * 更新项目。
     *
     * @param operatorDTO 操作者信息
     * @param orgId       所属组织 ID
     * @param projectId   项目 ID
     * @param projectDTO  项目信息
     * @param version     项目版本号
     * @return 项目数据实体
     */
    Project update(
        OperatorDTO operatorDTO,
        Long orgId,
        Long projectId,
        ProjectModifyDTO projectDTO,
        long version
    );

    /**
     * 删除项目。
     *
     * @param operatorDTO 操作者信息
     * @param orgId       所属组织 ID
     * @param projectId   项目 ID
     * @param version     项目版本号
     */
    void delete(
        OperatorDTO operatorDTO,
        Long orgId,
        Long projectId,
        long version
    );

    /**
     * 关闭项目。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param version   项目更新版本号
     */
    void close(OperatorDTO operator, Long orgId, Long projectId, long version);

    /**
     * 重新开启项目。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param version   项目更新版本号
     */
    void reopen(OperatorDTO operator, Long orgId, Long projectId, long version);

    /**
     * CLONE PROJECT
     */
    boolean cloneProject(Long oldOrgId, Long oldProjectId, String projectName);

    /**
     * 删除项目。
     *
     * @param orgId
     * @param projectId
     * @return
     */
    boolean deleteProject(Long orgId, Long projectId);

    File saveDownloadFile(Long operatorId);


    Page<StageVersion> getStageVersionList(Long orgId, Long projectId, PageDTO pageDTO);

    void creatStageVersionList(Long orgId, Long projectId, StageVersionDTO stageVersionDTO, ContextDTO context);

    StageVersion getStageVersion(Long orgId, Long projectId, Long stageVersionId);

    void deleteStageVersionList(Long orgId, Long projectId,Long id, ContextDTO context);

    List<StageVersion> getStageVersionList(Long orgId, Long projectId, String stage);

    void updateProjectHourStatus(OperatorDTO operator, Long orgId, Long projectId, long version, ProjectModifyDTO projectDTO);

    void enableOvertime(OperatorDTO operator, Long orgId, Long projectId, long version);

    List<HierarchyWBSDTO> getHierarchyInfo(Long orgId, Long projectId, String key);

    void saveWbsSetting(Long orgId, Long projectId, ProjectInfoDTO projectInfoDTO);

    /**
     * 更新项目层级模板。
     *
     * @param operatorDTO 操作者信息
     * @param orgId       所属组织 ID
     * @param projectId   项目 ID
     * @return 项目数据实体
     */
    Project updateHierarchyTemplate(
        OperatorDTO operatorDTO,
        Long orgId,
        Long projectId,
        ProjectModifyDTO projectDTO
    );

    /**
     * 更新项目层级模板。
     *
     * @param operatorDTO 操作者信息
     * @param orgId       所属组织 ID
     * @param projectId   项目 ID
     * @return 项目数据实体
     */
    Project approveOvertime(
        OperatorDTO operatorDTO,
        Long orgId,
        Long projectId,
        ProjectModifyDTO projectDTO
    );
}
