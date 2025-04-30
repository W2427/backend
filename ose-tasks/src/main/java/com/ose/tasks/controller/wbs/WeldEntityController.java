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
import com.ose.tasks.api.wbs.WeldEntityAPI;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.wbs.piping.ISOEntityService;
import com.ose.tasks.domain.model.service.wbs.piping.WeldEntityInterface;
import com.ose.tasks.dto.WpsImportDTO;
import com.ose.tasks.dto.WpsImportResultDTO;
import com.ose.tasks.dto.material.NPSThicknessSearchDTO;
import com.ose.tasks.dto.wbs.*;
import com.ose.tasks.dto.wps.WpsCriteriaDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.material.MaterialCodeAliasGroup;
import com.ose.tasks.entity.material.NPSch;
import com.ose.tasks.entity.wbs.entity.WeldEntity;
import com.ose.tasks.entity.wbs.entity.WeldEntityBase;
import com.ose.tasks.entity.wbs.entity.WeldHierarchyInfoEntity;
import com.ose.tasks.entity.wps.Wps;
import com.ose.tasks.vo.ShopFieldType;
import com.ose.tasks.vo.qc.WPSMatchResult;
import com.ose.tasks.vo.qc.WeldType;
import com.ose.tasks.vo.wbs.WBSEntityType;
import com.ose.util.BeanUtils;
import com.ose.util.CollectionUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import io.swagger.annotations.Api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.Tuple;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Api(description = "焊口实体管理接口")
@RestController
public class WeldEntityController extends AbstractWBSEntityController implements WeldEntityAPI {


    private WeldEntityInterface weldEntityService;


    private ISOEntityService isoEntityService;


    private ProjectInterface projectService;


    private HierarchyInterface hierarchyService;


//    private WpsInterface wpsInterface;

    private static final String COMMA = ",";

    /**
     * 构造方法。
     */
    @Autowired
    public WeldEntityController(
        WeldEntityInterface weldEntityService,
        ISOEntityService isoEntityService,
        ProjectInterface projectService,
        HierarchyInterface hierarchyService
//        WpsInterface wpsInterface
    ) {
        this.weldEntityService = weldEntityService;
        this.isoEntityService = isoEntityService;
        this.projectService = projectService;
        this.hierarchyService = hierarchyService;
//        this.wpsInterface = wpsInterface;
    }

    @Override
    @Operation(summary = "查询焊口实体")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<? extends WeldEntityBase> searchWeldEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        WeldEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            weldEntityService.search(orgId, projectId, criteriaDTO, pageDTO)
        );
    }

    @Override
    @Operation(summary = "取得焊口实体详细信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<? extends WeldEntityBase> getWeldEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId
    ) {
        return new JsonObjectResponseBody<>(
            weldEntityService.get(orgId, projectId, entityId)
        );
    }

    @Override
    @Operation(summary = "删除焊口实体")
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody deleteWeldEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version
    ) {

        Project project = projectService.get(orgId, projectId, version);


/*        if (wbsEntityService.isInWorkFlow(entityId, projectId, orgId)) {
            throw new BusinessError("",
                "business-error: WELD_JOINT is ALREADY in work flow, CAN'T delete.");
        }*/

        weldEntityService.delete(
            getContext().getOperator(),
            orgId,
            project,
            entityId
        );


        return new JsonResponseBody();
    }

    @Override
    @Operation(summary = "插入焊口实体")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody addWeldEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Parameter(description = "焊口实体信息") WeldEntryInsertDTO weldEntryInsertDTO
    ) {

        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId);

        if (!isoEntityService.existsByEntityNo(weldEntryInsertDTO.getIsoNo(), projectId)) {
            throw new BusinessError("", "business-error: iso no does NOT exist.");
        }

        String weldNo = weldEntryInsertDTO.getIsoNo()
            + "-W"
            + StringUtils.padLeft(StringUtils.trim(weldEntryInsertDTO.getDisplayName()), 4, '0');

        if (weldEntityService.existsByEntityNo(weldNo, projectId)) {
            throw new BusinessError("", "business-error: weld no ALREADY EXISTS.");
        }

        WeldEntity weldEntity = new WeldEntity(project);
        BeanUtils.copyProperties(weldEntryInsertDTO, weldEntity);

        weldEntity.setNo(weldNo);

        weldEntity.setDisplayName(weldEntryInsertDTO.getDisplayName());

        weldEntity.setThickness(weldEntryInsertDTO.getThickness());

        weldEntity.setRemarks(weldEntryInsertDTO.getRemarks());


        weldEntity.setNps(weldEntryInsertDTO.getNpsText());


        if (!ShopFieldType.FFW.name().equalsIgnoreCase(weldEntity.getShopField())) {
            if (StringUtils.isEmpty(weldEntity.getMaterial2()) || StringUtils.isEmpty(weldEntity.getMaterialCode2())) {
                throw new BusinessError("", "Component#2 Material&Code CAN'T be empty.");
            }
        }

        weldEntityService.insert(operator, orgId, projectId, weldEntity);



        weldEntityService.setIsoIdsAndWbs(projectId, weldEntity.getId());



        return new JsonResponseBody(getContext());
    }

    @Override
    @Operation(summary = "更新焊口实体")
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonObjectResponseBody<WeldEntityBase> updateWeldEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody @Parameter(description = "焊口实体信息") WeldEntryUpdateDTO weldEntryUpdateDTO
    ) {

        final OperatorDTO operator = getContext().getOperator();
        projectService.get(orgId, projectId, version);

        WeldEntityBase weldEntityBase = weldEntityService.get(orgId, projectId, entityId);
        WeldEntity weldEntity = BeanUtils.copyProperties(weldEntityBase, new WeldEntity());
        BeanUtils.copyProperties(weldEntryUpdateDTO, weldEntity);


        weldEntity.setThickness(weldEntryUpdateDTO.getThickness());

        weldEntity.setRemarks(weldEntryUpdateDTO.getRemarks());


        weldEntity.setNps(weldEntryUpdateDTO.getNpsText());


        if (!ShopFieldType.FFW.name().equalsIgnoreCase(weldEntity.getShopField())) {
            if (StringUtils.isEmpty(weldEntity.getMaterial2()) || StringUtils.isEmpty(weldEntity.getMaterialCode2())) {
                throw new BusinessError("", "Component#2 Material&Code CAN'T be empty.");
            }
        }

        return new JsonObjectResponseBody<>(
            getContext(),
            weldEntityService.update(operator, orgId, projectId, weldEntity)
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
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities/{entityId}/hierarchy-parents",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody moveWeld(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam("version") long version,
        @RequestBody ParentsPutDTO parentsPutDTO
    ) {
        Project project = projectService.get(orgId, projectId, version);

        if (!LongUtils.isEmpty(parentsPutDTO.getParentSpoolHierarchyId())
            && !LongUtils.isEmpty(parentsPutDTO.getParentIsoHierarchyId())) {
            throw new BusinessError("焊口只能指定一个父级");
        }

        if (!LongUtils.isEmpty(parentsPutDTO.getParentSpoolHierarchyId())) {
            validateParentHierarchyNode(
                hierarchyService.get(orgId, projectId, parentsPutDTO.getParentSpoolHierarchyId()),
                WBSEntityType.SPOOL.name()
            );
        } else if (!LongUtils.isEmpty(parentsPutDTO.getParentIsoHierarchyId())) {
            validateParentHierarchyNode(
                hierarchyService.get(orgId, projectId, parentsPutDTO.getParentIsoHierarchyId()),
                WBSEntityType.ISO.name()
            );
        }




        WeldHierarchyInfoEntity weldHierarchyInfoEntity
            = (WeldHierarchyInfoEntity) weldEntityService.get(orgId,
            projectId,
            entityId);

//        if (!LongUtils.isEmpty(parentsPutDTO.getParentSpoolHierarchyId())
//            && !parentsPutDTO.getParentSpoolHierarchyId().equals(weldHierarchyInfoEntity.getParentHierarchyIdOnPiping())) {
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
//                        WBSEntityType.WELD_JOINT.name(),
//                        entityId,
//                        WBSEntityType.SPOOL.name(),
//                        (Long) hierarchyNode.get("id")
//                    );
//                }
//            }
//        } else if (!LongUtils.isEmpty(parentsPutDTO.getParentIsoHierarchyId())
//            && !parentsPutDTO.getParentIsoHierarchyId().equals(weldHierarchyInfoEntity.getParentHierarchyIdOnPiping())) {
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
//                        WBSEntityType.WELD_JOINT.name(),
//                        entityId,
//                        WBSEntityType.ISO.name(),
//                        (Long) hierarchyNode.get("id")
//                    );
//                }
//            }
//        } else if (LongUtils.isEmpty(parentsPutDTO.getParentIsoHierarchyId())
//            && LongUtils.isEmpty(parentsPutDTO.getParentSpoolHierarchyId())) {
//
//            hierarchyService.deleteAllHierarchyInfoByEntityId(getContext().getOperator(),
//                project,
//                orgId,
//                entityId);
//        }




        weldEntityService.setIsoIdsAndWbs(projectId, entityId);

        return new JsonResponseBody();
    }

    /**
     * 取得焊口已设定WPS信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities/{entityId}/already-set-wps-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @Operation(summary = "取得焊口已设定的WPS信息")
    @WithPrivilege
    public JsonResponseBody getAlreadySetWPSInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                                 @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                 @PathVariable @Parameter(description = "实体 ID") Long entityId) {


        return new JsonListResponseBody<>(
            getContext(),
            weldEntityService.getAlreadySetWPSInfo(entityId, orgId, projectId)
        );
    }

    /**
     * 取得焊口匹配的可选WPS信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities/{entityId}/optional-wps-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @Operation(summary = "取得焊口匹配的可选WPS信息")
    @WithPrivilege
    public JsonResponseBody getOptionalWPSInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                               @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                               @PathVariable @Parameter(description = "实体 ID") Long entityId) {
        WeldEntityBase entity = weldEntityService.get(orgId, projectId, entityId);

        Page<Wps> wpsPage = null;

        if (entity == null) {
            throw new BusinessError("", "business-error:WELD is Empty.");
        }


        if (entity.getMaterial1().equals("") || entity.getWeldType().equals("")) {
            throw new BusinessError("", "business-error:WELD Material is Empty.");
        }

        WpsCriteriaDTO wpsCriteriaDTO = new WpsCriteriaDTO();
        wpsCriteriaDTO.setBaseMetal(entity.getMaterial1());

        wpsCriteriaDTO.setBaseMetal2(entity.getMaterial2());

        wpsCriteriaDTO.setWeldType(entity.getWeldType());

        if (entity.getWeldType().equals(WeldType.BW.name())) {
            NPSThicknessSearchDTO searchDTO = new NPSThicknessSearchDTO();
            searchDTO.setNps(entity.getNpsText());
            searchDTO.setNpsValue(String.valueOf(entity.getNps()));
            searchDTO.setThicknessSch(entity.getThickness());
//            NPSch npSch = npsInterface.searchThickness(orgId, projectId, searchDTO);


//            if (npSch != null) {
//
//
//                wpsCriteriaDTO.setMaxDiaRange(String.valueOf(entity.getNps()));
//                wpsCriteriaDTO.setContainMaxDiaRange("true");
//                wpsCriteriaDTO.setMinDiaRange(String.valueOf(entity.getNps()));
//                wpsCriteriaDTO.setContainMinDiaRange("true");
//                wpsCriteriaDTO.setMaxThickness(String.valueOf(npSch.getThickness()));
//                wpsCriteriaDTO.setContainMaxThickness("true");
//                wpsCriteriaDTO.setMinThickness(String.valueOf(npSch.getThickness()));
//                wpsCriteriaDTO.setContainMinThickness("true");
//                wpsPage = wpsInterface.search(orgId, projectId, wpsCriteriaDTO);
//            }
//        } else {
//            wpsPage = wpsInterface.search(orgId, projectId, wpsCriteriaDTO);
//        }
//        if (wpsPage == null || !wpsPage.hasContent()) {
//            throw new BusinessError("", "business-error: WPS info does NOT exist.");
        }

        return new JsonListResponseBody<>(wpsPage);
    }

    /**
     * 匹配单个焊口的WPS信息。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param entityId    实体ID
     * @param wpsInfoDTOS WPS信息
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities/{entityId}/wps-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @Operation(summary = "匹配单个焊口的WPS信息")
    @WithPrivilege
    public JsonResponseBody setWPSInfo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestBody List<WPSInfoDTO> wpsInfoDTOS
    ) {
        if (wpsInfoDTOS == null || wpsInfoDTOS.size() == 0) {
            throw new BusinessError("未指定 WPS 信息");
        }

        List<Long> wpsIds = new ArrayList<>();
        List<String> wpsNos = new ArrayList<>();

        for (WPSInfoDTO wpsInfo : wpsInfoDTOS) {
            wpsIds.add(wpsInfo.getWpsId());
            wpsNos.add(wpsInfo.getWpsNo());
        }

        WeldEntityBase entity = weldEntityService.get(orgId, projectId, entityId);
        WeldEntity weldEntity = BeanUtils.copyProperties(entity, new WeldEntity());
        weldEntity.setWpsNo(org.apache.commons.lang.StringUtils.join(wpsNos.toArray(), COMMA));
        weldEntity.setWpsId(org.apache.commons.lang.StringUtils.join(wpsIds.toArray(), COMMA));
        weldEntity.setWpsEdited(true);

        weldEntityService.update(getContext().getOperator(), orgId, projectId, weldEntity);

        WeldWPSMatchResultDTO weldWPSMatchResultDTO = new WeldWPSMatchResultDTO();
        weldWPSMatchResultDTO.setWeldMatchDTOList(
            Collections.singletonList(BeanUtils.copyProperties(weldEntity, new WeldMatchDTO()))
        );
        weldWPSMatchResultDTO.setSuccessCount(1);

        return new JsonObjectResponseBody<>(weldWPSMatchResultDTO);
    }

    /**
     * 批量匹配焊口的WPS信息。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param weldMatchDTOS 焊口实体
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities/wps-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @Operation(summary = "批量匹配焊口的WPS信息")
    @WithPrivilege
    public JsonResponseBody setWPSInfos(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody List<WeldMatchDTO> weldMatchDTOS
    ) {
        WeldWPSMatchResultDTO weldWPSMatchResultDTO = new WeldWPSMatchResultDTO();
        List<WeldMatchDTO> weldMatchDTOList = new ArrayList<>();
        int failureCount = 0;
        int successCount = 0;
        int skipCount = 0;
        if (weldMatchDTOS != null && weldMatchDTOS.size() != 0) {
            for (WeldMatchDTO weldMatchDTOIn : weldMatchDTOS) {


                if (weldMatchDTOIn.getWpsEdited() == null) weldMatchDTOIn.setWpsEdited(false);
                if (weldMatchDTOIn.getWpsEdited()) {
                    WeldMatchDTO weldMatchDTOOut = BeanUtils.copyProperties(weldMatchDTOIn, new WeldMatchDTO());
                    weldMatchDTOOut.setResult(WPSMatchResult.SKIP);
                    weldMatchDTOList.add(weldMatchDTOOut);
                    skipCount++;
                    continue;
                }

                NPSThicknessSearchDTO searchDTO = new NPSThicknessSearchDTO();
                searchDTO.setNps(weldMatchDTOIn.getNpsText());
                searchDTO.setNpsValue(String.valueOf(weldMatchDTOIn.getNps()));
                searchDTO.setThicknessSch(weldMatchDTOIn.getThickness());
//                NPSch npSch = npsInterface.searchThickness(orgId, projectId, searchDTO);

//                if (npSch == null) {
//                    WeldMatchDTO weldMatchDTOOut = BeanUtils.copyProperties(weldMatchDTOIn, new WeldMatchDTO());
//                    weldMatchDTOOut.setResult(WPSMatchResult.FAILURE);
//                    weldMatchDTOList.add(weldMatchDTOOut);
//                    failureCount++;
//                } else {
//                    WpsCriteriaDTO wpsCriteriaDTO = new WpsCriteriaDTO();
//
//                    wpsCriteriaDTO.setBaseMetal(weldMatchDTOIn.getMaterial1());
//                    wpsCriteriaDTO.setBaseMetal2(weldMatchDTOIn.getMaterial2());
//                    wpsCriteriaDTO.setMaxDiaRange(String.valueOf(weldMatchDTOIn.getNps()));
//                    wpsCriteriaDTO.setContainMaxDiaRange("true");
//                    wpsCriteriaDTO.setMinDiaRange(String.valueOf(weldMatchDTOIn.getNps()));
//                    wpsCriteriaDTO.setContainMinDiaRange("true");
//                    wpsCriteriaDTO.setMaxThickness(String.valueOf(npSch.getThickness()));
//                    wpsCriteriaDTO.setContainMaxThickness("true");
//                    wpsCriteriaDTO.setMinThickness(String.valueOf(npSch.getThickness()));
//                    wpsCriteriaDTO.setContainMinThickness("true");
//                    wpsCriteriaDTO.setWeldType(weldMatchDTOIn.getWeldType());
//
////                    List<Wps> wpsList = wpsInterface.findAll(orgId, projectId, wpsCriteriaDTO);
//                    List<Wps> wpsList = new ArrayList<>();
//                    List<Long> wpsIds = new ArrayList<>();
//                    List<String> wpsNos = new ArrayList<>();
//                    WeldEntityBase weldEntityIn = weldEntityService.get(orgId, projectId, weldMatchDTOIn.getId());
//                    WeldEntity weldEntity = BeanUtils.copyProperties(weldEntityIn, new WeldEntity());
//                    if (wpsList != null && wpsList.size() != 0) {
//                        for (Wps wps : wpsList) {
//                            wpsIds.add(wps.getId());
//                            wpsNos.add(wps.getCode());
//                        }
//                    } else {
//                        WeldMatchDTO weldMatchDTOOut = BeanUtils.copyProperties(weldEntityIn, new WeldMatchDTO());
//                        weldMatchDTOOut.setResult(WPSMatchResult.FAILURE);
//                        weldMatchDTOList.add(weldMatchDTOOut);
//                        failureCount++;
//                        continue;
//                    }
//
//                    if (wpsIds.size() > 5) {
//                        wpsIds = wpsIds.subList(0, 5);
//                        wpsNos = wpsNos.subList(0, 5);
//                    }
//                    weldEntity.setWpsNo(org.apache.commons.lang.StringUtils.join(wpsNos.toArray(), COMMA));
//                    weldEntity.setWpsId(org.apache.commons.lang.StringUtils.join(wpsIds.toArray(), COMMA));
//                    weldEntityService.update(getContext().getOperator(), orgId, projectId, weldEntity);
//                    WeldMatchDTO weldMatchDTOOut = BeanUtils.copyProperties(weldEntity, new WeldMatchDTO());
//                    weldMatchDTOOut.setResult(WPSMatchResult.SUCCESS);
//                    weldMatchDTOList.add(weldMatchDTOOut);
//                    successCount++;
//                }
            }
        }
        weldWPSMatchResultDTO.setWeldMatchDTOList(weldMatchDTOList);
        weldWPSMatchResultDTO.setFailureCount(failureCount);
        weldWPSMatchResultDTO.setSkipCount(skipCount);
        weldWPSMatchResultDTO.setSuccessCount(successCount);

        return new JsonObjectResponseBody<>(weldWPSMatchResultDTO);
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
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities/download"
    )
    @Operation(summary = "按条件下载焊口实体列表")
    @WithPrivilege
    @Override
    public synchronized void downloadWeldEntities(@PathVariable("orgId") Long orgId,
                                     @PathVariable("projectId") Long projectId,
                                     WeldEntryCriteriaDTO criteriaDTO) throws IOException {
        final OperatorDTO operator = getContext().getOperator();
        File excel = weldEntityService.saveDownloadFile(orgId, projectId, criteriaDTO, operator.getId());

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-project-entities-weld.xlsx\""
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

    /**
     * 导入WPS
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param uploadDTO
     */
    @Override
    @Operation(
        summary = "上传WPS更新清单文件",
        description = "请先通过文档管理服务的 <code>POST /orgs/{orgId}/wps-update-file</code> 接口将导入文件上传到服务器，"
            + "xls 的 三列是 ISO weldNo WPS，数据从第二行开始"
    )
    @RequestMapping(method = POST, value = "/orgs/{orgId}/projects/{projectId}/weld-entities/import-wps",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<WpsImportResultDTO> importUpdateWps(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody WpsImportDTO uploadDTO) {

        Long userId = getContext().getOperator().getId();

        WpsImportResultDTO dto = weldEntityService.updateWeldEntityWps(orgId, projectId, userId, uploadDTO);

        return new JsonObjectResponseBody<>(dto);

    }

    /**
     * 取得焊口和焊工共同的WPS列表。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities/welder/wps",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @Operation(summary = "取得焊口和焊工共同的WPS列表")
    @WithPrivilege
    public JsonResponseBody getWelderWps(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        WeldWelderWPSDTO weldWelderWPSDTO) {


        return new JsonListResponseBody<>(
            getContext(),
            weldEntityService.getWelderWps(orgId, projectId, weldWelderWPSDTO)
        );
    }

}
