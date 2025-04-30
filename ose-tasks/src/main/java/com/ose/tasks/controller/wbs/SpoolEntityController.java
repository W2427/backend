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
import com.ose.tasks.api.wbs.SpoolEntityAPI;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.wbs.ParentsPutDTO;
import com.ose.tasks.dto.wbs.SpoolEntryCriteriaDTO;
import com.ose.tasks.dto.wbs.SpoolEntryInsertDTO;
import com.ose.tasks.dto.wbs.SpoolEntryUpdateDTO;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.entity.SpoolEntity;
import com.ose.tasks.entity.wbs.entity.SpoolEntityBase;
import com.ose.tasks.entity.wbs.entity.SpoolHierarchyInfoEntity;
import com.ose.tasks.vo.wbs.HierarchyType;
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
import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Api(description = "单管实体管理接口")
@RestController
public class SpoolEntityController extends AbstractWBSEntityController implements SpoolEntityAPI {


    private BaseWBSEntityInterface<SpoolEntityBase, SpoolEntryCriteriaDTO> spoolEntityService;


    private ProjectInterface projectService;


    private HierarchyInterface hierarchyService;


    private final PlanInterface planService;

    /**
     * 构造方法。
     */
    @Autowired
    public SpoolEntityController(
        BaseWBSEntityInterface<SpoolEntityBase, SpoolEntryCriteriaDTO> spoolEntityService,
        ProjectInterface projectService,
        HierarchyInterface hierarchyService,
        PlanInterface planService
    ) {
        this.spoolEntityService = spoolEntityService;
        this.projectService = projectService;
        this.hierarchyService = hierarchyService;
        this.planService = planService;
    }

    @Override
    @Operation(summary = "查询单管实体")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/spool-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<? extends SpoolEntityBase> searchSpoolEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        SpoolEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            spoolEntityService.search(orgId, projectId, criteriaDTO, pageDTO)
        );
    }

    @Override
    @Operation(summary = "取得单管实体详细信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/spool-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<? extends SpoolEntityBase> getSpoolEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId
    ) {
        return new JsonObjectResponseBody<>(
            spoolEntityService.get(orgId, projectId, entityId)
        );
    }

    @Override
    @Operation(summary = "删除单管实体")
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/spool-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody deleteSpoolEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version
    ) {

        Project project = projectService.get(orgId, projectId, version);


/*        if (wbsEntityService.isInWorkFlow(entityId, projectId, orgId)) {
            throw new BusinessError("",
                "business-error: SPOOL is ALREADY in work flow, CAN'T delete.");
        }*/

        spoolEntityService.delete(
            getContext().getOperator(),
            orgId,
            project,
            entityId
        );







        return new JsonResponseBody(getContext());
    }

    @Override
    @Operation(summary = "插入单管实体")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/spool-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody addSpoolEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Parameter(description = "单管实体信息") SpoolEntryInsertDTO spoolEntryInsertDTO
    ) {

        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId);

        if (spoolEntityService.existsByEntityNo(spoolEntryInsertDTO.getNo(), projectId)) {
            throw new BusinessError("单管编号已被其他实体使用");
        }

        SpoolEntity spoolEntity = new SpoolEntity(project);
        BeanUtils.copyProperties(spoolEntryInsertDTO, spoolEntity);

        spoolEntity.setDisplayName(spoolEntryInsertDTO.getDisplayName());

        spoolEntity.setRemarks(spoolEntryInsertDTO.getRemarks());

        spoolEntity.setNps(spoolEntryInsertDTO.getNpsText());

        if (null != spoolEntryInsertDTO.getWeight()) {
            spoolEntity.setWeightText(spoolEntryInsertDTO.getWeight()
                + (spoolEntryInsertDTO.getWeightUnit() == null ? "" : spoolEntryInsertDTO.getWeightUnit().toString()));
        }

        if (null != spoolEntryInsertDTO.getPaintingArea()) {
            spoolEntity.setPaintingAreaText(spoolEntryInsertDTO.getPaintingArea()
                + (spoolEntryInsertDTO.getPaintingAreaUnit() == null ? "" : spoolEntryInsertDTO.getPaintingAreaUnit().toString()));
        }

        spoolEntityService.insert(operator, orgId, projectId, spoolEntity);



        spoolEntityService.setIsoIdsAndWbs(projectId, spoolEntity.getId());

        return new JsonResponseBody(getContext());
    }

    @Override
    @Operation(summary = "更新单管实体")
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/spool-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody updateSpoolEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody @Parameter(description = "单管实体信息") SpoolEntryUpdateDTO spoolEntryUpdateDTO
    ) {

        final OperatorDTO operator = getContext().getOperator();
        projectService.get(orgId, projectId, version);

        SpoolEntityBase spoolEntityBase = spoolEntityService.get(orgId, projectId, entityId);

        SpoolEntity spoolEntity = BeanUtils.copyProperties(spoolEntityBase, new SpoolEntity());
        BeanUtils.copyProperties(spoolEntryUpdateDTO, spoolEntity);

        spoolEntity.setRemarks(spoolEntryUpdateDTO.getRemarks());


        spoolEntity.setNps(spoolEntryUpdateDTO.getNpsText());

        if (null != spoolEntryUpdateDTO.getWeight()) {
            spoolEntity.setWeightText(spoolEntryUpdateDTO.getWeight()
                + (spoolEntryUpdateDTO.getWeightUnit() == null ? "" : spoolEntryUpdateDTO.getWeightUnit().toString()));
        }

        if (null != spoolEntryUpdateDTO.getPaintingArea()) {
            spoolEntity.setPaintingAreaText(spoolEntryUpdateDTO.getPaintingArea()
                + (spoolEntryUpdateDTO.getPaintingAreaUnit() == null ? "" : spoolEntryUpdateDTO.getPaintingAreaUnit().toString()));
        }
        SpoolEntityBase entityResult = spoolEntityService.update(operator, orgId, projectId, spoolEntity);
        return new JsonObjectResponseBody<>(getContext(), entityResult);
    }

    @Override
    @Operation(summary = "更新SPOOL父级信息")
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/spool-entities/{entityId}/hierarchy-parents",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody moveSpool(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody ParentsPutDTO parentsPutDTO
    ) {
        Project project = projectService.get(orgId, projectId, version);

        if (LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnPiping())) {
            throw new BusinessError("区域父级必须优先于其他父级设定.");
        } else {
            HierarchyNode lpParent = hierarchyService.get(orgId, projectId, parentsPutDTO.getParentHierarchyIdOnPiping());
            validateParentHierarchyNode(lpParent, WBSEntityType.ISO.name());

            validateParentHierarchyNode(
                hierarchyService.get(orgId, projectId, parentsPutDTO.getParentHierarchyIdOnPiping()),
                lpParent.getPath(),
                HierarchyType.PIPING.name()
            );
        }


        SpoolHierarchyInfoEntity spoolHierarchyInfoEntity = (SpoolHierarchyInfoEntity) spoolEntityService.get(orgId,
            projectId,
            entityId);

//        if (!LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnPiping())
//            && !parentsPutDTO.getParentHierarchyIdOnPiping().equals(spoolHierarchyInfoEntity.getParentHierarchyIdOnPiping())) {
//
//
//            List<Tuple> hierarchyNodeList = hierarchyService.
//                getHierarchyNodeByEntityId(orgId, projectId, parentsPutDTO.getParentHierarchyIdOnPiping());
//            if (!CollectionUtils.isEmpty(hierarchyNodeList)) {
//                Project proj = projectService.get(orgId, projectId);
//                OperatorDTO operator = getContext().getOperator();
//
//                for (Tuple hierarchyNode : hierarchyNodeList) {
//                    hierarchyService.moveTo(
//                        operator,
//                        proj,
//                        WBSEntityType.SPOOL.name(),
//                        entityId,
//                        WBSEntityType.ISO.name(),
//                        (Long) hierarchyNode.get("id")
//                    );
//                }
//            }
///*            hierarchyService.moveTo(
//                getContext().getOperator(),
//                projectService.get(orgId, projectId),
//                HierarchyNodeType.ENTITY,
//                WBSEntityType.SPOOL,
//                entityId,
//                HierarchyNodeType.ENTITY,
//                WBSEntityType.ISO,
//                parentsPutDTO.getLpParentHierarchyId()
//            );*/
//        } else if (LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnPiping())) {
//            hierarchyService.deleteOnlyHierarchyInfoByHierarchyType(getContext().getOperator(),
//                project,
//                orgId,
//                entityId,
//                HierarchyType.PIPING.name());
//        }


























        spoolEntityService.setIsoIdsAndWbs(projectId, entityId);
        return new JsonResponseBody();
    }

    /**
     * 取得单管图纸信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @Operation(summary = "取得单管图纸信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/spool-entities/{entityId}/drawing-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    public JsonResponseBody getDrawingInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                           @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                           @PathVariable @Parameter(description = "实体 ID") Long entityId) {

        return new JsonListResponseBody<>(
            spoolEntityService.getDrawingInfo(
                orgId,
                projectId,
                entityId,
                null
            )
        );
    }

    /**
     * 取得单管材料信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/spool-entities/{entityId}/material-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(summary = "取得单管材料信息")
    @WithPrivilege()
    @Override
    public JsonResponseBody getMaterialInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                            @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                            @PathVariable @Parameter(description = "实体 ID") Long entityId) {
        return new JsonListResponseBody<>(
            spoolEntityService.getMaterialInfo(
                entityId,
                orgId,
                projectId)
        );
    }

    /**
     * 按条件下载单管实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/spool-entities/download"
    )
    @Operation(summary = "按条件下载单管实体列表")
    @WithPrivilege
    @Override
    public synchronized void downloadSpoolEntities(@PathVariable("orgId") Long orgId,
                                                   @PathVariable("projectId") Long projectId,
                                                   SpoolEntryCriteriaDTO criteriaDTO) throws IOException {
        System.out.println("开始下载spool");
        final OperatorDTO operator = getContext().getOperator();
        File excel = spoolEntityService.saveDownloadFile(orgId, projectId, criteriaDTO, operator.getId());

        HttpServletResponse response = getContext().getResponse();

        System.out.println("spool下载，返回文件下载内容");
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-project-entities-spool.xlsx\""
            );

            System.out.println("spool下载，文件复制开始");
            IOUtils.copy(
                new FileInputStream(excel), response.getOutputStream()
            );
            System.out.println("spool下载，文件复制结束");

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
    @Operation(summary = "根据二维码取得单管实体详细信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/spool-entities/qr-code/{qrcode}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<? extends SpoolEntityBase> getSpoolEntityByQrCode(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") String qrcode
    ) {
        return new JsonObjectResponseBody<>(
            spoolEntityService.getByQrCode(orgId, projectId, qrcode)
        );
    }

}
