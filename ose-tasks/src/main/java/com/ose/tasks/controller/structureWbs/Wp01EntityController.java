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
import com.ose.tasks.api.wbsStructure.Wp01EntityAPI;
import com.ose.tasks.controller.wbs.AbstractWBSEntityController;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.domain.model.service.wbs.WBSEntityInterface;
import com.ose.tasks.dto.structureWbs.Wp01EntryCriteriaDTO;
import com.ose.tasks.dto.structureWbs.Wp01EntryInsertDTO;
import com.ose.tasks.dto.structureWbs.Wp01EntryUpdateDTO;
import com.ose.tasks.dto.wbs.ParentsPutDTO;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.entity.wbs.structureEntity.Wp01Entity;
import com.ose.tasks.entity.wbs.structureEntity.Wp01EntityBase;
import com.ose.tasks.entity.wbs.structureEntity.Wp01HierarchyInfoEntity;
import com.ose.tasks.vo.wbs.HierarchyType;
import com.ose.util.BeanUtils;
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

@Api(description = "结构模块实体管理接口")
@RestController
public class Wp01EntityController extends AbstractWBSEntityController implements Wp01EntityAPI {


    private BaseWBSEntityInterface<Wp01EntityBase, Wp01EntryCriteriaDTO> wp01EntityService;


    private WBSEntityInterface wbsEntityService;


    private ProjectInterface projectService;


    private HierarchyInterface hierarchyService;


    /**
     * 构造方法。
     */
    @Autowired
    public Wp01EntityController(
        BaseWBSEntityInterface<Wp01EntityBase, Wp01EntryCriteriaDTO> wp01EntityService,
        ProjectInterface projectService,
        HierarchyInterface hierarchyService,
        WBSEntityInterface wbsEntityService
    ) {
        this.wp01EntityService = wp01EntityService;
        this.projectService = projectService;
        this.hierarchyService = hierarchyService;
        this.wbsEntityService = wbsEntityService;
    }

    @Override
    @Operation(
        summary = "查询模块实体",
        description = "查询模块实体"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp01-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<? extends Wp01EntityBase> searchWp01Entities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        Wp01EntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            wp01EntityService.search(orgId, projectId, criteriaDTO, pageDTO)
        );
    }


    @Override
    @Operation(
        summary = "查询指派任务包 模块实体",
        description = "查询指派任务包 模块实体"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp01-task-package-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<? extends Wp01EntityBase> searchWp01TaskPackageEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        Wp01EntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            wp01EntityService.searchTaskPackageEntities(orgId, projectId, criteriaDTO, pageDTO)
        );
    }

    @Override
    @Operation(
        summary = "取得模块实体详细信息",
        description = "取得模块实体详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp01-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<? extends Wp01EntityBase> getWp01Entity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId
    ) {
        return new JsonObjectResponseBody<>(
            wp01EntityService.get(orgId, projectId, entityId)
        );
    }

    @Override
    @Operation(
        summary = "删除模块实体",
        description = "删除模块实体"
    )
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/wp01-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody deleteWp01Entity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version
    ) {


        Project project = projectService.get(orgId, projectId, version);

/*
        if (wbsEntityService.isInWorkFlow(entityId, projectId, orgId)) {
            throw new BusinessError("",
                "business-error: Wp01 is ALREADY in work flow, CAN'T delete.");
        }*/


        wp01EntityService.delete(
            getContext().getOperator(),
            orgId,
            project,
            entityId
        );

        return new JsonResponseBody(getContext());
    }

    @Override
    @Operation(
        summary = "插入模块实体",
        description = "插入模块实体"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/wp01-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody addWp01Entity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Parameter(description = "模块实体信息") Wp01EntryInsertDTO wp01EntryInsertDTO
    ) {
        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId);
        Wp01Entity wp01Entity = new Wp01Entity(project);





        BeanUtils.copyProperties(wp01EntryInsertDTO, wp01Entity);


        wp01Entity.setDisplayName(wp01EntryInsertDTO.getNo());
        wp01EntityService.insert(operator, orgId, projectId, wp01Entity);
        return new JsonResponseBody(getContext());
    }

    @Override
    @Operation(
        summary = "更新模块实体",
        description = "更新模块实体"
    )
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/wp01-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody updateWp01Entity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody @Parameter(description = "模块实体信息") Wp01EntryUpdateDTO wp01EntryUpdateDTO
    ) {
        final OperatorDTO operator = getContext().getOperator();
        projectService.get(orgId, projectId, version);
        Wp01EntityBase wp01Entity = wp01EntityService.get(orgId, projectId, entityId);







        if (!wp01Entity.getNo().equals(wp01EntryUpdateDTO.getNo()) && wp01EntityService.existsByEntityNo(wp01EntryUpdateDTO.getNo(), projectId)) {
            throw new BusinessError("", "business-error: module  ALREADY EXISTS.");
        }
        Wp01Entity wp01EntityCommon = BeanUtils.copyProperties(wp01Entity, new Wp01Entity());
        BeanUtils.copyProperties(wp01EntryUpdateDTO, wp01EntityCommon);


        wp01EntityCommon.setDisplayName(wp01EntryUpdateDTO.getNo());
        Wp01EntityBase entityResult = wp01EntityService.update(operator, orgId, projectId, wp01EntityCommon);
        return new JsonObjectResponseBody<>(getContext(), entityResult);
    }

    @Override
    @Operation(
        summary = "更新父级信息",
        description = "更新父级信息"
    )
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/wp01-entities/{entityId}/hierarchy-parents",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody moveWp01(
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
//                HierarchyNodeType.MODULE,
//                HierarchyNodeType.SUB_MODULE,
//                HierarchyNodeType.AREA,
//                HierarchyNodeType.SUB_AREA
//            );


            final String rootPath = String.format("%s%s/", structureParent.getPath(), structureParent.getId());

            Wp01HierarchyInfoEntity wp01HierarchyInfoEntity =
                (Wp01HierarchyInfoEntity) wp01EntityService.get(orgId, projectId, entityId);
            hierarchyService.moveTo(
                getContext().getOperator(),
                projectService.get(orgId, projectId),
                wp01HierarchyInfoEntity.getEntityType(),
                entityId,
                wp01HierarchyInfoEntity.getEntityType(),
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
        summary = "取得模块图纸信息",
        description = "取得模块图纸信息"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp01-entities/{entityId}/drawing-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege()
    public JsonListResponseBody<SubDrawing> getDrawingInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                           @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                           @PathVariable @Parameter(description = "实体 ID") Long entityId) {

        return new JsonListResponseBody<SubDrawing>(
            wp01EntityService.getDrawingInfo(
                orgId,
                projectId,
                entityId,
                null
            )
        );
    }

    /**
     * 取得模块材料信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp01-entities/{entityId}/material-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "取得模块材料信息",
        description = "取得模块材料信息"
    )
    @WithPrivilege()
    @Override
    public JsonResponseBody getMaterialInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                            @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                            @PathVariable @Parameter(description = "实体 ID") Long entityId) {
        return new JsonListResponseBody<>(
            wp01EntityService.getMaterialInfo(entityId, orgId, projectId)
        );
    }

    /**
     * 按条件下载模块实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp01-entities/download"
    )
    @Operation(
        summary = "按条件下载模块实体列表",
        description = "按条件下载模块实体列表"
    )
    @WithPrivilege
    @Override
    public synchronized void downloadWp01Entities(@PathVariable("orgId") Long orgId,
                                     @PathVariable("projectId") Long projectId,
                                     Wp01EntryCriteriaDTO criteriaDTO) throws IOException {

        final OperatorDTO operator = getContext().getOperator();
        File excel = wp01EntityService.saveDownloadFile(orgId, projectId, criteriaDTO, operator.getId());

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-project-entities-wp01.xlsx\""
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
