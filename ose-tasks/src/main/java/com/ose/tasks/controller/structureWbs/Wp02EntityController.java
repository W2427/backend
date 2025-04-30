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
import com.ose.tasks.api.wbsStructure.Wp02EntityAPI;
import com.ose.tasks.controller.wbs.AbstractWBSEntityController;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.domain.model.service.wbs.WBSEntityInterface;
import com.ose.tasks.dto.structureWbs.Wp02EntryCriteriaDTO;
import com.ose.tasks.dto.structureWbs.Wp02EntryInsertDTO;
import com.ose.tasks.dto.structureWbs.Wp02EntryUpdateDTO;
import com.ose.tasks.dto.wbs.ParentsPutDTO;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.structureEntity.Wp02Entity;
import com.ose.tasks.entity.wbs.structureEntity.Wp02EntityBase;
import com.ose.tasks.entity.wbs.structureEntity.Wp02HierarchyInfoEntity;
import com.ose.tasks.vo.wbs.HierarchyType;
import com.ose.util.BeanUtils;
import io.swagger.annotations.Api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import jakarta.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Api(description = "结构分段实体管理接口")
@RestController
public class Wp02EntityController extends AbstractWBSEntityController implements Wp02EntityAPI {


    private BaseWBSEntityInterface<Wp02EntityBase, Wp02EntryCriteriaDTO> wp02EntityService;


    private WBSEntityInterface wbsEntityService;


    private ProjectInterface projectService;


    private HierarchyInterface hierarchyService;

    /**
     * 构造方法。
     */
    @Autowired
    public Wp02EntityController(
        BaseWBSEntityInterface<Wp02EntityBase, Wp02EntryCriteriaDTO> wp02EntityService,
        ProjectInterface projectService,
        HierarchyInterface hierarchyService,
        WBSEntityInterface wbsEntityService
    ) {
        this.wp02EntityService = wp02EntityService;
        this.projectService = projectService;
        this.hierarchyService = hierarchyService;
        this.wbsEntityService = wbsEntityService;
    }

    @Override
    @Operation(
        summary = "查询wp02实体",
        description = "查询wp02实体"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp02-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<? extends Wp02EntityBase> searchWp02Entities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        Wp02EntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            wp02EntityService.search(orgId, projectId, criteriaDTO, pageDTO)
        );
    }

    @Override
    @Operation(
        summary = "查询指派任务包 分段 实体",
        description = "查询指派任务包 分段 实体"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp02-task-package-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<? extends Wp02EntityBase> searchWp02TaskPackageEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        Wp02EntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            wp02EntityService.searchTaskPackageEntities(orgId, projectId, criteriaDTO, pageDTO)
        );
    }

    @Override
    @Operation(
        summary = "取得wp02实体详细信息",
        description = "取得wp02实体详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp02-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<? extends Wp02EntityBase> getWp02Entity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId
    ) {
        return new JsonObjectResponseBody<>(
            wp02EntityService.get(orgId, projectId, entityId)
        );
    }

    @Override
    @Operation(
        summary = "删除wp02实体",
        description = "删除wp02实体"
    )
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/wp02-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody deleteWp02Entity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version
    ) {


        Project project = projectService.get(orgId, projectId, version);


        if (wbsEntityService.isInWorkFlow(entityId, projectId, orgId)) {
            throw new BusinessError("",
                "business-error: Wp02 is ALREADY in work flow, CAN'T delete.");
        }


        wp02EntityService.delete(
            getContext().getOperator(),
            orgId,
            project,
            entityId
        );

        return new JsonResponseBody(getContext());
    }

    @Override
    @Operation(
        summary = "插入wp02实体",
        description = "插入wp02实体"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/wp02-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody addWp02Entity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Parameter(description = "wp02实体信息") Wp02EntryInsertDTO wp02EntryInsertDTO
    ) {

        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId);





        Wp02Entity wp02Entity = new Wp02Entity(project);
        BeanUtils.copyProperties(wp02EntryInsertDTO, wp02Entity);
        wp02Entity.setDisplayName(wp02EntryInsertDTO.getNo());
        wp02EntityService.insert(operator, orgId, projectId, wp02Entity);
        return new JsonResponseBody(getContext());
    }

    @Override
    @Operation(
        summary = "更新wp02实体",
        description = "更新wp02实体"
    )
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/wp02-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody updateWp02Entity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody @Parameter(description = "wp02实体信息") Wp02EntryUpdateDTO wp02EntryUpdateDTO
    ) {
        final OperatorDTO operator = getContext().getOperator();
        projectService.get(orgId, projectId, version);





        Wp02EntityBase wp02Entity = wp02EntityService.get(orgId, projectId, entityId);


        if (!wp02Entity.getNo().equals(wp02EntryUpdateDTO.getNo()) && wp02EntityService.existsByEntityNo(wp02EntryUpdateDTO.getNo(), projectId)) {
            throw new BusinessError("", "business-error: section  ALREADY EXISTS.");
        }
        Wp02Entity wp02EntityCommon = BeanUtils.copyProperties(wp02Entity, new Wp02Entity());
        BeanUtils.copyProperties(wp02EntryUpdateDTO, wp02EntityCommon);

        wp02EntityCommon.setDisplayName(wp02EntryUpdateDTO.getNo());
        Wp02EntityBase entityResult = wp02EntityService.update(operator, orgId, projectId, wp02EntityCommon);
        return new JsonObjectResponseBody<>(getContext(), entityResult);
    }

    @Override
    @Operation(
        summary = "更新父级信息",
        description = "更新父级信息"
    )
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/wp02-entities/{entityId}/hierarchy-parents",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody moveWp02(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody ParentsPutDTO parentsPutDTO
    ) {


        if (parentsPutDTO.getParentHierarchyIdOnStructure() != null && parentsPutDTO.getParentHierarchyIdOnStructure() != 0L) {

            HierarchyNode structureParent = hierarchyService
                .get(orgId, projectId, parentsPutDTO.getParentHierarchyIdOnStructure());



//            validateParentHierarchyNode(
//                structureParent,
//                HierarchyType.STRUCTURE,
//                HierarchyNodeType.LAYER_PACKAGE
//            );

            Wp02HierarchyInfoEntity wp02HierarchyInfoEntity =
                (Wp02HierarchyInfoEntity) wp02EntityService.get(orgId, projectId, entityId);
            hierarchyService.moveTo(
                getContext().getOperator(),
                projectService.get(orgId, projectId),
                wp02HierarchyInfoEntity.getEntityType(),
                entityId,
                wp02HierarchyInfoEntity.getEntityType(),
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
        summary = "取得wp02图纸信息",
        description = "取得wp02图纸信息"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp02-entities/{entityId}/drawing-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege()
    public JsonResponseBody getDrawingInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                           @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                           @PathVariable @Parameter(description = "实体 ID") Long entityId) {

        return new JsonListResponseBody<>(
            wp02EntityService.getDrawingInfo(
                orgId,
                projectId,
                entityId,
                null
            )
        );
    }

    /**
     * 取得wp02材料信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp02-entities/{entityId}/material-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "取得wp02材料信息",
        description = "取得wp02材料信息"
    )
    @WithPrivilege()
    @Override
    public JsonResponseBody getMaterialInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                            @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                            @PathVariable @Parameter(description = "实体 ID") Long entityId) {
        return new JsonListResponseBody<>(
            wp02EntityService.getMaterialInfo(entityId, orgId, projectId)
        );
    }

    /**
     * 按条件下载wp02实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp02-entities/download"
    )
    @Operation(
        summary = "按条件下载wp02实体列表",
        description = "按条件下载wp02实体列表"
    )
    @WithPrivilege
    @Override
    public synchronized void downloadWp02Entities(@PathVariable("orgId") Long orgId,
                                     @PathVariable("projectId") Long projectId,
                                     Wp02EntryCriteriaDTO criteriaDTO) throws IOException {

        final OperatorDTO operator = getContext().getOperator();
        File excel = wp02EntityService.saveDownloadFile(orgId, projectId, criteriaDTO, operator.getId());

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-project-entities-wp02.xlsx\""
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
