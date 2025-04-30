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
import com.ose.tasks.api.wbsStructure.Wp03EntityAPI;
import com.ose.tasks.controller.wbs.AbstractWBSEntityController;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.domain.model.service.wbs.WBSEntityInterface;
import com.ose.tasks.dto.structureWbs.Wp03EntryCriteriaDTO;
import com.ose.tasks.dto.structureWbs.Wp03EntryInsertDTO;
import com.ose.tasks.dto.structureWbs.Wp03EntryUpdateDTO;
import com.ose.tasks.dto.wbs.ParentsPutDTO;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.entity.wbs.structureEntity.Wp03Entity;
import com.ose.tasks.entity.wbs.structureEntity.Wp03EntityBase;
import com.ose.tasks.entity.wbs.structureEntity.Wp03HierarchyInfoEntity;
import com.ose.tasks.vo.wbs.HierarchyType;
import com.ose.util.BeanUtils;
import com.ose.util.LongUtils;
import io.swagger.annotations.Api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Api(description = "结构片体实体管理接口")
@RestController
public class Wp03EntityController extends AbstractWBSEntityController implements Wp03EntityAPI {


    private BaseWBSEntityInterface<Wp03EntityBase, Wp03EntryCriteriaDTO> wp03EntityService;


    private WBSEntityInterface wbsEntityService;


    private ProjectInterface projectService;


    private HierarchyInterface hierarchyService;


    /**
     * 构造方法。
     */
    @Autowired
    public Wp03EntityController(
        BaseWBSEntityInterface<Wp03EntityBase, Wp03EntryCriteriaDTO> wp03EntityService,
        ProjectInterface projectService,
        HierarchyInterface hierarchyService,
        WBSEntityInterface wbsEntityService
    ) {
        this.wp03EntityService = wp03EntityService;
        this.projectService = projectService;
        this.hierarchyService = hierarchyService;
        this.wbsEntityService = wbsEntityService;
    }

    @Override
    @Operation(
        summary = "查询wp03实体",
        description = "查询wp03实体"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp03-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<? extends Wp03EntityBase> searchWp03Entities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        Wp03EntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            wp03EntityService.search(orgId, projectId, criteriaDTO, pageDTO)
        );
    }

    @Override
    @Operation(
        summary = "查询指派任务包 WP03实体",
        description = "查询指派任务包 WP03实体"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp03-task-package-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<? extends Wp03EntityBase> searchWp03TaskPackageEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        Wp03EntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            wp03EntityService.searchTaskPackageEntities(orgId, projectId, criteriaDTO, pageDTO)
        );
    }

    @Override
    @Operation(
        summary = "取得wp03实体详细信息",
        description = "取得wp03实体详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp03-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<? extends Wp03EntityBase> getWp03Entity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId
    ) {
        return new JsonObjectResponseBody<>(
            wp03EntityService.get(orgId, projectId, entityId)
        );
    }

    @Override
    @Operation(
        summary = "删除wp03实体",
        description = "删除wp03实体"
    )
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/wp03-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody deleteWp03Entity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version
    ) {


        Project project = projectService.get(orgId, projectId, version);


        if (wbsEntityService.isInWorkFlow(entityId, projectId, orgId)) {
            throw new BusinessError("",
                "business-error: Wp03 is ALREADY in work flow, CAN'T delete.");
        }


        wp03EntityService.delete(
            getContext().getOperator(),
            orgId,
            project,
            entityId
        );

        return new JsonResponseBody(getContext());
    }

    @Override
    @Operation(
        summary = "插入wp03实体",
        description = "插入wp03实体"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/wp03-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody addWp03Entity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Parameter(description = "wp03实体信息") Wp03EntryInsertDTO wp03EntryInsertDTO
    ) {




        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId);
        Wp03Entity wp03Entity = new Wp03Entity(project);
        BeanUtils.copyProperties(wp03EntryInsertDTO, wp03Entity);
        wp03Entity.setDisplayName(wp03EntryInsertDTO.getNo());
        wp03EntityService.insert(operator, orgId, projectId, wp03Entity);
        return new JsonResponseBody(getContext());
    }

    @Override
    @Operation(
        summary = "更新wp03实体",
        description = "更新wp03实体"
    )
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/wp03-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody updateWp03Entity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody @Parameter(description = "wp03实体信息") Wp03EntryUpdateDTO wp03EntryUpdateDTO
    ) {
        final OperatorDTO operator = getContext().getOperator();
        projectService.get(orgId, projectId, version);
        Wp03EntityBase wp03Entity = wp03EntityService.get(orgId, projectId, entityId);
        Wp03Entity wp03EntityCommon = BeanUtils.copyProperties(wp03Entity, new Wp03Entity());







        if (!wp03EntityCommon.getNo().equals(wp03EntryUpdateDTO.getNo()) && wp03EntityService.existsByEntityNo(wp03EntryUpdateDTO.getNo(), projectId)) {
            throw new BusinessError("", "business-error: panel  ALREADY EXISTS.");
        }
        BeanUtils.copyProperties(wp03EntryUpdateDTO, wp03EntityCommon);


        wp03EntityCommon.setDisplayName(wp03EntryUpdateDTO.getNo());
        Wp03EntityBase entityResult = wp03EntityService.update(operator, orgId, projectId, wp03EntityCommon);
        return new JsonObjectResponseBody<>(getContext(), entityResult);
    }

    @Override
    @Operation(
        summary = "更新父级信息",
        description = "更新父级信息"
    )
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/wp03-entities/{entityId}/hierarchy-parents",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody moveWp03(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody ParentsPutDTO parentsPutDTO
    ) {


        if (!LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnStructure())) {

            HierarchyNode structureParent = hierarchyService
                .get(orgId, projectId, parentsPutDTO.getParentHierarchyIdOnStructure());




//            validateParentHierarchyNode(
//                structureParent,
//                HierarchyType.STRUCTURE,
//                HierarchyNodeType.MODULE,
//                HierarchyNodeType.LAYER_PACKAGE
//            );


            Wp03HierarchyInfoEntity wp03HierarchyInfoEntity =
                (Wp03HierarchyInfoEntity) wp03EntityService.get(orgId, projectId, entityId);
            hierarchyService.moveTo(
                getContext().getOperator(),
                projectService.get(orgId, projectId),
                wp03HierarchyInfoEntity.getEntityType(),
                entityId,
                wp03HierarchyInfoEntity.getEntityType(),
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
        summary = "取得wp03图纸信息",
        description = "取得wp03图纸信息"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp03-entities/{entityId}/drawing-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege()
    public JsonListResponseBody<SubDrawing> getDrawingInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                           @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                           @PathVariable @Parameter(description = "实体 ID") Long entityId) {

        return new JsonListResponseBody<SubDrawing>(
            wp03EntityService.getDrawingInfo(
                orgId,
                projectId,
                entityId,
                null
            )
        );
    }

    /**
     * 取得wp03材料信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp03-entities/{entityId}/material-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "取得wp03材料信息",
        description = "取得wp03材料信息"
    )
    @WithPrivilege()
    @Override
    public JsonResponseBody getMaterialInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                            @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                            @PathVariable @Parameter(description = "实体 ID") Long entityId) {
        return new JsonListResponseBody<>(
            wp03EntityService.getMaterialInfo(entityId, orgId, projectId)
        );
    }

    /**
     * 按条件下载wp03实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp03-entities/download"
    )
    @Operation(
        summary = "按条件下载wp03实体列表",
        description = "按条件下载wp03实体列表"
    )
    @WithPrivilege
    @Override
    public synchronized void downloadWp03Entities(@PathVariable("orgId") Long orgId,
                                     @PathVariable("projectId") Long projectId,
                                     Wp03EntryCriteriaDTO criteriaDTO) throws IOException {

        final OperatorDTO operator = getContext().getOperator();
        File excel = wp03EntityService.saveDownloadFile(orgId, projectId, criteriaDTO, operator.getId());

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-project-entities-wp03.xlsx\""
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
