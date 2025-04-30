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
import com.ose.tasks.api.wbsStructure.Wp05EntityAPI;
import com.ose.tasks.controller.wbs.AbstractWBSEntityController;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.wbs.WBSEntityInterface;
import com.ose.tasks.domain.model.service.wbs.structure.Wp05EntityInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.structureWbs.Wp05EntryCriteriaDTO;
import com.ose.tasks.dto.structureWbs.Wp05EntryInsertDTO;
import com.ose.tasks.dto.structureWbs.Wp05EntryUpdateDTO;
import com.ose.tasks.dto.wbs.ParentsPutDTO;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.structureEntity.Wp05Entity;
import com.ose.tasks.entity.wbs.structureEntity.Wp05EntityBase;
import com.ose.tasks.entity.wbs.structureEntity.Wp05HierarchyInfoEntity;
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

import static com.ose.tasks.vo.setting.BatchTaskCode.BOM_PERCENT_UPDATE;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Api(description = "结构零件实体管理接口")
@RestController
public class Wp05EntityController extends AbstractWBSEntityController implements Wp05EntityAPI {


    private Wp05EntityInterface wp05EntityService;


    private WBSEntityInterface wbsEntityService;


    private ProjectInterface projectService;


    private HierarchyInterface hierarchyService;


    private final BatchTaskInterface batchTaskService;

    /**
     * 构造方法。
     */
    @Autowired
    public Wp05EntityController(
        Wp05EntityInterface wp05EntityService,
        ProjectInterface projectService,
        HierarchyInterface hierarchyService,
        WBSEntityInterface wbsEntityService,
        BatchTaskInterface batchTaskService
    ) {
        this.wp05EntityService = wp05EntityService;
        this.projectService = projectService;
        this.hierarchyService = hierarchyService;
        this.wbsEntityService = wbsEntityService;
        this.batchTaskService = batchTaskService;
    }

    @Override
    @Operation(
        summary = "查询wp05实体",
        description = "查询wp05实体"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp05-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<? extends Wp05EntityBase> searchWp05Entities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        Wp05EntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            wp05EntityService.search(orgId, projectId, criteriaDTO, pageDTO)
        );
    }

    @Override
    @Operation(
        summary = "取得wp05实体详细信息%",
        description = "取得wp05实体详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp05-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<? extends Wp05EntityBase> getWp05Entity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId
    ) {
        return new JsonObjectResponseBody<>(
            wp05EntityService.get(orgId, projectId, entityId)
        );
    }

    @Override
    @Operation(
        summary = "删除实体",
        description = "删除实体"
    )
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/wp05-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody deleteWp05Entity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version
    ) {


        Project project = projectService.get(orgId, projectId, version);


        if (wbsEntityService.isInWorkFlow(entityId, projectId, orgId)) {
            throw new BusinessError("",
                "business-error: Wp05 is ALREADY in work flow, CAN'T delete.");
        }


        wp05EntityService.delete(
            getContext().getOperator(),
            orgId,
            project,
            entityId
        );

        return new JsonResponseBody(getContext());
    }

    @Override
    @Operation(
        summary = "插入wp05实体",
        description = "插入wp05实体"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/wp05-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody addWp05Entity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Parameter(description = "wp05实体信息") Wp05EntryInsertDTO wp05EntryInsertDTO
    ) {
        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId);










        Wp05Entity wp05Entity = new Wp05Entity(project);
        BeanUtils.copyProperties(wp05EntryInsertDTO, wp05Entity);


        wp05Entity.setDisplayName(wp05EntryInsertDTO.getNo());
        wp05EntityService.insert(operator, orgId, projectId, wp05Entity);
        return new JsonResponseBody(getContext());
    }

    @Override
    @Operation(
        summary = "更新wp05实体",
        description = "更新wp05实体"
    )
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/wp05-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody updateWp05Entity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody @Parameter(description = "wp05实体信息") Wp05EntryUpdateDTO wp05EntryUpdateDTO
    ) {
        final OperatorDTO operator = getContext().getOperator();
        projectService.get(orgId, projectId, version);
        Wp05EntityBase wp05Entity = wp05EntityService.get(orgId, projectId, entityId);
        Wp05Entity wp05EntityCommon = BeanUtils.copyProperties(wp05Entity, new Wp05Entity());







        if (!wp05EntityCommon.getNo().equals(wp05EntryUpdateDTO.getNo()) && wp05EntityService.existsByEntityNo(wp05EntryUpdateDTO.getNo(), projectId)) {
            throw new BusinessError("", "business-error: part  ALREADY EXISTS.");
        }





        BeanUtils.copyProperties(wp05EntryUpdateDTO, wp05EntityCommon);


        wp05EntityCommon.setDisplayName(wp05EntryUpdateDTO.getNo());
        Wp05EntityBase entityResult = wp05EntityService.update(operator, orgId, projectId, wp05EntityCommon);
        return new JsonObjectResponseBody<>(getContext(), entityResult);
    }

    @Override
    @Operation(
        summary = "更新父级信息",
        description = "更新父级信息"
    )
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/wp05-entities/{entityId}/hierarchy-parents",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody moveWp05(
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



            Wp05HierarchyInfoEntity wp05HierarchyInfoEntity =
                (Wp05HierarchyInfoEntity) wp05EntityService.get(orgId, projectId, entityId);
            hierarchyService.moveTo(
                getContext().getOperator(),
                projectService.get(orgId, projectId),
                wp05HierarchyInfoEntity.getEntityType(),
                entityId,
                wp05HierarchyInfoEntity.getEntityType(),
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
        summary = "取得wp05图纸信息",
        description = "取得wp05图纸信息"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp05-entities/{entityId}/drawing-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege()
    public JsonResponseBody getDrawingInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                           @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                           @PathVariable @Parameter(description = "实体 ID") Long entityId) {

        return new JsonListResponseBody<>(
            wp05EntityService.getDrawingInfo(
                orgId,
                projectId,
                entityId,
                null
            )
        );
    }

    /**
     * 取得wp05材料信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp05-entities/{entityId}/material-info",
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "取得wp05材料信息",
        description = "取得wp05材料信息"
    )
    @WithPrivilege()
    @Override
    public JsonResponseBody getMaterialInfo(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                            @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                            @PathVariable @Parameter(description = "实体 ID") Long entityId) {
        return new JsonListResponseBody<>(
            wp05EntityService.getMaterialInfo(entityId, orgId, projectId)
        );
    }

    /**
     * 按条件下载wp05实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp05-entities/download"
    )
    @Operation(
        summary = "按条件下载wp05实体列表",
        description = "按条件下载wp05实体列表"
    )
    @WithPrivilege
    @Override
    public synchronized void downloadWp05Entities(@PathVariable("orgId") Long orgId,
                                     @PathVariable("projectId") Long projectId,
                                     Wp05EntryCriteriaDTO criteriaDTO) throws IOException {

        final OperatorDTO operator = getContext().getOperator();
        File excel = wp05EntityService.saveDownloadFile(orgId, projectId, criteriaDTO, operator.getId());

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-project-entities-wp05.xlsx\""
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
        summary = "更新Wp05节点材料匹配%",
        description = "更新Wp05节点上 bom_ln_code 对应到SPM中的材料的匹配%"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp05-bom-match",
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
                BatchResultDTO result = wp05EntityService.updateBomLnMatch(
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
