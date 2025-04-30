package com.ose.tasks.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.auth.api.OrganizationFeignAPI;
import com.ose.auth.entity.Organization;
import com.ose.controller.BaseController;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.ProjectAPI;
import com.ose.tasks.domain.model.service.ModuleProcessInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.plan.WBSSearchInterface;
import com.ose.tasks.dto.HierarchyWBSDTO;
import com.ose.tasks.dto.ModuleProcessDefinitionPutDTO;
import com.ose.tasks.dto.ProjectInfoDTO;
import com.ose.tasks.dto.ProjectModifyDTO;
import com.ose.tasks.dto.process.EntityProcessRelationsDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.process.ModuleProcessDefinition;
import com.ose.tasks.entity.process.ModuleProcessDefinitionBasic;


import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "项目管理接口")
@RestController
public class ProjectController extends BaseController implements ProjectAPI {


    private ProjectInterface projectService;


    private WBSSearchInterface wbsSearchService;


    private ModuleProcessInterface moduleProcessService;


    private UploadFeignAPI uploadFeignAPI;


    private final OrganizationFeignAPI organizationFeignAPI;

    /**
     * 构造方法。
     */
    @Autowired
    public ProjectController(
        ProjectInterface projectService,
        WBSSearchInterface wbsSearchService,
        ModuleProcessInterface moduleProcessService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UploadFeignAPI uploadFeignAPI,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            OrganizationFeignAPI organizationFeignAPI
    ) {
        this.projectService = projectService;
        this.wbsSearchService = wbsSearchService;
        this.moduleProcessService = moduleProcessService;
        this.uploadFeignAPI = uploadFeignAPI;
        this.organizationFeignAPI = organizationFeignAPI;
    }

    @Override
    @Operation(description = "创建项目")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    @WithPrivilege
    public JsonObjectResponseBody<Project> create(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @RequestBody ProjectModifyDTO projectDTO
    ) {

        Organization org = organizationFeignAPI.details(orgId, null).getData();

        if (org == null) {
            throw new NotFoundError();
        }

        Long companyId = org.getCompanyId();

        return new JsonObjectResponseBody<>(
            projectService.create(
                getContext().getOperator(),
                companyId,
                orgId,
                projectDTO
            )
        );
    }

    @Override
    @Operation(description = "更新项目")
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonObjectResponseBody<Project> update(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody ProjectModifyDTO projectDTO
    ) {
        return new JsonObjectResponseBody<>(
            projectService.update(
                getContext().getOperator(),
                orgId,
                projectId,
                projectDTO,
                version
            )
        );
    }

    @Override
    @Operation(description = "删除项目")
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestParam @Parameter(description = "项目更新版本号") long version
    ) {

        projectService.delete(
            getContext().getOperator(),
            orgId,
            projectId,
            version
        );

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "查询项目")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<Project> list(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            getContext(),
            projectService.search(orgId, pageDTO)
        );
    }

    @Override
    @Operation(description = "取得项目详细信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<Project> get(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId
    ) {
        return new JsonObjectResponseBody<>(
            getContext(),
            projectService.get(orgId, projectId)
        );
    }

    @Operation(summary = "设置模块工作流定义")
    @Override
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/module-process-definitions/{funcPart}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonObjectResponseBody<ModuleProcessDefinition> setModuleProcessDefinition(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable("funcPart") String funcPart,
        @RequestParam(defaultValue = "0") @Parameter(description = "模块工作流部署更新版本号（存在部署中的工作流时必须）") long version,
        @RequestBody ModuleProcessDefinitionPutDTO moduleProcessDefinitionPutDTO
    ) {


        final EntityProcessRelationsDTO entityProcessRelationsDTO = wbsSearchService.getProcessEntityTypeRelations(orgId, projectId);


        ModuleProcessDefinition moduleProcessDefinition = moduleProcessService.deploy(
            getContext().getOperator(),
            orgId,
            projectId,
            version,
            moduleProcessDefinitionPutDTO.getFilename(),
            funcPart,
            entityProcessRelationsDTO,
            moduleProcessDefinitionPutDTO.getBpmnName()
        );


        return new JsonObjectResponseBody<>(moduleProcessDefinition);
    }

    @Operation(description = "取得项目的模块工作流定义列表")
    @Override
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/module-process-definitions",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonListResponseBody<ModuleProcessDefinitionBasic> listModuleProcessDefinitions(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId
    ) {
        return new JsonListResponseBody<>(moduleProcessService.list(orgId, projectId));
    }

    @Operation(description = "取得项目的指定类型模块的工作流定义部署的历史列表")
    @Override
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/module-process-definitions/{moduleType}/history",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonListResponseBody<ModuleProcessDefinitionBasic> getModuleProcessDefinitionHistory(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "模块类型") String moduleType
    ) {
        return new JsonListResponseBody<>(moduleProcessService.history(orgId, projectId, moduleType));
    }

    @Operation(description = "删除项目的模块工作流定义")
    @Override
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/module-process-definitions/{funcPart}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody deleteModuleProcessDefinition(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("funcPart") String funcPart,
        @RequestParam @Parameter(description = "模块流程的功能块") String funcPartStr,
        @RequestParam @Parameter(description = "模块工作流部署更新版本号") long version
    ) {
//        String funcPart = funcPartStr;
        if (funcPart == null) {
            throw new BusinessError("No func Part is assigned");
        }
        moduleProcessService
            .delete(getContext().getOperator(), orgId, projectId, funcPart, version);
        return new JsonResponseBody();
    }


    @Override
    @Operation(description = "关闭项目")
    @WithPrivilege
    public JsonResponseBody close(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestParam @Parameter(description = "项目更新版本号") long version
    ) {
        projectService.close(getContext().getOperator(), orgId, projectId, version);
        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "重新开启项目")
    @WithPrivilege
    public JsonResponseBody reopen(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestParam @Parameter(description = "项目更新版本号") long version
    ) {
        projectService.reopen(getContext().getOperator(), orgId, projectId, version);
        return new JsonResponseBody();
    }


    /**
     * 克隆项目。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/clone",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    public JsonResponseBody clone(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("projectName") String projectName
    ) {
        projectService.cloneProject(orgId, projectId, projectName);
        return new JsonResponseBody();
    }


    @Override
    @Operation(
        summary = "删除项目",
        description = "删除项目"
    )
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/deleteProject",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteProject(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId
    ) {
        projectService.deleteProject(orgId, projectId);
        return new JsonResponseBody();
    }

    /**
     * 导出projects
     *
     */
    @RequestMapping(
        method = GET,
        value = "/projects/download"
    )
    @Operation(description = "导出projects")
    @WithPrivilege
    @Override
    public void downloadProjects() throws IOException {

        final OperatorDTO operator = getContext().getOperator();
        File excel = projectService.saveDownloadFile(operator.getId());

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-projects.xlsx\""
            );

            IOUtils.copy(
                new FileInputStream(excel), response.getOutputStream()
            );

        } catch (FileNotFoundException e) {
            throw new NotFoundError();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessError("文件编码不支持");
        } catch (IOException e) {
            throw new BusinessError("下载文件出错");
        }

        response.flushBuffer();
    }


    @Override
    @Operation(description = "disable overtime")
    @WithPrivilege
    public JsonResponseBody updateProjectHourStatus(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody ProjectModifyDTO projectDTO
    ) {
        projectService.updateProjectHourStatus(getContext().getOperator(), orgId, projectId, version, projectDTO);
        return new JsonResponseBody();
    }


    @Override
    @Operation(description = "enable overtime")
    @WithPrivilege
    public JsonResponseBody enable(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestParam @Parameter(description = "项目更新版本号") long version
    ) {
        projectService.enableOvertime(getContext().getOperator(), orgId, projectId, version);
        return new JsonResponseBody();
    }

    /**
     * 获取项目的树形辅助信息 Project_information。
     *
     * @param orgId
     * @param projectId
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/project-hierarchy-info/{key}",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    public JsonObjectResponseBody<HierarchyWBSDTO> getHierarchyProjectInfo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("key") String key
    ){
        HierarchyWBSDTO responseData = new HierarchyWBSDTO();

        //层次类型
        String hierarchyType;

        responseData.setChildren(
            projectService.getHierarchyInfo(
                orgId, projectId, key
            )
        );

        return new JsonObjectResponseBody<>(responseData);
    }


    /**
     * 保存项目参数。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/project-hierarchy-info",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    public JsonResponseBody saveProjectHierarchyInfo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody ProjectInfoDTO projectInfoDTO
    ){
        projectService.saveWbsSetting(orgId, projectId, projectInfoDTO);
        return new JsonResponseBody();

    }

    @Override
    @Operation(summary = "更新项目层级模板", description = "更新项目层级模板")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/update-hierarchy-template",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonObjectResponseBody<Project> updateHierarchyTemplate(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody ProjectModifyDTO projectDTO
    ) {
        return new JsonObjectResponseBody<>(
            projectService.updateHierarchyTemplate(
                getContext().getOperator(),
                orgId,
                projectId,
                projectDTO
            )
        );
    }

    @Override
    @Operation(summary = "按组织架构更新加班时间")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/approve-overtime",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonObjectResponseBody<Project> approveOvertime(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody ProjectModifyDTO projectDTO
    ) {
        return new JsonObjectResponseBody<>(
            projectService.approveOvertime(
                getContext().getOperator(),
                orgId,
                projectId,
                projectDTO
            )
        );
    }
}
