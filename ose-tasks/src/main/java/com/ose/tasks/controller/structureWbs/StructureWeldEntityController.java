package com.ose.tasks.controller.structureWbs;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.wbsStructure.StructureWeldEntityAPI;
import com.ose.tasks.controller.wbs.AbstractWBSEntityController;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.wbs.structure.StructureWeldEntityInterface;
import com.ose.tasks.domain.model.service.wbs.WBSEntityInterface;
import com.ose.tasks.dto.structureWbs.StructureWeldEntryCriteriaDTO;
import com.ose.tasks.dto.structureWbs.StructureWeldEntryInsertDTO;
import com.ose.tasks.dto.structureWbs.StructureWeldEntryUpdateDTO;
import com.ose.tasks.dto.wbs.ParentsPutDTO;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.structureEntity.StructureWeldEntity;
import com.ose.tasks.entity.wbs.structureEntity.StructureWeldEntityBase;
import com.ose.tasks.entity.wbs.structureEntity.StructureWeldHierarchyInfoEntity;
import com.ose.util.BeanUtils;
import com.ose.util.LongUtils;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Api(description = "结构焊口实体管理接口")
@RestController
public class StructureWeldEntityController extends AbstractWBSEntityController implements StructureWeldEntityAPI {


    private StructureWeldEntityInterface structureWeldEntityService;


    private ProjectInterface projectService;


    private WBSEntityInterface wbsEntityService;


    private HierarchyInterface hierarchyService;


    /**
     * 构造方法。
     */
    @Autowired
    public StructureWeldEntityController(
        StructureWeldEntityInterface structureWeldEntityService,
        ProjectInterface projectService,
        HierarchyInterface hierarchyService,
        WBSEntityInterface wbsEntityService
    ) {
        this.structureWeldEntityService = structureWeldEntityService;
        this.projectService = projectService;
        this.hierarchyService = hierarchyService;
        this.wbsEntityService = wbsEntityService;
    }

    @Override
    @Operation(
        summary = "查询焊口实体",
        description = "查询焊口实体")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/structure-weld-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<? extends StructureWeldEntityBase> searchStructureWeldEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        StructureWeldEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            structureWeldEntityService.search(orgId, projectId, criteriaDTO, pageDTO)
        );
    }

    @Override
    @Operation(
        summary = "取得焊口实体详细信息",
        description = "取得焊口实体详细信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/structure-weld-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<? extends StructureWeldEntityBase> getStructureWeldEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId
    ) {
        return new JsonObjectResponseBody<>(
            structureWeldEntityService.get(orgId, projectId, entityId)
        );
    }

    @Override
    @Operation(
        summary = "删除焊口实体",
        description = "删除焊口实体")
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/structure-weld-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody deleteStructureWeldEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version
    ) {


        Project project = projectService.get(orgId, projectId, version);


        if (wbsEntityService.isInWorkFlow(entityId, projectId, orgId)) {
            throw new BusinessError("",
                "business-error: StructureWeld is ALREADY in work flow, CAN'T delete.");
        }


        structureWeldEntityService.delete(
            getContext().getOperator(),
            orgId,
            project,
            entityId
        );
        return new JsonResponseBody(getContext());
    }

    @Override
    @Operation(
        summary = "插入焊口实体",
        description = "插入焊口实体")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/structure-weld-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody addStructureWeldEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Parameter(description = "结构焊口实体信息") StructureWeldEntryInsertDTO structureWeldEntryInsertDTO
    ) {

        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId);


        StructureWeldEntity structureWeldEntity = new StructureWeldEntity(project);


        BeanUtils.copyProperties(structureWeldEntryInsertDTO, structureWeldEntity);

        structureWeldEntityService.insert(operator, orgId, projectId, structureWeldEntity);

        return new JsonResponseBody(getContext());
    }

    @Override
    @Operation(
        summary = "更新焊口实体",
        description = "更新焊口实体")
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/structure-weld-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody updateStructureWeldEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody @Parameter(description = "焊口实体信息") StructureWeldEntryUpdateDTO structureWeldEntryUpdateDTO
    ) {

        final OperatorDTO operator = getContext().getOperator();
        projectService.get(orgId, projectId, version);


        StructureWeldEntityBase wpMiscEntity = structureWeldEntityService.get(orgId, projectId, entityId);


        if (!wpMiscEntity.getNo().equals(structureWeldEntryUpdateDTO.getNo()) && structureWeldEntityService.existsByEntityNo(structureWeldEntryUpdateDTO.getNo(), projectId)) {
            throw new BusinessError("", "business-error: Structure_weld  ALREADY EXISTS.");
        }
        StructureWeldEntity wpMiscEntityCommon = BeanUtils.copyProperties(wpMiscEntity, new StructureWeldEntity());
        BeanUtils.copyProperties(structureWeldEntryUpdateDTO, wpMiscEntityCommon);


        StructureWeldEntityBase entityResult = structureWeldEntityService.update(operator, orgId, projectId, wpMiscEntityCommon);
        return new JsonObjectResponseBody<>(getContext(), entityResult);
    }

    @Override
    @Operation(
        summary = "更新父级信息",
        description = "更新父级信息")
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/structure-weld-entities/{entityId}/hierarchy-parents",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody moveStructureWeld(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody ParentsPutDTO parentsPutDTO
    ) {


        if (!LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnStructure())) {

            HierarchyNode structureParent = hierarchyService
                .get(orgId, projectId, parentsPutDTO.getParentHierarchyIdOnStructure());

            StructureWeldHierarchyInfoEntity structureWeldHierarchyInfoEntity =
                (StructureWeldHierarchyInfoEntity) structureWeldEntityService.get(orgId, projectId, entityId);
            hierarchyService.moveTo(
                getContext().getOperator(),
                projectService.get(orgId, projectId),
                structureWeldHierarchyInfoEntity.getEntityType(),
                entityId,
                structureWeldHierarchyInfoEntity.getEntityType(),
                parentsPutDTO.getParentHierarchyIdOnStructure()
            );

        }
        return new JsonResponseBody();
    }

    /**
     * 取得管件图纸信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @Operation(
        summary = "取得焊口图纸信息",
        description = "取得焊口图纸信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/structure-weld-entities/{entityId}/drawing-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege()
    public JsonResponseBody getDrawingInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                           @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                           @PathVariable @Parameter(description = "实体 ID") Long entityId) {

        return new JsonListResponseBody<>(
            structureWeldEntityService.getDrawingInfo(
                orgId,
                projectId,
                entityId,
                null
            )
        );
    }

    /**
     * 取得焊口材料信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/structure-weld-entities/{entityId}/material-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "取得焊口材料信息",
        description = "取得焊口材料信息")
    @WithPrivilege()
    @Override
    public JsonResponseBody getMaterialInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                            @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                            @PathVariable @Parameter(description = "实体 ID") Long entityId) {
        return new JsonListResponseBody<>(
            structureWeldEntityService.getMaterialInfo(entityId, orgId, projectId)
        );
    }

    /**
     * 按条件下载焊口实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/structure-weld-entities/download"
    )
    @Operation(
        summary = "按条件下载焊口实体列表",
        description = "按条件下载焊口实体列表")
    @WithPrivilege
    @Override
    public synchronized void downloadStructureWeldEntities(@PathVariable("orgId") Long orgId,
                                              @PathVariable("projectId") Long projectId,
                                              StructureWeldEntryCriteriaDTO criteriaDTO) throws IOException {



        final OperatorDTO operator = getContext().getOperator();
        File excel = structureWeldEntityService.saveDownloadFile(orgId, projectId, criteriaDTO, operator.getId());

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-project-entities-structure-weld.xlsx\""
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


}
