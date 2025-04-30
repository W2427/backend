package com.ose.tasks.controller.taskpackage;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.taskpackage.TaskPackageCategoryAPI;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.taskpackage.TaskPackageCategoryInterface;
import com.ose.tasks.dto.taskpackage.*;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.taskpackage.TaskPackageCategory;
import com.ose.tasks.entity.taskpackage.TaskPackageCategoryProcessRelation;
import com.ose.tasks.entity.taskpackage.TaskPackageCategoryProcessRelationBasic;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import static com.ose.constant.HttpResponseHeaders.DATA_REVISION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Tag(name = "任务包类型接口")
@RestController
public class TaskPackageCategoryController extends BaseController implements TaskPackageCategoryAPI {


    private final ProjectInterface projectService;


    private final TaskPackageCategoryInterface taskPackageCategoryService;

    /**
     * 构造方法。
     */
    @Autowired
    public TaskPackageCategoryController(
        ProjectInterface projectService,
        TaskPackageCategoryInterface taskPackageCategoryService
    ) {
        this.projectService = projectService;
        this.taskPackageCategoryService = taskPackageCategoryService;
    }



    @Override
    @Operation(description = "创建任务包类型")
    @WithPrivilege
    public JsonObjectResponseBody<TaskPackageCategory> create(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Valid TaskPackageCategoryCreateDTO taskPackageCategoryDTO
    ) {

//        projectService.get(orgId, projectId);

        return new JsonObjectResponseBody<>(
            taskPackageCategoryService.create(
                getContext().getOperator(),
                orgId,
                projectId,
                taskPackageCategoryDTO
            )
        );
    }

    @Override
    @Operation(description = "查询任务包类型")
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<TaskPackageCategory> search(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        TaskPackageCategoryCriteriaDTO criteriaDTO
    ) {
        return new JsonListResponseBody<>(
            taskPackageCategoryService.search(
                orgId,
                projectId,
                criteriaDTO,
                criteriaDTO.toPageable()
            )
        );
    }

    @Override
    @Operation(description = "取得任务包类型详细信息")
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<TaskPackageCategory> get(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包类型 ID") Long categoryId
    ) {

        TaskPackageCategory taskPackageCategory = taskPackageCategoryService
            .get(orgId, projectId, categoryId);

        if (taskPackageCategory == null) {
            throw new NotFoundError();
        }

        return new JsonObjectResponseBody<>(taskPackageCategory);
    }

    @Override
    @Operation(description = "更新任务包类型信息")
    @WithPrivilege
    public JsonResponseBody update(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包类型 ID") Long categoryId,
        @RequestParam @Parameter(description = "任务包类型信息更新版本号") Long version,
        @RequestBody @Valid TaskPackageCategoryUpdateDTO taskPackageCategoryDTO
    ) {

        getResponse().setHeader(
            DATA_REVISION,
            taskPackageCategoryService
                .update(
                    getContext().getOperator(),
                    orgId,
                    projectId,
                    categoryId,
                    version,
                    taskPackageCategoryDTO
                )
                .getVersion()
                .toString()
        );

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "删除任务包类型")
    @WithPrivilege
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包类型 ID") Long categoryId,
        @RequestParam @Parameter(description = "任务包类型信息更新版本号") Long version
    ) {

        taskPackageCategoryService.delete(
            getContext().getOperator(),
            orgId,
            projectId,
            categoryId,
            version
        );

        return new JsonResponseBody();
    }



    @Override
    @Operation(description = "添加任务包类型关联的工序")
    @WithPrivilege
    public JsonObjectResponseBody<TaskPackageCategoryProcessRelationBasic> addProcess(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包类型 ID") Long categoryId,
        @RequestBody TaskPackageCategoryProcessRelationCreateDTO relationDTO
    ) {
        return new JsonObjectResponseBody<>(
            taskPackageCategoryService.addProcess(
                getContext().getOperator(),
                orgId,
                projectId,
                categoryId,
                relationDTO
            )
        );
    }

    @Override
    @Operation(description = "查询任务包类型关联的工序")
    @WithPrivilege
    public JsonListResponseBody<TaskPackageCategoryProcessRelation> processes(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包类型 ID") Long categoryId,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            taskPackageCategoryService.getProcesses(
                orgId,
                projectId,
                categoryId,
                pageDTO.toPageable()
            )
        );
    }

    @Override
    @Operation(description = "删除任务包类型关联的工序")
    @WithPrivilege
    public JsonResponseBody deleteProcesses(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包类型 ID") Long categoryId,
        @PathVariable @Parameter(description = "工序 ID 或任务包类型-工序关系 ID") Long processId
    ) {
        taskPackageCategoryService.deleteProcess(orgId, projectId, categoryId, processId);
        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "取得所有已添加到指定类型任务包的实体的 ID")
    @WithPrivilege
    public JsonObjectResponseBody<TaskPackageCategoryEntityDTO> entityIDs(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包类型 ID") Long categoryId
    ) {
        return new JsonObjectResponseBody<>(new TaskPackageCategoryEntityDTO(
            taskPackageCategoryService.entityIDs(orgId, projectId, categoryId)
        ));
    }

    @Override
    @Operation(description = "取得任务包类型对应的实体类型")
    @WithPrivilege
    public JsonListResponseBody<BpmEntitySubType> entityTypes(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包类型 ID") Long categoryId
    ) {
        return new JsonListResponseBody<>(
            taskPackageCategoryService.entityTypes(orgId, projectId, categoryId)
        );
    }

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-package-categories/type-enum",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    public JsonObjectResponseBody<TaskPackageTypeEnumDTO> getTypeEnums(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    ) {
        return new JsonObjectResponseBody<>(
            taskPackageCategoryService.getTypeEnums(orgId, projectId)
        );
    }

}
