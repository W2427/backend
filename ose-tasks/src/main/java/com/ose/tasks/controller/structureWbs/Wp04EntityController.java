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
import com.ose.tasks.api.wbsStructure.Wp04EntityAPI;
import com.ose.tasks.controller.wbs.AbstractWBSEntityController;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.domain.model.service.wbs.WBSEntityInterface;
import com.ose.tasks.dto.structureWbs.Wp04EntryCriteriaDTO;
import com.ose.tasks.dto.structureWbs.Wp04EntryInsertDTO;
import com.ose.tasks.dto.structureWbs.Wp04EntryUpdateDTO;
import com.ose.tasks.dto.wbs.ParentsPutDTO;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.structureEntity.Wp04Entity;
import com.ose.tasks.entity.wbs.structureEntity.Wp04EntityBase;
import com.ose.tasks.entity.wbs.structureEntity.Wp04HierarchyInfoEntity;
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

@Api(description = "结构构件实体管理接口")
@RestController
public class Wp04EntityController extends AbstractWBSEntityController implements Wp04EntityAPI {


    private BaseWBSEntityInterface<Wp04EntityBase, Wp04EntryCriteriaDTO> wp04EntityService;


    private ProjectInterface projectService;


    private WBSEntityInterface wbsEntityService;


    private HierarchyInterface hierarchyService;

    /**
     * 构造方法。
     */
    @Autowired
    public Wp04EntityController(
        BaseWBSEntityInterface<Wp04EntityBase, Wp04EntryCriteriaDTO> wp04EntityService,
        ProjectInterface projectService,
        HierarchyInterface hierarchyService,
        WBSEntityInterface wbsEntityService
    ) {
        this.wp04EntityService = wp04EntityService;
        this.projectService = projectService;
        this.wbsEntityService = wbsEntityService;
        this.hierarchyService = hierarchyService;
    }

    @Override
    @Operation(
        summary = "查询wp04实体",
        description = "查询wp04实体"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp04-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<? extends Wp04EntityBase> searchWp04Entities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        Wp04EntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            wp04EntityService.search(orgId, projectId, criteriaDTO, pageDTO)
        );
    }

    @Override
    @Operation(
        summary = "查询指派任务包 WP04 实体",
        description = "查询指派任务包 WP04 实体"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp04-task-package-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<? extends Wp04EntityBase> searchWp04TaskPackageEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        Wp04EntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            wp04EntityService.searchTaskPackageEntities(orgId, projectId, criteriaDTO, pageDTO)
        );
    }


    @Override
    @Operation(
        summary = "取得wp04实体详细信息",
        description = "取得wp04实体详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp04-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<? extends Wp04EntityBase> getWp04Entity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId
    ) {
        return new JsonObjectResponseBody<>(
            wp04EntityService.get(orgId, projectId, entityId)
        );
    }

    @Override
    @Operation(
        summary = "删除WP04实体",
        description = "删除WP04实体"
    )
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/wp04-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody deleteWp04Entity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version
    ) {


        Project project = projectService.get(orgId, projectId, version);


        if (wbsEntityService.isInWorkFlow(entityId, projectId, orgId)) {
            throw new BusinessError("",
                "business-error: Wp04 is ALREADY in work flow, CAN'T delete.");
        }


        wp04EntityService.delete(
            getContext().getOperator(),
            orgId,
            project,
            entityId
        );


        return new JsonResponseBody(getContext());
    }

    @Override
    @Operation(
        summary = "插入wp04实体",
        description = "插入wp04实体"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/wp04-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody addWp04Entity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Parameter(description = "wp04实体信息") Wp04EntryInsertDTO wp04EntryInsertDTO
    ) {
        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId);






        Wp04Entity wp04Entity = new Wp04Entity(project);
        BeanUtils.copyProperties(wp04EntryInsertDTO, wp04Entity);


        wp04Entity.setDisplayName(wp04EntryInsertDTO.getNo());
        wp04EntityService.insert(operator, orgId, projectId, wp04Entity);
        return new JsonResponseBody(getContext());
    }

    @Override
    @Operation(
        summary = "更新wp04实体",
        description = "更新wp04实体"
    )
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/wp04-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody updateWp04Entity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody @Parameter(description = "wp04实体信息") Wp04EntryUpdateDTO wp04EntryUpdateDTO
    ) {
        final OperatorDTO operator = getContext().getOperator();
        projectService.get(orgId, projectId, version);
        Wp04EntityBase wp04Entity = wp04EntityService.get(orgId, projectId, entityId);






        Wp04Entity wp04EntityCommon = BeanUtils.copyProperties(wp04Entity, new Wp04Entity());


        if (!wp04EntityCommon.getNo().equals(wp04EntryUpdateDTO.getNo()) && wp04EntityService.existsByEntityNo(wp04EntryUpdateDTO.getNo(), projectId)) {
            throw new BusinessError("", "business-error: Component  ALREADY EXISTS.");
        }
        BeanUtils.copyProperties(wp04EntryUpdateDTO, wp04EntityCommon);


        wp04EntityCommon.setDisplayName(wp04EntryUpdateDTO.getNo());
        Wp04EntityBase entityResult = wp04EntityService.update(operator, orgId, projectId, wp04EntityCommon);
        return new JsonObjectResponseBody<>(getContext(), entityResult);
    }

    @Override
    @Operation(
        summary = "更新父级信息",
        description = "更新父级信息"
    )
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/wp04-entities/{entityId}/hierarchy-parents",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody moveWp04(
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
//                HierarchyNodeType.ENTITY
//            );


            Wp04HierarchyInfoEntity wp04HierarchyInfoEntity =
                (Wp04HierarchyInfoEntity) wp04EntityService.get(orgId, projectId, entityId);
            hierarchyService.moveTo(
                getContext().getOperator(),
                projectService.get(orgId, projectId),
                wp04HierarchyInfoEntity.getEntityType(),
                entityId,
                wp04HierarchyInfoEntity.getEntityType(),
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
        summary = "取得wp04图纸信息",
        description = "取得wp04图纸信息"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp04-entities/{entityId}/drawing-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege()
    public JsonResponseBody getDrawingInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                           @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                           @PathVariable @Parameter(description = "实体 ID") Long entityId) {

        return new JsonListResponseBody<>(
            wp04EntityService.getDrawingInfo(
                orgId,
                projectId,
                entityId,
                null
            )
        );
    }

    /**
     * 取得wp04材料信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp04-entities/{entityId}/material-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "取得wp04材料信息",
        description = "取得wp04材料信息"
    )
    @WithPrivilege()
    @Override
    public JsonResponseBody getMaterialInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                            @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                            @PathVariable @Parameter(description = "实体 ID") Long entityId) {
        return new JsonListResponseBody<>(
            wp04EntityService.getMaterialInfo(entityId, orgId, projectId)
        );
    }

    /**
     * 按条件下载wp04实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp04-entities/download"
    )
    @Operation(
        summary = "按条件下载wp04实体列表",
        description = "按条件下载wp04实体列表"
    )
    @WithPrivilege
    @Override
    public synchronized void downloadWp04Entities(@PathVariable("orgId") Long orgId,
                                     @PathVariable("projectId") Long projectId,
                                     Wp04EntryCriteriaDTO criteriaDTO) throws IOException {

        final OperatorDTO operator = getContext().getOperator();
        File excel = wp04EntityService.saveDownloadFile(orgId, projectId, criteriaDTO, operator.getId());

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-project-entities-wp04.xlsx\""
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
