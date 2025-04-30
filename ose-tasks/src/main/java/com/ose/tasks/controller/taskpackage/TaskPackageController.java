package com.ose.tasks.controller.taskpackage;

import com.ose.auth.annotation.SetOrgInfo;
import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.feign.RequestWrapper;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.taskpackage.TaskPackageAPI;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.plan.WBSSearchInterface;
import com.ose.tasks.domain.model.service.taskpackage.TaskPackageCategoryInterface;
import com.ose.tasks.domain.model.service.taskpackage.TaskPackageInterface;
import com.ose.tasks.dto.*;
import com.ose.tasks.dto.process.EntityProcessDTO;
import com.ose.tasks.dto.taskpackage.*;
import com.ose.tasks.dto.wbs.WBSEntryCriteriaDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.taskpackage.*;
import com.ose.tasks.vo.setting.BatchTaskCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ose.constant.HttpResponseHeaders.DATA_REVISION;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Tag(name = "任务包接口")
@RestController
public class TaskPackageController extends BaseController implements TaskPackageAPI {


    private final TaskPackageInterface taskPackageService;


    private final TaskPackageCategoryInterface taskPackageCategoryInterface;

    private final ProjectNodeRepository projectNodeRepository;
    private final WBSSearchInterface wbsSearchService;

    private final ProjectInterface projectService;

    private final HierarchyInterface hierarchyService;
    private final BatchTaskInterface batchTaskService;

    /**
     * 构造方法。
     */
    @Autowired
    public TaskPackageController(
        TaskPackageInterface taskPackageService,
        TaskPackageCategoryInterface taskPackageCategoryInterface,
        ProjectNodeRepository projectNodeRepository, WBSSearchInterface wbsSearchService,
        ProjectInterface projectService, HierarchyInterface hierarchyService,
        BatchTaskInterface batchTaskService) {
        this.taskPackageService = taskPackageService;
        this.taskPackageCategoryInterface = taskPackageCategoryInterface;
        this.projectNodeRepository = projectNodeRepository;
        this.wbsSearchService = wbsSearchService;
        this.projectService = projectService;
        this.hierarchyService = hierarchyService;
        this.batchTaskService = batchTaskService;
    }



    @Override
    @Operation(description = "创建任务包")
    @WithPrivilege
    public JsonObjectResponseBody<TaskPackageBasic> create(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Valid TaskPackageCreateDTO taskPackageDTO
    ) {
        return new JsonObjectResponseBody<>(
            taskPackageService.create(
                getContext().getOperator(),
                orgId,
                projectId,
                taskPackageDTO
            )
        );
    }

    @Override
    @Operation(description = "查询任务包")
    @WithPrivilege
    @SetUserInfo
    @SetOrgInfo
    public JsonListResponseBody<TaskPackagePercent> search(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        TaskPackageCriteriaDTO criteriaDTO
    ) {
        return new JsonListResponseBody<>(
            taskPackageService
                .search(orgId, projectId, criteriaDTO, criteriaDTO.toPageable())
        ).setIncluded(taskPackageCategoryInterface);
    }

    @Override
    @Operation(description = "查询任务包的关联人员")
    @WithPrivilege
    @SetUserInfo
    @SetOrgInfo
    public JsonListResponseBody<TaskPackageAUthCriteriaDTO> searchModifyName(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId
    ) {
        return new JsonListResponseBody<>(
            taskPackageService
                .searchModifyName(orgId, projectId)
        );
    }

    @Override
    @Operation(description = "取得任务包详细信息")
    @WithPrivilege
    @SetUserInfo
    @SetOrgInfo
    public JsonObjectResponseBody<TaskPackage> get(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包 ID") Long taskPackageId
    ) {
        return new JsonObjectResponseBody<>(
            taskPackageService.get(orgId, projectId, taskPackageId)
        );
    }

    @Override
    @Operation(description = "更新任务包信息")
    @WithPrivilege
    public JsonResponseBody update(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包 ID") Long taskPackageId,
        @RequestParam @Parameter(description = "任务包更新版本号") Long version,
        @RequestBody @Valid TaskPackageUpdateDTO taskPackageDTO
    ) {

        getResponse().setHeader(
            DATA_REVISION,
            taskPackageService
                .update(
                    getContext().getOperator(),
                    orgId,
                    projectId,
                    taskPackageId,
                    version,
                    taskPackageDTO
                )
                .getVersion()
                .toString()
        );

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "工作组分配")
    @WithPrivilege
    public JsonResponseBody setTeams(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包 ID") Long taskPackageId,
        @RequestBody @Valid TaskPackageTeamDTO teamDTO
    ) {
        taskPackageService.setTeams(
            getContext().getOperator(),
            orgId,
            projectId,
            taskPackageId,
            teamDTO.getTeams()
        );
        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "取得工序工作组配置")
    @WithPrivilege
    public JsonListResponseBody<TaskPackageProcessTeamDTO> getTeams(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包 ID") Long taskPackageId
    ) {
        return new JsonListResponseBody<>(
            taskPackageService.getTeams(projectId, taskPackageId)
        );
    }

    @Override
    @Operation(description = "任务委派")
    @WithPrivilege
    public JsonResponseBody delegate(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包 ID") Long taskPackageId,
        @RequestBody @Valid TaskPackageDelegateDTO delegateDTO
    ) {
        taskPackageService.delegate(
            getContext().getOperator(),
            orgId,
            projectId,
            taskPackageId,
            delegateDTO.getDelegates()
        );
        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "取得工序任务配置")
    @WithPrivilege
    public JsonListResponseBody<TaskPackageProcessDelegateDTO> delegates(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包 ID") Long taskPackageId
    ) {
        return new JsonListResponseBody<>(
            taskPackageService.getDelegates(projectId, taskPackageId)
        );
    }

    @Override
    @Operation(description = "删除任务包信息")
    @WithPrivilege
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包 ID") Long taskPackageId,
        @RequestParam @Parameter(description = "任务包更新版本号") Long version
    ) {

        taskPackageService.delete(
            getContext().getOperator(),
            orgId,
            projectId,
            taskPackageId,
            version
        );

        return new JsonResponseBody();
    }


    /**
     * 取得添加任务包对应的层级结构。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-package-hierarchy",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    @Operation(description = "取得添加任务包对应的层级结构")
    public JsonObjectResponseBody<HierarchyDTO<HierarchyNodeDTO>> getHierarchy(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam(name = "depth", defaultValue = "0") @Parameter(description = "展开深度") int depth,
        @RequestParam(name = "viewType") @Parameter(description = "视图类型") String viewType,
        @RequestParam(name = "needEntity", required = false, defaultValue = "true") @Parameter(description = "是否需要返回实体节点") Boolean needEntity,
        @RequestParam("entityType") @Parameter(description = "任务包类型中指定的实体类型") String entityType
    ) {
        Project project = projectService.get(orgId, projectId);

        HierarchyDTO<HierarchyNodeDTO> responseData = new HierarchyDTO<>();

        responseData.setVersion(project.getVersion());


        responseData.setChildren(
            hierarchyService.getTaskPackageHierarchy(
                orgId, projectId, depth, entityType
            )
        );

        return new JsonObjectResponseBody<>(responseData);
    }

    @Operation(summary = "导入任务包关联实体接口")
    @Override
    @WithPrivilege
    @PostMapping(
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/{taskPackageId}/entities-import",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonObjectResponseBody<TaskPackageImportResultDTO> importTaskPackageEntities(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "任务包ID") Long taskPackageId,
        @RequestBody TaskPackageEntityImportDTO taskPackageEntityImportDTO
    ){
        return new JsonObjectResponseBody<TaskPackageImportResultDTO>(
            getContext(),
            taskPackageService.importEntities(
                orgId,
                projectId,
                taskPackageId,
                taskPackageEntityImportDTO,
                getContext())
        );
    }

    @Override
    @Operation(description = "向任务包中添加实体")
    @WithPrivilege
    public JsonListResponseBody<TaskPackageEntityRelation> addEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包 ID") Long taskPackageId,
        @RequestBody TaskPackageEntityRelationCreateDTO relationDTO
    ) {
        ContextDTO context = getContext();
        String authorization = context.getAuthorization();
        String userAgent = context.getUserAgent();

        final RequestAttributes attributes = new ServletRequestAttributes(
            new RequestWrapper(context.getRequest(), authorization, userAgent),
            context.getResponse()
        );

        Set<Long> entityIDs = new HashSet<>();
        List<ProjectNode> entities = new ArrayList<>();
        ProjectNode entity;

        for (TaskPackageEntityRelationCreateDTO.EntityDTO entityDTO : relationDTO.getEntities()) {
            if (!entityIDs.contains(entityDTO.getEntityId())) {

                entity = projectNodeRepository.findById(
                    entityDTO.getEntityId()
                ).orElse(null);

                if (entity == null) {
                    throw new BusinessError("error.task-package.entity-not-found");
                }

                entityIDs.add(entity.getId());
                entities.add(entity);
            }
        }

        List<TaskPackageEntityRelation> relations = taskPackageService
            .addEntities(orgId, projectId, taskPackageId, entities);

        if (relations.size() > 0) {
            final OperatorDTO operator = getContext().getOperator();

            RequestContextHolder.setRequestAttributes(attributes, true);
            context.setContextSet(true);





            Project project = projectService.get(orgId, projectId);

            batchTaskService.runConstructTaskExecutor(
                null,
                project,
                BatchTaskCode.TASK_PACKAGE,
                false,
                context,
                batchTask -> {
                    taskPackageService.setWBSTaskPackageInfo(operator, orgId, projectId, taskPackageId);
                    return new BatchResultDTO();
                });
        }

        return new JsonListResponseBody<>(relations);
    }

    @Override
    @Operation(description = "查询任务包关联实体")
    @WithPrivilege
    public JsonListResponseBody<TaskPackageEntityRelation> entities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包 ID") Long taskPackageId,
        TaskPackageEntityRelationSearchDTO taskPackageEntityRelationSearchDTO
    ) {
        return new JsonListResponseBody<>(
            taskPackageService.entities(orgId, projectId, taskPackageId, taskPackageEntityRelationSearchDTO)
        );
    }

    @Override
    @Operation(
        summary = "删除任务包与实体的关联关系",
        description = "多个实体 ID 通过逗号分隔"
    )
    @WithPrivilege
    public JsonResponseBody deleteEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包 ID") Long taskPackageId,
        @PathVariable @Parameter(description = "实体 ID") String entityId
    ) {
        ContextDTO context = getContext();
        String authorization = context.getAuthorization();
        String userAgent = context.getUserAgent();
        final RequestAttributes attributes = new ServletRequestAttributes(
            new RequestWrapper(context.getRequest(), authorization, userAgent),
            context.getResponse()
        );
        RequestContextHolder.setRequestAttributes(attributes, true);
        context.setContextSet(true);

        if (taskPackageService.deleteEntity(orgId, projectId, taskPackageId, entityId) > 0) {

            final OperatorDTO operator = context.getOperator();







            Project project = projectService.get(orgId, projectId);

            batchTaskService.runConstructTaskExecutor(
                null,
                project,
                BatchTaskCode.TASK_PACKAGE,
                false,
                context,
                batchTask -> {
                    taskPackageService.setWBSTaskPackageInfo(operator, orgId, projectId, taskPackageId);
                    return new BatchResultDTO();
                });
        }
        return new JsonResponseBody();
    }



    @Override
    @Operation(description = "向任务包中添加图纸")
    @WithPrivilege
    public JsonListResponseBody<TaskPackageDrawingRelation> addDrawings(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包 ID") Long taskPackageId,
        @RequestBody @Valid TaskPackageDrawingRelationCreateDTO relationDTO
    ) {
        return new JsonListResponseBody<>(
            taskPackageService.addDrawings(orgId, projectId, taskPackageId, relationDTO)
        );
    }

    @Override
    @Operation(description = "查询任务包图纸")
    @WithPrivilege
    public JsonListResponseBody<TaskPackageDrawingRelation> drawings(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包 ID") Long taskPackageId,
        TaskPackageDrawingRelationCriteriaDTO criteriaDTO
    ) {
        return new JsonListResponseBody<>(
            taskPackageService.drawings(orgId, projectId, taskPackageId, criteriaDTO, criteriaDTO.toPageable())
        );
    }

    @Override
    @Operation(description = "查询任务包自动关联的子图纸")
    @WithPrivilege
    public JsonListResponseBody<TaskPackageDrawingDTO> subDrawings(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包 ID") Long taskPackageId,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            taskPackageService.subDrawings(orgId, projectId, taskPackageId, pageDTO)
        );
    }

    @Override
    @Operation(description = "取得所有相关图纸的 ID")
    @WithPrivilege
    public JsonObjectResponseBody<TaskPackageDrawingIdDTO> allDrawings(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包 ID") Long taskPackageId
    ) {
        return new JsonObjectResponseBody<>(
            new TaskPackageDrawingIdDTO(
                taskPackageService.allDrawings(orgId, projectId, taskPackageId)
            )
        );
    }

    @Override
    @Operation(description = "将图纸从任务包删除")
    @WithPrivilege
    public JsonResponseBody deleteDrawing(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包 ID") Long taskPackageId,
        @PathVariable @Parameter(description = "任务包图纸关系 ID") Long relationId
    ) {
        taskPackageService.deleteDrawing(orgId, projectId, taskPackageId, relationId);
        return new JsonResponseBody();
    }


    @Override
    @Operation(description = "查询任务包关联实体的工序的计划")
    @WithPrivilege
    @SetOrgInfo
    public JsonObjectResponseBody<TaskPackageDTO> wbsEntries(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包 ID") Long taskPackageId,
        WBSEntryCriteriaDTO criteriaDTO
    ) {
        return new JsonObjectResponseBody<TaskPackageDTO>(
            taskPackageService.wbsEntries(orgId, projectId, taskPackageId, criteriaDTO)
        );
    }


    @Override
    @Operation(description = "查询任务包关联实体的工序的计划")
    @WithPrivilege
    @SetOrgInfo
    public JsonObjectResponseBody<TaskPackageDTO> wbsEntriesSectors(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包 ID") Long taskPackageId
    ) {
        return new JsonObjectResponseBody<TaskPackageDTO>(
            taskPackageService.wbsEntriesSectors(orgId, projectId, taskPackageId)
        );
    }



    @Override
    @Operation(description = "查询任务包工序信息")
    @WithPrivilege
    public JsonListResponseBody<EntityProcessDTO> processes(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包 ID") Long taskPackageId,
        WBSEntryCriteriaDTO criteriaDTO
    ) {
        return new JsonListResponseBody<>(
            taskPackageService.processes(orgId, projectId, taskPackageId, criteriaDTO)
        );
    }


    /**
     * 取得添加任务包对应的层级结构。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/project-node-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @Operation(description = "查询任务包中可添加的实体")
    @WithPrivilege
    public JsonListResponseBody<ProjectNode> projectNodeEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        TaskPackageProjectNodeEntitySearchDTO taskPackageProjectNodeEntitySearchDTO
    ) {
//        if (WBSEntityType.SUB_SYSTEM.equals(taskPackageProjectNodeEntitySearchDTO.getEntityCategoryType()) ||
//            WBSEntityType.SYSTEM.equals(taskPackageProjectNodeEntitySearchDTO.getEntityCategoryType())) {
//
//            taskPackageProjectNodeEntitySearchDTO.setHierarchyType(HierarchyType.SUB_SYSTEM);
//        }
        return new JsonListResponseBody<>(
            taskPackageService.projectNodeEntities(orgId, projectId, taskPackageProjectNodeEntitySearchDTO)
        );
    }

}
