package com.ose.tasks.controller.material;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.material.FMaterialStructureNestAPI;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.material.FMaterialStructureNestInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.material.FMaterialStructureNestDTO;
import com.ose.tasks.dto.material.FMaterialStructureNestImportDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.material.FMaterialRequisitionEntity;
import com.ose.tasks.entity.material.FMaterialStructureNest;
import com.ose.tasks.vo.setting.BatchTaskStatus;
import com.ose.util.LongUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.ose.tasks.vo.setting.BatchTaskCode.PROJECT_STRUCTURE_NEST_IMPORT;
import static com.ose.tasks.vo.setting.BatchTaskCode.PROJECT_STRUCTURE_NEST_UPDATE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Api(description = "结构套料方案接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class FMaterialStructureNestController extends BaseController implements FMaterialStructureNestAPI {

    private final FMaterialStructureNestInterface fMaterialStructureNestService;


    private final ProjectInterface projectService;


    private final BatchTaskInterface batchTaskService;

    private UploadFeignAPI uploadFeignAPI;

    /**
     * 构造方法
     */
    @Autowired
    public FMaterialStructureNestController(
        FMaterialStructureNestInterface fMaterialStructureNestService,
        ProjectInterface projectService,
        BatchTaskInterface batchTaskService,
        UploadFeignAPI uploadFeignAPI
    ) {
        this.fMaterialStructureNestService = fMaterialStructureNestService;
        this.projectService = projectService;
        this.batchTaskService = batchTaskService;
        this.uploadFeignAPI = uploadFeignAPI;

    }

    /**
     * 创建结构套料方案。
     *
     * @param orgId                     组织id
     * @param projectId                 项目id
     * @param fMaterialStructureNestDTO 传输对象
     * @return
     */
    @Override
    @ApiOperation(value = "创建结构套料方案", notes = "创建结构套料方案。")
    @WithPrivilege
    @RequestMapping(method = POST, value = "material-structure-nest")
    @ResponseStatus(CREATED)
    public JsonResponseBody create(@PathVariable @ApiParam("组织id") Long orgId,
                                   @PathVariable @ApiParam("项目id") Long projectId,
                                   @RequestBody @ApiParam("套料方案信息") FMaterialStructureNestDTO fMaterialStructureNestDTO) {
        ContextDTO context = getContext();
        OperatorDTO operatorDTO = context.getOperator();
        fMaterialStructureNestService.create(orgId, projectId, fMaterialStructureNestDTO, context, operatorDTO);
        return new JsonResponseBody();

    }

    /**
     * 结构套料方案列表。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param pageDTO   分页参数
     * @return
     */
    @RequestMapping(method = GET, value = "material-structure-nest")
    @ApiOperation(value = "结构套料方案列表", notes = "结构套料方案列表。")
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<FMaterialStructureNest> search(@PathVariable @ApiParam("orgId") Long orgId,
                                                               @PathVariable @ApiParam("项目Id") Long projectId,
                                                               PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            fMaterialStructureNestService.search(
                orgId,
                projectId,
                pageDTO
            )
        ).setIncluded(fMaterialStructureNestService);
    }

    /**
     * 结构套料方案详情。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 结构套料方案id
     * @return
     */
    @RequestMapping(method = GET, value = "material-structure-nest/{fMaterialStructureNestId}", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "结构套料方案详情", notes = "结构套料方案详情")
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<FMaterialStructureNest> detail(@PathVariable @ApiParam("orgId") Long orgId,
                                                                 @PathVariable @ApiParam("项目Id") Long projectId,
                                                                 @PathVariable @ApiParam("结构套料方案id") Long fMaterialStructureNestId
    ) {
        return new JsonObjectResponseBody<>(
            fMaterialStructureNestService.detail(
                orgId,
                projectId,
                fMaterialStructureNestId
            )
        ).setIncluded(fMaterialStructureNestService);
    }

    /**
     * 更新结构套料方案。
     *
     * @param orgId                     组织id
     * @param projectId                 项目id
     * @param fMaterialStructureNestId  结构套料方案id
     * @param fMaterialStructureNestDTO 传输对象
     * @return
     */
    @Override
    @ApiOperation(value = "更新结构套料方案", notes = "更新结构套料方案。")
    @WithPrivilege
    @RequestMapping(method = POST, value = "material-structure-nest/{fMaterialStructureNestId}")
    @ResponseStatus(OK)
    public JsonResponseBody update(@PathVariable @ApiParam("组织id") Long orgId,
                                   @PathVariable @ApiParam("项目id") Long projectId,
                                   @PathVariable @ApiParam("结构套料方案id") Long fMaterialStructureNestId,
                                   @RequestBody @ApiParam("结构套料方案") FMaterialStructureNestDTO fMaterialStructureNestDTO) {
        ContextDTO context = getContext();
        OperatorDTO operatorDTO = context.getOperator();
        fMaterialStructureNestService.update(orgId, projectId, fMaterialStructureNestId, fMaterialStructureNestDTO, context, operatorDTO);
        return new JsonResponseBody();

    }

    /**
     * 删除结构套料方案。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 结构套料方案id
     * @return
     */
    @Override
    @ApiOperation(value = "删除结构套料方案", notes = "删除结构套料方案。")
    @WithPrivilege
    @RequestMapping(method = DELETE, value = "material-structure-nest/{fMaterialStructureNestId}")
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fMaterialStructureNestId") Long fMaterialStructureNestId
    ) {
        ContextDTO context = getContext();
        OperatorDTO operatorDTO = context.getOperator();
        fMaterialStructureNestService.delete(orgId, projectId, fMaterialStructureNestId, operatorDTO);
        return new JsonResponseBody();
    }

    /**
     * 结构套料导入。
     *
     * @param orgId                           组织id
     * @param projectId                       项目id
     * @param fMaterialStructureNestId        结构套料方案id
     * @param version                         项目版本
     * @param fMaterialStructureNestImportDTO 导入文件
     * @return
     */
    @Override
    @ApiOperation(
        value = "导入结构套料",
        notes = "请先通过文档管理服务的 <code>POST /orgs/{orgId}/import-project-structure-nest</code> 接口将导入文件上传到服务器，"
            + "导入文件请参照 <a href=\"/samples/import-project-entities.xlsx\">import-structure-nesting-template.xlsx</a>。"
    )
    @WithPrivilege
    @SetUserInfo
    @RequestMapping(
        method = POST,
        value = "material-structure-nest/{fMaterialStructureNestId}/import",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    public JsonResponseBody importStructureNest(
        @PathVariable @ApiParam("所属组织 ID") Long orgId,
        @PathVariable @ApiParam("项目 ID") Long projectId,
        @PathVariable @ApiParam("结构套料方案 Id") Long fMaterialStructureNestId,
        @RequestParam @ApiParam("项目更新版本号") long version,
        @RequestBody FMaterialStructureNestImportDTO fMaterialStructureNestImportDTO
    ) {

        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId, version);

        batchTaskService.run(
            getContext(), project, PROJECT_STRUCTURE_NEST_IMPORT,
            batchTask -> {

                BatchResultDTO result = fMaterialStructureNestService.importStructureNest(
                    orgId,
                    projectId,
                    fMaterialStructureNestId,
                    batchTask,
                    operator,
                    project,
                    fMaterialStructureNestImportDTO
                );
                JsonObjectResponseBody<FileES> responseBody
                    = uploadFeignAPI.save(
                    orgId.toString(),
                    projectId.toString(),
                    fMaterialStructureNestImportDTO.getFileName(),
                    new FilePostDTO()
                );

                batchTask.setImportFile(
                    LongUtils.parseLong(responseBody.getData().getId()
                    ));

                return result;
            }
        );

        return new JsonResponseBody();
    }

    /**
     * 创建结构套料方案流程。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 结构套料方案id
     * @return
     */
    @Override
    @ApiOperation(value = "创建结构套料方案流程", notes = "创建结构套料方案流程。")
    @WithPrivilege
    @RequestMapping(method = POST, value = "material-structure-nest/{fMaterialStructureNestId}/activity")
    @ResponseStatus(OK)
    public JsonResponseBody createActivity(@PathVariable @ApiParam("组织id") Long orgId,
                                           @PathVariable @ApiParam("项目id") Long projectId,
                                           @PathVariable @ApiParam("结构套料方案id") Long fMaterialStructureNestId) {
        ContextDTO context = getContext();
        OperatorDTO operatorDTO = context.getOperator();
        fMaterialStructureNestService.createActivity(orgId, projectId, fMaterialStructureNestId, context, operatorDTO);
        return new JsonResponseBody();

    }

    /**
     * 修改保存结构套料方案流程。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 结构套料方案id
     * @return
     */
    @Override
    @ApiOperation(value = "修改保存结构套料方案流程", notes = "修改保存结构套料方案流程。")
    @WithPrivilege
    @RequestMapping(method = POST, value = "material-structure-nest/{fMaterialStructureNestId}/activity/modify")
    @ResponseStatus(OK)
    public JsonResponseBody changeActivityStatus(
        @PathVariable @ApiParam("组织id") Long orgId,
        @PathVariable @ApiParam("项目id") Long projectId,
        @PathVariable @ApiParam("结构套料方案id") Long fMaterialStructureNestId,
        @RequestParam @ApiParam("项目更新版本号") long version) {
        OperatorDTO operatorDTO = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId, version);
        batchTaskService.run(
            getContext(), project, PROJECT_STRUCTURE_NEST_UPDATE,
            batchTask -> {
                BatchResultDTO result = fMaterialStructureNestService.changeActivityStatus(
                    orgId,
                    projectId,
                    fMaterialStructureNestId,
                    operatorDTO,
                    batchTask
                );
                if (result.getErrorCount()>0){
                    batchTask.setLastModifiedAt(new Date());
                    batchTask.setStatus(BatchTaskStatus.FAILED);
                }else{
                    batchTask.setLastModifiedAt(new Date());
                    batchTask.setStatus(BatchTaskStatus.FINISHED);
                }
                return result;
            }
        );
        return new JsonResponseBody();

    }

    /**
     * 关联领料单。
     *
     * @param orgId                     组织id
     * @param projectId                 项目id
     * @param fMaterialStructureNestId  结构套料方案id
     * @param fMaterialStructureNestDTO 传输对象
     * @return
     */
    @Override
    @ApiOperation(value = "关联领料单", notes = "关联领料单。")
    @WithPrivilege
    @RequestMapping(method = POST, value = "material-structure-nest/{fMaterialStructureNestId}/material-requisitions")
    @ResponseStatus(OK)
    public JsonResponseBody saveMaterialRequisitions(@PathVariable @ApiParam("组织id") Long orgId,
                                                     @PathVariable @ApiParam("项目id") Long projectId,
                                                     @PathVariable @ApiParam("结构套料方案id") Long fMaterialStructureNestId,
                                                     @RequestBody @ApiParam("领料单信息") FMaterialStructureNestDTO fMaterialStructureNestDTO) {
        ContextDTO context = getContext();
        OperatorDTO operatorDTO = context.getOperator();
        fMaterialStructureNestService.saveMaterialRequisitions(orgId, projectId, fMaterialStructureNestId, fMaterialStructureNestDTO, context, operatorDTO);
        return new JsonResponseBody();

    }

    /**
     * 查找领料单详情。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 结构套料方案id
     * @return
     */
    @RequestMapping(method = GET, value = "material-structure-nest/{fMaterialStructureNestId}/material-requisitions", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查找领料单详情", notes = "查找领料单详情")
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<FMaterialRequisitionEntity> findMaterialRequisitionsByEntityId(@PathVariable @ApiParam("orgId") Long orgId,
                                                                                                 @PathVariable @ApiParam("项目Id") Long projectId,
                                                                                                 @PathVariable @ApiParam("结构套料方案id") Long fMaterialStructureNestId
    ) {
        ContextDTO context = getContext();
        OperatorDTO operatorDTO = context.getOperator();
        return new JsonObjectResponseBody<>(
            fMaterialStructureNestService.findMaterialRequisitionsByEntityId(
                orgId,
                projectId,
                fMaterialStructureNestId,
                context,
                operatorDTO
            )
        ).setIncluded(fMaterialStructureNestService);
    }

    /**
     * 设置分包商。
     *
     * @param orgId                     组织id
     * @param projectId                 项目id
     * @param fMaterialStructureNestId  结构套料方案id
     * @param fMaterialStructureNestDTO 传输对象
     * @return
     */
    @Override
    @ApiOperation(value = "设置分包商", notes = "设置分包商。")
    @WithPrivilege
    @RequestMapping(method = POST, value = "material-structure-nest/{fMaterialStructureNestId}/subcontractor")
    @ResponseStatus(OK)
    public JsonResponseBody setSubcontractor(@PathVariable @ApiParam("组织id") Long orgId,
                                             @PathVariable @ApiParam("项目id") Long projectId,
                                             @PathVariable @ApiParam("结构套料方案id") Long fMaterialStructureNestId,
                                             @RequestBody @ApiParam("结构套料方案") FMaterialStructureNestDTO fMaterialStructureNestDTO) {
        ContextDTO context = getContext();
        OperatorDTO operatorDTO = context.getOperator();
        fMaterialStructureNestService.setSubcontractor(orgId, projectId, fMaterialStructureNestId, fMaterialStructureNestDTO, operatorDTO);
        return new JsonResponseBody();

    }

}
