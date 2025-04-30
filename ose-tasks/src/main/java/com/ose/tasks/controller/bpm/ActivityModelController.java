package com.ose.tasks.controller.bpm;

import static com.ose.tasks.domain.model.service.bpm.FileStorageInterface.DOC_CONFIG;
import static com.ose.tasks.domain.model.service.bpm.FileStorageInterface.DOC_MAX_SIZE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import com.ose.tasks.domain.model.service.bpm.ActivityTaskInterface;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.dto.bpm.ActTaskNodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ose.annotation.MultipartFileMaxSize;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.exception.ValidationError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.bpm.ActivityModelAPI;
import com.ose.tasks.api.bpm.FileAPI;
import com.ose.tasks.domain.model.service.bpm.ActivityModelInterface;
import com.ose.tasks.domain.model.service.bpm.FileStorageInterface;
import com.ose.tasks.entity.bpm.BpmReDeployment;
import com.ose.tasks.vo.SuspensionState;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "流程模型管理")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class ActivityModelController extends BaseController implements ActivityModelAPI, FileAPI {

    private final ActivityModelInterface activityModelService;


    private final FileStorageInterface fileStorageService;

    private final ActivityTaskInterface activityTaskService;



    @Value("${application.files.protected}")
    private String protectedDir;


    @Value("${application.files.temporary}")
    private String temporaryDir;

    /**
     * 构造方法
     *  @param activityModelService
     * @param fileStorageService
     * @param activityTaskService
     */
    @Autowired

    public ActivityModelController(ActivityModelInterface activityModelService, FileStorageInterface fileStorageService,
                                   ActivityTaskInterface activityTaskService) {
        this.activityModelService = activityModelService;
        this.fileStorageService = fileStorageService;
        this.activityTaskService = activityTaskService;
    }

    /**
     * 上传部署流程的文件。
     *
     * @param projectId 项目ID
     *                  项目 ID
     * @param orgId     组织ID
     *                  组织ID
     * @param file      通过 HTTP 传输的文件数据
     * @return 响应实例
     */
    @Override
    @Operation(summary = "上传流程部署文件", description = "需要用户登陆，并且必须为zip文件，文件中必须包含bpmn文件跟png文件")
    @RequestMapping(method = POST, value = "models/zip-file", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    @MultipartFileMaxSize(DOC_MAX_SIZE)
    @WithPrivilege
    public JsonObjectResponseBody<TaskTemporaryFileDTO> upload(@Parameter(description = "orgId") @PathVariable("orgId") Long orgId,
                                                               @Parameter(description = "项目id") @PathVariable("projectId") Long projectId,
                                                               @Parameter(description = "流程zip文件") @RequestParam MultipartFile file) {
        return saveMultipartFile(projectId, DOC_CONFIG, file);
    }

    /**
     * 保存上传的文件。
     *
     * @param orgId            组织ID
     *                         所属组织 ID
     * @param fileUploadConfig 文件上传配置
     * @param multipartFile    通过 HTTP 传输的文件数据
     * @return 响应实例
     */
    private JsonObjectResponseBody<TaskTemporaryFileDTO> saveMultipartFile(Long orgId,
                                                                           FileMetadataDTO.FileUploadConfig fileUploadConfig, MultipartFile multipartFile) {
        return new JsonObjectResponseBody<>(getContext(), new TaskTemporaryFileDTO(fileStorageService
            .saveAsTemporaryFile(getContext().getOperator(), orgId, fileUploadConfig, multipartFile).getName()));
    }

    /**
     * 部署流程文件。
     *
     * @param projectId      项目 ID
     * @param modelDeployDTO 文件上传配置
     * @return 响应实例
     */
    @Override
    @RequestMapping(method = POST, value = "models")
    @Operation(summary = "流程部署", description = "将部署文件得临时id必须上传")
    @ResponseStatus(CREATED)
    @WithPrivilege
    public JsonObjectResponseBody<BpmReDeployment> deploy(@Parameter(description = "orgId") @PathVariable("orgId") Long orgId,
                                                          @Parameter(description = "项目id") @PathVariable("projectId") Long projectId,
                                                          @Parameter(description = "部署流程模型") @RequestBody ModelDeployDTO modelDeployDTO) {
        return new JsonObjectResponseBody<>(activityModelService.createBpmModels(orgId, projectId, modelDeployDTO, getContext().getOperator()));
    }

    /**
     * 流程部署列表。
     *
     * @param projectId   项目 ID
     * @param criteriaDTO 文件上传配置
     * @return 响应实例
     */
    @Override
    @RequestMapping(method = GET, value = "models")
    @Operation(summary = "流程部署列表", description = "流程部署列表一览")
    @ResponseStatus(OK)
    @WithPrivilege
    public JsonListResponseBody<BpmReDeployment> list(@PathVariable @Parameter(description = "组织id") Long orgId,
                                                      @PathVariable @Parameter(description = "项目id") Long projectId, ActivitiModelCriteriaDTO criteriaDTO, PageDTO page) {
        return new JsonListResponseBody<>(getContext(),
            activityModelService.list(orgId, projectId, criteriaDTO, page));
    }

    /**
     * 查询流程模型详细信息
     *
     * @param projectId 项目ID
     *                  项目 ID
     * @param orgId     组织ID
     *                  组织id
     * @param procDefId 流程定义id
     * @return 响应实例
     */
    @Override
    @RequestMapping(method = GET, value = "models/{procDefId}")
    @Operation(summary = "查询流程模型详细信息", description = "根据模型的定义id获取流程详细信息。")
    @ResponseStatus(OK)
    @WithPrivilege
    public JsonObjectResponseBody<BpmReDeployment> get(@PathVariable @Parameter(description = "组织id") Long orgId,
                                                       @PathVariable @Parameter(description = "项目id") Long projectId,
                                                       @PathVariable @Parameter(description = "流程定义id") String procDefId) {
        BpmReDeployment brd = activityModelService.findByProcDefId(procDefId);
        if (brd != null) {

            DiagramResourceDTO dto = activityTaskService
                .getDiagramResource(brd.getProjectId(), brd.getProcessId(), brd.getVersion());
            brd.setDiagramResource(dto.getDiagramResource());

            List<ActTaskNodeDTO> nodes = activityTaskService.getModelNodes(projectId, brd.getProcessId(), brd.getVersion());
            brd.setNodes(nodes);

            return new JsonObjectResponseBody<>(getContext(), brd);
        }
        throw new ValidationError("Can't find model by the procDefId:" + procDefId);
    }

    /**
     * 挂起流程定义
     */
    @Override
    @RequestMapping(method = POST, value = "models/{procDefId}/suspend")
    @Operation(summary = "流程模型挂起", description = "根据模型的定义id挂起流程")
    @WithPrivilege
    public JsonResponseBody updateStateSuspend(@PathVariable @Parameter(description = "流程定义id") String procDefId) {
            activityModelService.updateSuspendState(procDefId, SuspensionState.SUSPEND);
        return new JsonResponseBody();
    }

    /**
     * 激活流程定义
     */
    @Override
    @RequestMapping(method = POST, value = "models/{procDefId}/active")
    @Operation(summary = "流程模型激活", description = "根据模型的定义id激活流程")
    @WithPrivilege
    public JsonResponseBody updateStateActive(@PathVariable @Parameter(description = "流程定义id") String procDefId) {
            activityModelService.updateSuspendState(procDefId, SuspensionState.ACTIVE);
        return new JsonResponseBody();
    }











}
