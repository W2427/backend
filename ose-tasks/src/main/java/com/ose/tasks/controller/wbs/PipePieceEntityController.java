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
import com.ose.tasks.api.wbs.PipePieceEntityAPI;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.wbs.ParentsPutDTO;
import com.ose.tasks.dto.wbs.PipePieceEntryCriteriaDTO;
import com.ose.tasks.dto.wbs.PipePieceEntryInsertDTO;
import com.ose.tasks.dto.wbs.PipePieceEntryUpdateDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.entity.PipePieceEntity;
import com.ose.tasks.entity.wbs.entity.PipePieceEntityBase;
import com.ose.tasks.entity.wbs.entity.PipePieceHierarchyInfoEntity;
import com.ose.tasks.vo.wbs.WBSEntityType;
import com.ose.util.BeanUtils;
import com.ose.util.CollectionUtils;
import com.ose.util.LongUtils;
import io.swagger.annotations.Api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.Tuple;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Api(description = "管段实体管理接口")
@RestController
public class PipePieceEntityController extends AbstractWBSEntityController implements PipePieceEntityAPI {


    private BaseWBSEntityInterface<PipePieceEntityBase, PipePieceEntryCriteriaDTO> pipePieceEntityService;


    private ProjectInterface projectService;


    private HierarchyInterface hierarchyService;


    private final PlanInterface planService;

    /**
     * 构造方法。
     */
    @Autowired
    public PipePieceEntityController(
        BaseWBSEntityInterface<PipePieceEntityBase, PipePieceEntryCriteriaDTO> pipePieceEntityService,
        ProjectInterface projectService,
        HierarchyInterface hierarchyService,
        PlanInterface planService
    ) {
        this.pipePieceEntityService = pipePieceEntityService;
        this.projectService = projectService;
        this.hierarchyService = hierarchyService;
        this.planService = planService;
    }

    @Override
    @Operation(summary = "查询管段实体")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/pipe-piece-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<? extends PipePieceEntityBase> searchPipeEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        PipePieceEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            pipePieceEntityService.search(orgId, projectId, criteriaDTO, pageDTO)
        );
    }

    @Override
    @Operation(summary = "取得管段实体详细信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/pipe-piece-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<? extends PipePieceEntityBase> getPipeEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId
    ) {
        return new JsonObjectResponseBody<>(
            pipePieceEntityService.get(orgId, projectId, entityId)
        );
    }

    @Override
    @Operation(summary = "删除管段实体")
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/pipe-piece-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody deletePipeEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version
    ) {


        Project project = projectService.get(orgId, projectId, version);


/*        if (wbsEntityService.isInWorkFlow(entityId, projectId, orgId)) {
            throw new BusinessError("",
                "business-error: PIPE_PIECE is ALREADY in work flow, CAN'T delete.");
        }*/

        pipePieceEntityService.delete(
            getContext().getOperator(),
            orgId,
            project,
            entityId
        );



        hierarchyService.delete(getContext().getOperator(), project, orgId, entityId);



        return new JsonResponseBody();
    }

    @Override
    @Operation(summary = "插入Pipe-piece实体")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/pipe-piece-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody addPipePieceEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Parameter(description = "管段实体信息") PipePieceEntryInsertDTO pipePieceEntryInsertDTO
    ) {

        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId);

        if (pipePieceEntityService.existsByEntityNo(pipePieceEntryInsertDTO.getNo(), projectId)) {
            throw new BusinessError("", "business-error: Pipe piece no ALREADY EXISTS.");
        }

        PipePieceEntity pipePieceEntity = new PipePieceEntity(project);
        BeanUtils.copyProperties(pipePieceEntryInsertDTO, pipePieceEntity);

        pipePieceEntity.setDisplayName(pipePieceEntryInsertDTO.getDisplayName());

        pipePieceEntity.setRemarks(pipePieceEntryInsertDTO.getRemarks());


        pipePieceEntity.setNps(pipePieceEntryInsertDTO.getNpsText());

        pipePieceEntity.setLength(pipePieceEntryInsertDTO.getLengthText());

        pipePieceEntityService.insert(operator, orgId, projectId, pipePieceEntity);

        pipePieceEntityService.setParentInfo(pipePieceEntryInsertDTO.getParentNodeNo(),
            pipePieceEntity,
            projectId,
            orgId,
            operator);





        pipePieceEntityService.setIsoIdsAndWbs(projectId, pipePieceEntity.getId());

        return new JsonResponseBody(getContext());
    }

    @Override
    @Operation(summary = "更新Pipe-piece实体")
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/pipe-piece-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody updatePipePieceEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody @Parameter(description = "管段实体信息") PipePieceEntryUpdateDTO pipePieceEntryUpdateDTO
    ) {

        final OperatorDTO operator = getContext().getOperator();
        projectService.get(orgId, projectId, version);

        PipePieceEntityBase pipePieceEntityBase = pipePieceEntityService.get(orgId, projectId, entityId);
        PipePieceEntity pipePieceEntity = BeanUtils.copyProperties(pipePieceEntityBase, new PipePieceEntity());

        BeanUtils.copyProperties(pipePieceEntryUpdateDTO, pipePieceEntity);

        pipePieceEntity.setRemarks(pipePieceEntryUpdateDTO.getRemarks());


        pipePieceEntity.setNps(pipePieceEntryUpdateDTO.getNpsText());

        pipePieceEntity.setLength(pipePieceEntryUpdateDTO.getLengthText());

        PipePieceEntityBase entityResult = pipePieceEntityService.update(operator, orgId, projectId, pipePieceEntity);
        return new JsonObjectResponseBody<>(getContext(), entityResult);
    }

    @Override
    @Operation(summary = "更新所属单管")
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/pipe-piece-entities/{entityId}/spool",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody moveToSpool(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody ParentsPutDTO parentsPutDTO
    ) {
        Project project = projectService.get(orgId, projectId, version);


        if (!LongUtils.isEmpty(parentsPutDTO.getParentSpoolHierarchyId())) {
            validateParentHierarchyNode(
                hierarchyService.get(orgId, projectId, parentsPutDTO.getParentSpoolHierarchyId()),
                WBSEntityType.SPOOL.name()
            );
        }


        PipePieceHierarchyInfoEntity pipePieceHierarchyInfoEntity
            = (PipePieceHierarchyInfoEntity) pipePieceEntityService.get(orgId,
            projectId,
            entityId);

//        if (!LongUtils.isEmpty(parentsPutDTO.getParentSpoolHierarchyId())
//            && !parentsPutDTO.getParentSpoolHierarchyId().equals(pipePieceHierarchyInfoEntity.getParentHierarchyIdOnPiping())) {
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
//                        WBSEntityType.PIPE_PIECE.name(),
//                        entityId,
//                        WBSEntityType.SPOOL.name(),
//                        (Long) hierarchyNode.get("id")
//                    );
//                }
//            }
//        } else if (LongUtils.isEmpty(parentsPutDTO.getParentSpoolHierarchyId())) {
//
//            hierarchyService.deleteAllHierarchyInfoByEntityId(getContext().getOperator(),
//                project,
//                orgId,
//                entityId);
//        }



        Set<Long> entityIds = new HashSet<>();
        entityIds.add(entityId);
        pipePieceEntityService.setIsoIdsAndWbs(projectId, entityId);
        return new JsonResponseBody();
    }

    /**
     * 取得管段图纸信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @Operation(summary = "取得管段图纸信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/pipe-piece-entities/{entityId}/drawing-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    public JsonResponseBody getDrawingInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                           @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                           @PathVariable @Parameter(description = "实体 ID") Long entityId) {

        return new JsonListResponseBody<>(
            pipePieceEntityService.getDrawingInfo(
                orgId,
                projectId,
                entityId,
                null
            )
        );
    }

    /**
     * 取得管段材料信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/pipe-piece-entities/{entityId}/material-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(summary = "取得管段材料信息")
    @WithPrivilege()
    @Override
    public JsonResponseBody getMaterialInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                            @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                            @PathVariable @Parameter(description = "实体 ID") Long entityId) {

        return new JsonListResponseBody<>(
            pipePieceEntityService.getMaterialInfo(entityId, orgId, projectId)
        );
    }

    /**
     * 按条件下载管段实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/pipe-piece-entities/download"
    )
    @Operation(summary = "按条件下载管段实体列表")
    @WithPrivilege
    @Override
    public synchronized void downloadPipePieceEntities(@PathVariable("orgId") Long orgId,
                                          @PathVariable("projectId") Long projectId,
                                          PipePieceEntryCriteriaDTO criteriaDTO) throws IOException {

        final OperatorDTO operator = getContext().getOperator();
        File excel = pipePieceEntityService.saveDownloadFile(orgId, projectId, criteriaDTO, operator.getId());

        HttpServletResponse response = getContext().getResponse();
        try {

            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-project-entities-pipe-piece.xlsx\""
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
