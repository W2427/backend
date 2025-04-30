package com.ose.tasks.api;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.HierarchyWBSDTO;
import com.ose.tasks.dto.ModuleProcessDefinitionPutDTO;
import com.ose.tasks.dto.ProjectInfoDTO;
import com.ose.tasks.dto.ProjectModifyDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.process.ModuleProcessDefinition;
import com.ose.tasks.entity.process.ModuleProcessDefinitionBasic;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 项目管理接口。
 */
public interface ProjectAPI {

    /**
     * 创建项目。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<Project> create(
        @PathVariable("orgId") Long orgId,
        @RequestBody ProjectModifyDTO projectDTO
    );

    /**
     * 更新项目。
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<Project> update(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("version") long version,
        @RequestBody ProjectModifyDTO projectDTO
    );

    /**
     * 删除项目。
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("version") long version
    );


    /**
     * 查询项目。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Project> list(
        @PathVariable("orgId") Long orgId,
        PageDTO pageDTO
    );

    /**
     * 取得项目详细信息。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<Project> get(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    /**
     * 设置模块工作流定义。
     */
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/module-process-definitions/{funcPart}",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ModuleProcessDefinition> setModuleProcessDefinition(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("funcPart") String funcPart,
        @RequestParam("version") long version,
        @RequestBody ModuleProcessDefinitionPutDTO moduleProcessDefinitionPutDTO
    ) throws FileNotFoundException;

    /**
     * 取得项目的模块工作流定义列表。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/module-process-definitions",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ModuleProcessDefinitionBasic> listModuleProcessDefinitions(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    /**
     * 取得项目的模块工作流定义部署历史列表。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/module-process-definitions/{moduleType}/history",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ModuleProcessDefinitionBasic> getModuleProcessDefinitionHistory(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("moduleType") String moduleType
    );


    /**
     * 删除项目的模块工作流定义。
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/module-process-definitions/{moduleType}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteModuleProcessDefinition(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("moduleType") String moduleType,
        @RequestParam(required = false, name = "disciplineCode", defaultValue = "PIPING") String disciplineCode,
        @RequestParam("version") long version
    );

    /**
     * 关闭项目。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/close",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody close(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("version") long version
    );

    /**
     * 重新开启项目。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/reopen",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody reopen(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("version") long version
    );

    /**
     * 克隆项目。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/clone",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody clone(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("projectName") String projectName
    );

    /**
     * 删除项目。
     *
     * @param orgId
     * @param projectId
     * @return
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/deleteProject",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteProject(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    /**
     * 导出projects
     **/
    @RequestMapping(
        method = GET,
        value = "/projects/download"
    )
    void downloadProjects() throws IOException;

    /**
     * 禁止填写加班工时
     * @param projectId
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/update-project-hour-status",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updateProjectHourStatus(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("version") long version,
        @RequestBody ProjectModifyDTO projectDTO
    );

    /**
     * 允许填写加班工时
     * @param projectId
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/enable",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody enable(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("version") long version
    );

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
    JsonObjectResponseBody<HierarchyWBSDTO> getHierarchyProjectInfo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("key") String key
    );

    /**
     * 保存项目参数。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/project-hierarchy-info",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody saveProjectHierarchyInfo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody ProjectInfoDTO projectInfoDTO
    );

    /**
     * 更新项目模板。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/update-hierarchy-template",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<Project> updateHierarchyTemplate(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody ProjectModifyDTO projectDTO
    );

    /**
     * 按组织架构更新加班时间
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/approve-overtime",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<Project> approveOvertime(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody ProjectModifyDTO projectDTO
    );
}
