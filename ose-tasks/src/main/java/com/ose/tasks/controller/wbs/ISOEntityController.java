package com.ose.tasks.controller.wbs;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.wbs.ISOEntityAPI;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.domain.model.service.wbs.piping.ISOEntityInterface;
import com.ose.tasks.domain.model.service.wbs.piping.ISOEntityService;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.structureWbs.Wp01EntryCriteriaDTO;
import com.ose.tasks.dto.wbs.ISOEntryCriteriaDTO;
import com.ose.tasks.dto.wbs.ISOEntryInsertDTO;
import com.ose.tasks.dto.wbs.ISOEntryUpdateDTO;
import com.ose.tasks.dto.wbs.ParentsPutDTO;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.entity.ISOEntity;
import com.ose.tasks.entity.wbs.entity.ISOEntityBase;
import com.ose.tasks.entity.wbs.entity.ISOHierarchyInfoEntity;
import com.ose.tasks.entity.wbs.structureEntity.Wp01EntityBase;
import com.ose.tasks.vo.wbs.HierarchyType;
import com.ose.tasks.vo.wbs.WBSEntityType;
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

import static com.ose.tasks.vo.setting.BatchTaskCode.BOM_PERCENT_UPDATE;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Api(description = "管线实体管理接口")
@RestController
public class ISOEntityController extends AbstractWBSEntityController implements ISOEntityAPI {


    private ISOEntityInterface isoEntityService;


    private ProjectInterface projectService;


    private HierarchyInterface hierarchyService;


    private final BatchTaskInterface batchTaskService;


    private final PlanInterface planService;




    /**
     * 构造方法。
     */
    @Autowired
    public ISOEntityController(
        ISOEntityInterface isoEntityService,
        ProjectInterface projectService,
        HierarchyInterface hierarchyService,
        BatchTaskInterface batchTaskService,
        PlanInterface planService
    ) {
        this.isoEntityService = isoEntityService;
        this.projectService = projectService;
        this.hierarchyService = hierarchyService;
        this.batchTaskService = batchTaskService;
        this.planService = planService;
    }

    @Override
    @Operation(summary = "查询管线实体")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/iso-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<? extends ISOEntityBase> searchISOEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        ISOEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            isoEntityService.search(orgId, projectId, criteriaDTO, pageDTO)
        );
    }

    @Override
    @Operation(summary = "查询管系模块")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/iso-entities-modules",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<ISOEntity> searchISOEntitiesModules(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId
    ) {
        return new JsonListResponseBody<>(
            isoEntityService.searchModules(orgId, projectId)
        );
    }

    @Override
    @Operation(summary = "取得管线实体详细信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/iso-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<? extends ISOEntityBase> getISOEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId
    ) {
        return new JsonObjectResponseBody<>(
            isoEntityService.get(orgId, projectId, entityId)
        );
    }

    @Override
    @Operation(summary = "删除管线实体")
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/iso-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody deleteISOEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version
    ) {


        Project project = projectService.get(orgId, projectId, version);


/*        if (wbsEntityService.isInWorkFlow(entityId, projectId, orgId)) {
            throw new BusinessError("",
                "business-error: ISO is ALREADY in work flow, CAN'T delete.");
        }*/


        isoEntityService.delete(
            getContext().getOperator(),
            orgId,
            project,
            entityId
        );







        return new JsonResponseBody(getContext());
    }

    @Override
    @Operation(summary = "插入管线实体")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/iso-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody addISOEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Parameter(description = "管线实体信息") ISOEntryInsertDTO isoEntryInsertDTO
    ) {

        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId);

        if (isoEntityService.existsByEntityNo(isoEntryInsertDTO.getNo(), projectId)) {
            throw new BusinessError("", "business-error: iso no ALREADY EXISTS.");
        }
        ISOEntity isoEntity = new ISOEntity(project);
        BeanUtils.copyProperties(isoEntryInsertDTO, isoEntity);

        isoEntity.setDisplayName(isoEntryInsertDTO.getDisplayName());

        isoEntity.setRemarks2(isoEntryInsertDTO.getRemarks2());


        isoEntity.setNps(isoEntryInsertDTO.getNpsText());


        setDesignPressureDisplayText(
            isoEntity,
            isoEntryInsertDTO.getDesignPressureExtraInfo(),
            isoEntryInsertDTO.getDesignPressure(),
            isoEntryInsertDTO.getDesignPressureUnit()
        );


        if (null != isoEntryInsertDTO.getDesignTemperature()) {
            isoEntity.setDesignTemperatureText(isoEntryInsertDTO.getDesignTemperature()
                + (isoEntryInsertDTO.getDesignTemperatureUnit() == null
                ? ""
                : isoEntryInsertDTO.getDesignTemperatureUnit().toString()));
        }

        if (null != isoEntryInsertDTO.getOperatePressure()) {
            isoEntity.setOperatePressureText(isoEntryInsertDTO.getOperatePressure()
                + (isoEntryInsertDTO.getOperatePressureUnit() == null
                ? ""
                : isoEntryInsertDTO.getOperatePressureUnit().toString()));
        }

        if (null != isoEntryInsertDTO.getOperateTemperature()) {
            isoEntity.setOperateTemperatureText(isoEntryInsertDTO.getOperateTemperature()
                + (isoEntryInsertDTO.getOperateTemperatureUnit() == null
                ? ""
                : isoEntryInsertDTO.getOperateTemperatureUnit().toString()));
        }

        isoEntity.setInsulationThickness(isoEntryInsertDTO.getInsulationThicknessText());


        if (null != isoEntryInsertDTO.getTestPressure()) {
            isoEntity.setTestPressureText(isoEntryInsertDTO.getTestPressure()
                + (isoEntryInsertDTO.getTestPressureUnit() == null
                ? ""
                : isoEntryInsertDTO.getTestPressureUnit().toString()));
        }



        isoEntityService.insert(operator, orgId, projectId, isoEntity);


        isoEntityService.setIsoIdsAndWbs(projectId, isoEntity.getId());
        return new JsonResponseBody(getContext());
    }

    @Override
    @Operation(summary = "更新管线实体")
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/iso-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody updateISOEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody @Parameter(description = "管线实体信息") ISOEntryUpdateDTO isoEntryUpdateDTO
    ) {

        final OperatorDTO operator = getContext().getOperator();
        projectService.get(orgId, projectId, version);

        ISOEntityBase isoEntityBase = isoEntityService.get(orgId, projectId, entityId);
        ISOEntity isoEntityCommon = BeanUtils.copyProperties(isoEntityBase, new ISOEntity());
        BeanUtils.copyProperties(isoEntryUpdateDTO, isoEntityCommon);

        isoEntityCommon.setRemarks2(isoEntryUpdateDTO.getRemarks2());


        isoEntityCommon.setNps(isoEntryUpdateDTO.getNpsText());


        setDesignPressureDisplayText(
            isoEntityCommon,
            isoEntryUpdateDTO.getDesignPressureExtraInfo(),
            isoEntryUpdateDTO.getDesignPressure(),
            isoEntryUpdateDTO.getDesignPressureUnit()
        );


        if (null != isoEntryUpdateDTO.getDesignTemperature()) {
            isoEntityCommon.setDesignTemperatureText(isoEntryUpdateDTO.getDesignTemperature()
                + (isoEntryUpdateDTO.getDesignTemperatureUnit() == null
                ? ""
                : isoEntryUpdateDTO.getDesignTemperatureUnit().toString()));
        }

        if (null != isoEntryUpdateDTO.getOperatePressure()) {
            isoEntityCommon.setOperatePressureText(isoEntryUpdateDTO.getOperatePressure()
                + (isoEntryUpdateDTO.getOperatePressureUnit() == null
                ? ""
                : isoEntryUpdateDTO.getOperatePressureUnit().toString()));
        }

        if (null != isoEntryUpdateDTO.getOperateTemperature()) {
            isoEntityCommon.setOperateTemperatureText(isoEntryUpdateDTO.getOperateTemperature()
                + (isoEntryUpdateDTO.getOperateTemperatureUnit() == null
                ? ""
                : isoEntryUpdateDTO.getOperateTemperatureUnit().toString()));
        }

        isoEntityCommon.setInsulationThickness(isoEntryUpdateDTO.getInsulationThicknessText());


        if (null != isoEntryUpdateDTO.getTestPressure()) {
            isoEntityCommon.setTestPressureText(isoEntryUpdateDTO.getTestPressure()
                + (isoEntryUpdateDTO.getTestPressureUnit() == null
                ? ""
                : isoEntryUpdateDTO.getTestPressureUnit().toString()));
        }

        ISOEntityBase entityResult = isoEntityService.update(operator, orgId, projectId, isoEntityCommon);
        return new JsonObjectResponseBody<>(getContext(), entityResult);
    }

    @Override
    @Operation(summary = "更新父级信息")
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/iso-entities/{entityId}/hierarchy-parents",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody moveISO(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody ParentsPutDTO parentsPutDTO
    ) {
        /*
        ISO 有 AREA，LAYER_PACKAGE, PRESSURE_TEST_PACKAGE, CLEAN_PACKAGE, SUB_SYSTEM 5个父级
        如果项目设定中，没有LAYER_PACKAGE时，父级选择为4个，这时候将AREA复制到 LAYER_PACKAGE
         */
        Project project = projectService.get(orgId, projectId, version);

        if (LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnPiping())
            && (!LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnPtp())
            || !LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnCp())
            || !LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnSs()))
            ) {
            throw new BusinessError("区域父级必须优先于其他父级设定");
        }

        if (!LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnPiping())) {

            HierarchyNode areaParent = hierarchyService
                .get(orgId, projectId, parentsPutDTO.getParentHierarchyIdOnPiping());

            validateParentHierarchyNode(
                areaParent,
                HierarchyType.PIPING.name(),
                "AREA"
            );

            final String rootPath = String.format("%s%s/", areaParent.getPath(), areaParent.getId());

            if (!LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnPtp())) {
                validateParentHierarchyNode(
                    hierarchyService.get(orgId, projectId, parentsPutDTO.getParentHierarchyIdOnPtp()),
                    rootPath,
                    HierarchyType.PRESSURE_TEST_PACKAGE.name()
                );
            }

            if (!LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnCp())) {
                validateParentHierarchyNode(
                    hierarchyService.get(orgId, projectId, parentsPutDTO.getParentHierarchyIdOnCp()),
                    rootPath,
                    HierarchyType.CLEAN_PACKAGE.name()
                );
            }

            if (!LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnSs())) {
                validateParentHierarchyNode(
                    hierarchyService.get(orgId, projectId, parentsPutDTO.getParentHierarchyIdOnSs()),
                    rootPath,
                    HierarchyType.SUB_SYSTEM.name()
                );
            }
        }


        ISOHierarchyInfoEntity isoHierarchyInfoEntity =
            (ISOHierarchyInfoEntity) isoEntityService.get(orgId, projectId, entityId);


//        if (!LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnPiping())
//            && !parentsPutDTO.getParentHierarchyIdOnPiping().equals(isoHierarchyInfoEntity.getParentHierarchyIdOnPiping())) {
//            hierarchyService.moveTo(
//                getContext().getOperator(),
//                projectService.get(orgId, projectId),
//                WBSEntityType.ISO.name(),
//                entityId,
//                null,
//                parentsPutDTO.getParentHierarchyIdOnPiping()
//            );
//
//            if (!project.getAreaOptionalNodeTypes().equalsIgnoreCase("LAYER_PACKAGE")) {
//                hierarchyService.moveTo(
//                    getContext().getOperator(),
//                    projectService.get(orgId, projectId),
//                    WBSEntityType.ISO.name(),
//                    entityId,
//                    null,
//                    parentsPutDTO.getParentHierarchyIdOnPiping()
//                );
//            }
//        } else if (LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnPiping())) {
//            hierarchyService.deleteOnlyHierarchyInfoByHierarchyType(getContext().getOperator(),
//                project,
//                orgId,
//                entityId,
//                HierarchyType.PIPING.name()
//            );
//        }
//
//
//        if (!LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnPiping())
//            && !parentsPutDTO.getParentHierarchyIdOnPiping().equals(isoHierarchyInfoEntity.getParentHierarchyIdOnPiping())) {
//            hierarchyService.moveTo(
//                getContext().getOperator(),
//                projectService.get(orgId, projectId),
//                WBSEntityType.ISO.name(),
//                entityId,
//                null,
//                parentsPutDTO.getParentHierarchyIdOnPiping()
//            );
//        } else if (LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnPiping())) {
//            hierarchyService.deleteOnlyHierarchyInfoByHierarchyType(getContext().getOperator(),
//                project,
//                orgId,
//                entityId,
//                HierarchyType.PIPING.name()
//            );
//        }
//
//
//        if (!LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnPtp())
//            && !parentsPutDTO.getParentHierarchyIdOnPtp().equals(isoHierarchyInfoEntity.getParentHierarchyIdOnPtp())) {
//            hierarchyService.moveTo(
//                getContext().getOperator(),
//                projectService.get(orgId, projectId),
//                WBSEntityType.ISO.name(),
//                entityId,
//                null,
//                parentsPutDTO.getParentHierarchyIdOnPtp()
//            );
//        } else if (LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnPtp())) {
//            hierarchyService.deleteOnlyHierarchyInfoByHierarchyType(getContext().getOperator(),
//                project,
//                orgId,
//                entityId,
//                HierarchyType.PRESSURE_TEST_PACKAGE.name()
//            );
//        }
//
//
//        if (!LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnCp())
//            && !parentsPutDTO.getParentHierarchyIdOnCp().equals(isoHierarchyInfoEntity.getParentHierarchyIdOnCp())) {
//            hierarchyService.moveTo(
//                getContext().getOperator(),
//                projectService.get(orgId, projectId),
//                WBSEntityType.ISO.name(),
//                entityId,
//                null,
//                parentsPutDTO.getParentHierarchyIdOnCp()
//            );
//        } else if (LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnCp())) {
//            hierarchyService.deleteOnlyHierarchyInfoByHierarchyType(getContext().getOperator(),
//                project,
//                orgId,
//                entityId,
//                HierarchyType.CLEAN_PACKAGE.name()
//            );
//        }
//
//
//        if (!LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnSs())
//            && !parentsPutDTO.getParentHierarchyIdOnSs().equals(isoHierarchyInfoEntity.getParentHierarchyIdOnSs())) {
//            hierarchyService.moveTo(
//                getContext().getOperator(),
//                projectService.get(orgId, projectId),
//                WBSEntityType.ISO.name(),
//                entityId,
//                null,
//                parentsPutDTO.getParentHierarchyIdOnSs()
//            );
//        } else if (LongUtils.isEmpty(parentsPutDTO.getParentHierarchyIdOnSs())) {
//            hierarchyService.deleteOnlyHierarchyInfoByHierarchyType(getContext().getOperator(),
//                project,
//                orgId,
//                entityId,
//                HierarchyType.SUB_SYSTEM.name()
//            );
//        }




        isoEntityService.setIsoIdsAndWbs(projectId, entityId);

        return new JsonResponseBody();
    }

    /**
     * 取得管件图纸信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @Operation(summary = "取得管线图纸信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/iso-entities/{entityId}/drawing-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege()
    public JsonResponseBody getDrawingInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                           @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                           @PathVariable @Parameter(description = "实体 ID") Long entityId) {

        return new JsonListResponseBody<>(
            isoEntityService.getDrawingInfo(
                orgId,
                projectId,
                entityId,
                null
            )
        );
    }

    /**
     * 取得管线材料信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/iso-entities/{entityId}/material-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(summary = "取得管线材料信息")
    @WithPrivilege()
    @Override
    public JsonResponseBody getMaterialInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                            @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                            @PathVariable @Parameter(description = "实体 ID") Long entityId) {
        return new JsonListResponseBody<>(
            isoEntityService.getMaterialInfo(entityId, orgId, projectId)
        );
    }

    /**
     * 按条件下载管线实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/iso-entities/download"
    )
    @Operation(summary = "按条件下载管线实体列表")
    @WithPrivilege
    @Override
    public synchronized void downloadISOEntities(@PathVariable("orgId") Long orgId,
                                    @PathVariable("projectId") Long projectId,
                                    ISOEntryCriteriaDTO criteriaDTO) throws IOException {

        final ContextDTO context = getContext();
        final OperatorDTO operator = context.getOperator();
        File excel = isoEntityService.saveDownloadFile(orgId, projectId, criteriaDTO, operator.getId());

        HttpServletResponse response = context.getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-project-entities-iso.xlsx\""
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
    @Operation(
        summary = " 更新ISO节点材料匹配%",
        description = "更新ISO节点上 bom_ln_code 对应到SPM中的材料的匹配%"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/bom-match",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody spmMatchBom(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestParam @Parameter(description = "是否首次BOM匹配") Boolean initialMatchFlag
    ) {

        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId, version);
        if (initialMatchFlag == null) initialMatchFlag = false;

        Boolean finalInitialMatchFlag = initialMatchFlag;

        batchTaskService.run(
            getContext(), project, BOM_PERCENT_UPDATE,
            batchTask -> {
                BatchResultDTO result = isoEntityService.updateBomLnMatch(
                    batchTask,
                    operator,
                    project,
                    finalInitialMatchFlag
                );

                return result;
            }
        );

        return new JsonResponseBody();
    }


}
