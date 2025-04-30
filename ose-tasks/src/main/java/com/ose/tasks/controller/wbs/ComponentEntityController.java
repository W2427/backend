package com.ose.tasks.controller.wbs;
import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.wbs.ComponentEntityAPI;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.domain.model.service.wbs.piping.ComponentEntityInterface;
import com.ose.tasks.dto.ComponentEntryCriteriaDTO;
import com.ose.tasks.dto.ComponentEntryInsertDTO;
import com.ose.tasks.dto.ComponentEntryUpdateDTO;
import com.ose.tasks.dto.wbs.ParentsPutDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.entity.ComponentEntity;
import com.ose.tasks.entity.wbs.entity.ComponentEntityBase;
import com.ose.tasks.entity.wbs.entity.ComponentHierarchyInfoEntity;
import com.ose.tasks.vo.wbs.WBSEntityType;
import com.ose.util.BeanUtils;
import com.ose.util.CollectionUtils;
import com.ose.util.StringUtils;
import io.swagger.annotations.Api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.Tuple;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Api(description = "管件实体管理接口")
@RestController
public class ComponentEntityController extends AbstractWBSEntityController implements ComponentEntityAPI {


    private ComponentEntityInterface componentEntityService;


    private ProjectInterface projectService;


    private HierarchyInterface hierarchyService;

    private static final String FLANGE = "FLANGE";



    private final PlanInterface planService;

    /**
     * 构造方法。
     */
    @Autowired
    public ComponentEntityController(
        ComponentEntityInterface componentEntityService,
        ProjectInterface projectService,
        HierarchyInterface hierarchyService,
        PlanInterface planService
    ) {
        this.componentEntityService = componentEntityService;
        this.projectService = projectService;
        this.hierarchyService = hierarchyService;
        this.planService = planService;
    }

    @Override
    @Operation(summary = "查询管件实体")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/component-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<? extends ComponentEntityBase> searchComponentEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        ComponentEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            componentEntityService.search(orgId, projectId, criteriaDTO, pageDTO)
        );
    }

    @Override
    @Operation(summary = "取得管件实体详细信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/component-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<? extends ComponentEntityBase> getComponentEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId
    ) {
        return new JsonObjectResponseBody<>(
            componentEntityService.get(orgId, projectId, entityId)
        );
    }

    @Override
    @Operation(summary = "删除管件实体")
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/component-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody deleteComponentEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version
    ) {

        Project project = projectService.get(orgId, projectId, version);


/*        if (wbsEntityService.isInWorkFlow(entityId, projectId, orgId)) {
            throw new BusinessError("",
                "business-error: COMPONENT is ALREADY in work flow, CAN'T delete.");
        }*/

        componentEntityService.delete(
            getContext().getOperator(),
            orgId,
            project,
            entityId
        );








        return new JsonResponseBody();
    }

    @Override
    @Operation(summary = "插入管件实体")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/component-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody addComponentEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Parameter(description = "管件实体信息") ComponentEntryInsertDTO componentEntryInsertDTO
    ) {

        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId);

        if (StringUtils.isEmpty(componentEntryInsertDTO.getParentNodeNo())) {
            throw new BusinessError("parent node no is NOT specified.");
        }

        Map<String, String> entityTypes = componentEntityService.setEntityTypeByRules(orgId,
            projectId,
            componentEntryInsertDTO.getParentNodeNo(),
            componentEntryInsertDTO.getShortCode());

        String componentEntityType = entityTypes.get("subEntityType");
        String entityBusinessType = entityTypes.get("entityBusinessType");

        String nodeNo = componentEntryInsertDTO.getParentNodeNo()
            + componentEntityType
            + (StringUtils.isEmpty(componentEntryInsertDTO.getNpsText()) ? "" : componentEntryInsertDTO.getNpsText())
            + (StringUtils.isEmpty(componentEntryInsertDTO.getMaterialCode()) ? "" : componentEntryInsertDTO.getMaterialCode());

        if (componentEntityService.existsByEntityNo(nodeNo, projectId)) {
            throw new BusinessError("", "business-error: Component ALREADY EXISTS.");
        }

        ComponentEntity componentEntity = new ComponentEntity(project);
        BeanUtils.copyProperties(componentEntryInsertDTO, componentEntity);

        componentEntity.setComponentEntityType(componentEntityType);

        if (FLANGE.equals(componentEntityType)) {
            componentEntity.setFlangeManagement(true);
        }

        componentEntity.setEntityBusinessType(entityBusinessType);

        componentEntity.setNo(nodeNo);

        componentEntity.setDisplayName("");

        componentEntity.setThickness(componentEntryInsertDTO.getThickness());

        componentEntity.setRemarks(componentEntryInsertDTO.getRemarks());


        componentEntity.setNps(componentEntryInsertDTO.getNpsText());

        componentEntityService.setParentInfo(componentEntryInsertDTO.getParentNodeNo(),
            componentEntity,
            projectId,
            orgId,
            operator);

        componentEntityService.insert(operator, orgId, projectId, componentEntity);


        componentEntityService.setIsoIdsAndWbs(projectId, componentEntity.getId());



        return new JsonResponseBody(getContext());
    }

    @Override
    @Operation(summary = "更新管件实体")
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/component-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonObjectResponseBody<ComponentEntityBase> updateComponentEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody @Parameter(description = "管件实体信息") ComponentEntryUpdateDTO componentEntryUpdateDTO
    ) {

        final OperatorDTO operator = getContext().getOperator();
        projectService.get(orgId, projectId, version);

        ComponentEntityBase componentEntityBase = componentEntityService.get(orgId, projectId, entityId);
        ComponentEntity componentEntity = BeanUtils.copyProperties(componentEntityBase, new ComponentEntity());

        BeanUtils.copyProperties(componentEntryUpdateDTO, componentEntity);

        Map<String, String> entityTypes = componentEntityService.setEntityTypeByRules(
            orgId,
            projectId,
            componentEntryUpdateDTO.getParentNodeNo(),
            componentEntryUpdateDTO.getShortCode());

        String componentEntityType = entityTypes.get("subEntityType");
        String entityBusinessType = entityTypes.get("entityBusinessType");

        componentEntity.setComponentEntityType(componentEntityType);

        if (FLANGE.equals(componentEntityType)) {
            componentEntity.setFlangeManagement(true);
        }

        componentEntity.setEntityBusinessType(entityBusinessType);

        String nodeNo = componentEntryUpdateDTO.getParentNodeNo()
            + componentEntityType
            + (StringUtils.isEmpty(componentEntryUpdateDTO.getNpsText()) ? "" : componentEntryUpdateDTO.getNpsText())
            + (StringUtils.isEmpty(componentEntryUpdateDTO.getMaterialCode()) ? "" : componentEntryUpdateDTO.getMaterialCode());

        componentEntity.setNo(nodeNo);

        componentEntity.setThickness(componentEntryUpdateDTO.getThickness());

        componentEntity.setRemarks(componentEntryUpdateDTO.getRemarks());


        componentEntity.setNps(componentEntryUpdateDTO.getNpsText());

        return new JsonObjectResponseBody<>(
            getContext(),
            componentEntityService.update(operator, orgId, projectId, componentEntity)
        );
    }

    /**
     * 更新父级。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @Override
    @Operation(summary = "更新父级")
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/component-entities/{entityId}/hierarchy-parents",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody moveComponent(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam("version") long version,
        @RequestBody ParentsPutDTO parentsPutDTO
    ) {
        Project project = projectService.get(orgId, projectId, version);


        if (parentsPutDTO.getParentSpoolHierarchyId() != null && parentsPutDTO.getParentSpoolHierarchyId() != 0L) {
            validateParentHierarchyNode(
                hierarchyService.get(orgId, projectId, parentsPutDTO.getParentHierarchyIdOnPiping()),
                WBSEntityType.SPOOL.name()
            );
        } else if (parentsPutDTO.getParentIsoHierarchyId() != null && parentsPutDTO.getParentIsoHierarchyId() != 0L) {
            validateParentHierarchyNode(
                hierarchyService.get(orgId, projectId, parentsPutDTO.getParentIsoHierarchyId()),
                WBSEntityType.ISO.name()
            );
        }


        ComponentHierarchyInfoEntity componentHierarchyInfoEntity
            = (ComponentHierarchyInfoEntity) componentEntityService.get(orgId,
            projectId,
            entityId);

//        if (parentsPutDTO.getParentSpoolHierarchyId() != null && parentsPutDTO.getParentSpoolHierarchyId() != 0L
//            && parentsPutDTO.getParentSpoolHierarchyId().equals(componentHierarchyInfoEntity.getParentHierarchyIdOnPiping())) {
//
//
//            List<Tuple> hierarchyNodeList = hierarchyService.
//                getHierarchyNodeByEntityId(orgId, projectId, parentsPutDTO.getParentSpoolHierarchyId());
//            if (!CollectionUtils.isEmpty(hierarchyNodeList)) {
//
//
//
//                for (Tuple hierarchyNode : hierarchyNodeList) {
//                    hierarchyService.moveTo(
//                        getContext().getOperator(),
//                        projectService.get(orgId, projectId, version),
//                        WBSEntityType.COMPONENT.name(),
//                        entityId,
//                        WBSEntityType.SPOOL.name(),
//                        (Long) hierarchyNode.get("id")
//                    );
//                }
//            }
//        } else if (parentsPutDTO.getParentIsoHierarchyId() != null && parentsPutDTO.getParentIsoHierarchyId() != 0L
//            && parentsPutDTO.getParentIsoHierarchyId().equals(componentHierarchyInfoEntity.getParentHierarchyIdOnPiping())) {
//
//            List<Tuple> hierarchyNodeList = hierarchyService.
//                getHierarchyNodeByEntityId(orgId, projectId, parentsPutDTO.getParentIsoHierarchyId());
//            if (!CollectionUtils.isEmpty(hierarchyNodeList)) {
//                Project proj = projectService.get(orgId, projectId);
//                OperatorDTO operator = getContext().getOperator();
//
//                for (Tuple hierarchyNode : hierarchyNodeList) {
//                    hierarchyService.moveTo(
//                        getContext().getOperator(),
//                        projectService.get(orgId, projectId, version),
//                        WBSEntityType.COMPONENT.name(),
//                        entityId,
//                        WBSEntityType.ISO.name(),
//                        (Long) hierarchyNode.get("id")
//                    );
//                }
//            }
//        } else if ((parentsPutDTO.getParentIsoHierarchyId() == null || parentsPutDTO.getParentIsoHierarchyId() == 0L)
//            && (parentsPutDTO.getParentSpoolHierarchyId() == null || parentsPutDTO.getParentSpoolHierarchyId() == 0L)) {
//
//            hierarchyService.deleteAllHierarchyInfoByEntityId(getContext().getOperator(),
//                project,
//                orgId,
//                entityId);
//        }


        componentEntityService.setIsoIdsAndWbs(projectId, entityId);



        return new JsonResponseBody();
    }

    /**
     * 取得管件图纸信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @Operation(summary = "取得图纸信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/component-entities/{entityId}/drawing-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    public JsonResponseBody getDrawingInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                           @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                           @PathVariable @Parameter(description = "实体 ID") Long entityId) {

        return new JsonListResponseBody<>(
            componentEntityService.getDrawingInfo(
                orgId,
                projectId,
                entityId,
                null
            )
        );
    }

    /**
     * 取得管件材料信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/component-entities/{entityId}/material-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(summary = "取得管件材料信息")
    @WithPrivilege()
    @Override
    public JsonResponseBody getMaterialInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                            @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                            @PathVariable @Parameter(description = "实体 ID") Long entityId) {
        return new JsonListResponseBody<>(
            componentEntityService.getMaterialInfo(entityId, orgId, projectId));
    }

    /**
     * 按条件下载管件实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/component-entities/download"
    )
    @Operation(summary = "按条件下载管件实体列表")
    @WithPrivilege
    @Override
    public synchronized void downloadComponentEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        ComponentEntryCriteriaDTO criteriaDTO) throws IOException {

        final OperatorDTO operator = getContext().getOperator();
        File excel = componentEntityService.saveDownloadFile(orgId, projectId, criteriaDTO, operator.getId());

        HttpServletResponse response = getContext().getResponse();
        try {

            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-project-entities-component.xlsx\""
            );

            IOUtils.copy(
                new FileInputStream(excel), response.getOutputStream()
            );

        } catch (FileNotFoundException e) {
            throw new NotFoundError();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessError("", "文件编码不支持");
        } catch (IOException e) {
            throw new BusinessError("", "下载文件出错");
        }

        response.flushBuffer();
    }
}
