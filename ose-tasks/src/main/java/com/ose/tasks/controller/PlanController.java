package com.ose.tasks.controller;

import com.ose.auth.annotation.SetOrgInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.auth.api.OrganizationFeignAPI;
import com.ose.auth.api.PrivilegeFeignAPI;
import com.ose.auth.dto.AuthCheckDTO;
import com.ose.auth.dto.BatchGetDTO;
import com.ose.auth.dto.OrganizationBasicDTO;
import com.ose.auth.dto.UserPrivilegeDTO;
import com.ose.auth.entity.Organization;
import com.ose.controller.BaseController;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.EventModel;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.dto.jpql.VersionQLDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.ConflictError;
import com.ose.exception.NotFoundError;
import com.ose.feign.RequestWrapper;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.PlanAPI;
import com.ose.tasks.domain.model.repository.ProjectRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.repository.worksite.WorkSiteRepository;
import com.ose.tasks.domain.model.service.AsyncPlanInterface;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.domain.model.service.plan.*;
import com.ose.tasks.domain.model.service.taskpackage.TaskPackageInterface;
import com.ose.tasks.domain.model.service.worksite.WorkSiteInterface;
import com.ose.tasks.dto.*;
import com.ose.tasks.dto.bpm.TaskHierarchyDTO;
import com.ose.tasks.dto.drawing.UploadDrawingFileResultDTO;
import com.ose.tasks.dto.wbs.*;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.process.ProcessEntity;
import com.ose.tasks.entity.taskpackage.TaskPackage;
import com.ose.tasks.entity.wbs.entry.*;
import com.ose.tasks.entity.worksite.WorkSite;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.util.DateUtils;
import com.ose.util.LongUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static com.ose.constant.HttpResponseHeaders.DATA_REVISION;
import static com.ose.tasks.vo.setting.BatchTaskCode.*;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "项目计划管理接口")
@RestController
public class PlanController extends BaseController implements PlanAPI {
    private final static Logger logger = LoggerFactory.getLogger(PlanController.class);


    private final BatchTaskInterface batchTaskService;


    private final ProjectInterface projectService;


    private final PlanImportInterface planImportService;


    private final PlanInterface planService;


    private final WBSSearchInterface wbsSearchService;


    private final PlanExportInterface planExportService;

    private final ProjectRepository projectRepository;


    private final PlanExecutionInterface planExecutionService;


    private final WorkSiteInterface workSiteService;


    private final TaskPackageInterface taskPackageService;


    private final PlanPlainInterface planPlainService;


    private final UploadFeignAPI uploadFeignAPI;


    private final OrganizationFeignAPI organizationFeignAPI;


    private final PrivilegeFeignAPI privilegeFeignAPI;

    private final TodoTaskDispatchInterface todoTaskDispatchService;

    private final AsyncPlanInterface asyncPlanService;

    private final TaskPackageRepository taskPackageRepository;

    private final PrivilegeFeignAPI privilegeAPI;
    private final WBSEntryStateRepository wbsEntryStateRepository;

    private final WorkSiteRepository workSiteRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public PlanController(
        BatchTaskInterface batchTaskService,
        ProjectInterface projectService,
        PlanImportInterface planImportService,
        PlanInterface planService,
        WBSSearchInterface wbsSearchService,
        PlanExportInterface planExportService,
        ProjectRepository projectRepository,
        PlanExecutionInterface planExecutionService,
        WorkSiteInterface workSiteService,
        TaskPackageInterface taskPackageService,
        PlanPlainInterface planPlainService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
        UploadFeignAPI uploadFeignAPI,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
        OrganizationFeignAPI organizationFeignAPI,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
        PrivilegeFeignAPI privilegeFeignAPI,
        AsyncPlanInterface asyncPlanService,
        TodoTaskDispatchInterface todoTaskDispatchService,
        TaskPackageRepository taskPackageRepository,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") PrivilegeFeignAPI privilegeAPI,
        WorkSiteRepository workSiteRepository,
        WBSEntryStateRepository wbsEntryStateRepository) {
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;
        this.planImportService = planImportService;
        this.planService = planService;
        this.wbsSearchService = wbsSearchService;
        this.planExportService = planExportService;
        this.projectRepository = projectRepository;
        this.planExecutionService = planExecutionService;
        this.workSiteService = workSiteService;
        this.taskPackageService = taskPackageService;
        this.planPlainService = planPlainService;
        this.uploadFeignAPI = uploadFeignAPI;
        this.organizationFeignAPI = organizationFeignAPI;
        this.privilegeFeignAPI = privilegeFeignAPI;
        this.todoTaskDispatchService = todoTaskDispatchService;
        this.asyncPlanService = asyncPlanService;
        this.taskPackageRepository = taskPackageRepository;
        this.privilegeAPI = privilegeAPI;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
        this.workSiteRepository = workSiteRepository;
    }

    /**
     * 从 WBS 条目列表中取得工作组 ID 集合。
     *
     * @param entries 条目列表
     * @param orgIDs  工作组 ID 集合
     */
    private void getTeamInfo(List<WBSEntryDTO> entries, Set<Long> orgIDs) {

        if (entries == null || entries.size() == 0) {
            return;
        }

        for (WBSEntryDTO entry : entries) {

            if (!LongUtils.isEmpty(entry.getTeamId())) {
                orgIDs.add(entry.getTeamId());
            }

            if (entry.getChildren() != null) {
                getTeamInfo(entry.getChildren(), orgIDs);
            }

        }

    }

    /**
     * 取得 WBS 条目的工作组信息。
     *
     * @param entries 条目信息列表
     * @return 工作组信息列表
     */
    private Map<Long, Object> getTeamInfo(List<WBSEntryDTO> entries) {

        Set<Long> orgIDs = new HashSet<>();

        getTeamInfo(entries, orgIDs);

        if (orgIDs.size() > 0) {

            JsonListResponseBody<OrganizationBasicDTO> responseBody = organizationFeignAPI
                .batchGet(new BatchGetDTO(orgIDs));

            Map<Long, Object> orgs = new HashMap<>();

            if (responseBody.getData() != null) {
                for (OrganizationBasicDTO org : responseBody.getData()) {
                    orgs.put(org.getId(), org);
                }
            }

            return orgs;
        }

        return new HashMap<>();
    }


    /**
     * 取得 WBS_Plain 条目的工作组信息。
     *
     * @param entries 条目信息列表
     * @return 工作组信息列表
     */
    private Map<Long, Object> getTeamInfo(List<WBSEntryPlain> entries, boolean plain) {

        Set<Long> orgIDs = new HashSet<>();

        if (entries == null || entries.size() == 0) {
            return null;
        }

        for (WBSEntryPlain entry : entries) {
            if (entry.getWorkSiteId() != null) {
                orgIDs.add(entry.getWorkSiteId());
            }


        }

        if (orgIDs.size() > 0) {

            Map<Long, Object> orgs = new HashMap<>();
            for (Long id : orgIDs) {
                Optional<WorkSite> workSite = workSiteRepository.findById(id);
                if (workSite.isPresent()) {
                    orgs.put(id, workSite.get());
                }
            }

            return orgs;
        }

        return new HashMap<>();
    }


    @Override
    @Operation(description = "导入项目计划")
    @WithPrivilege
    public JsonResponseBody importPlan(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody HierarchyNodeImportDTO nodeImportDTO
    ) {

        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(projectId);

        ContextDTO contextDTO = getContext();
        contextDTO.setRequestMethod(contextDTO.getRequest().getMethod());
        batchTaskService
            .conflictWith(projectId, PROJECT_PLAN_IMPORT, ENTITY_PROCESS_WBS_GENERATE)
            .run(
                contextDTO, project, PROJECT_PLAN_IMPORT,
                batchTask -> {

                    BatchResultDTO result = planImportService.importPlan(
                        batchTask,
                        operator,
                        project,
                        null,
                        nodeImportDTO,
                        wbsSearchService.getProcessEntityTypeRelations(orgId, projectId)
                    );
                    logger.error("四级计划 保存docs服务->开始");
                    JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(
                        orgId.toString(),
                        projectId.toString(),
                        nodeImportDTO.getFilename(),
                        new FilePostDTO()
                    );
                    logger.error("四级计划 保存docs服务->结束");
                    batchTask.setImportFile(LongUtils.parseLong(responseBody.getData().getId()));

                    return result;
                }
            );

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "查询 WBS")
    @WithPrivilege
    public JsonObjectResponseBody<WBSHierarchyDTO> getWBS(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        WBSEntryQueryDTO queryDTO
    ) {
        return getWBS(orgId, projectId, projectId, queryDTO);
    }

    @Override
    @Operation(description = "查询 WBS")
    @WithPrivilege
    public JsonObjectResponseBody<WBSHierarchyDTO> getWBS(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "WBS 条目 ID") Long wbsEntryId,
        WBSEntryQueryDTO queryDTO
    ) {

        Long version = wbsSearchService.getWBSVersion(projectId);

        if (version == null) {
            throw new NotFoundError();
        }

        WBSHierarchyDTO responseData = new WBSHierarchyDTO();

        responseData.setVersion(version);

        List<WBSEntryDTO> entries = wbsSearchService
            .getWBSHierarchy(orgId, projectId, wbsEntryId, queryDTO);

        responseData.setChildren(entries);

        JsonObjectResponseBody<WBSHierarchyDTO> result = new JsonObjectResponseBody<>(responseData);

        result.addIncluded(wbsSearchService.getReferencedWBSEntries(projectId, entries));
        result.addIncluded(getTeamInfo(responseData.getChildren()));

        return result;
    }


    @Override
    @Operation(description = "查询四级计划分页数据")
    @WithPrivilege
    public JsonListResponseBody<WBSEntryWithRelations> listEntityProcesses(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        WBSEntryQueryDTO queryDTO,
        PageDTO pageDTO
    ) {
        return listEntityProcesses(orgId, projectId, null, queryDTO, pageDTO);
    }

    @Override
    @Operation(description = "查询四级计划分页数据")
    @WithPrivilege
    public JsonListResponseBody<WBSEntryWithRelations> listEntityProcesses(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "WBS 条目 ID") Long wbsEntryId,
        WBSEntryQueryDTO queryDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            wbsSearchService.searchEntityProcesses(
                orgId, projectId, wbsEntryId, queryDTO, pageDTO.toPageable()
            )
        );
    }

    @Override
    @Operation(description = "添加实体资源")
    @WithPrivilege
    public JsonObjectResponseBody<WBSEntry> addEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "最大层级深度") Long workId,
        @RequestBody WBSEntityPostDTO wbsEntityPostDTO
    ) {
        ContextDTO context = getContext();

        String authorization = context.getAuthorization();
        if (!context.isContextSet()) {
            String userAgent = context.getUserAgent();
            final RequestAttributes attributes = new ServletRequestAttributes(
                new RequestWrapper(context.getRequest(), authorization, userAgent),
                null
            );
            RequestContextHolder.setRequestAttributes(attributes, true);
            context.setContextSet(true);
        }

        WBSEntry wbsEntry = planService.addEntity(
            getContext().getOperator(),
            orgId,
            projectId,
            workId,
            wbsEntityPostDTO
        );


        Project project = projectService.get(orgId, projectId);
        batchTaskService.runConstructTaskExecutor(
            null,
            project,
            BatchTaskCode.PLAN,
            false,
            context,
            batchTask -> {
                taskPackageService.setWBSTaskPackageInfo(getContext().getOperator(), orgId, projectId);
                return new BatchResultDTO();
            }
        );


        getResponse().setHeader(
            DATA_REVISION,
            "" + wbsSearchService.getWBSVersion(projectId)
        );

        return new JsonObjectResponseBody<>(wbsEntry);
    }

    /**
     * 检查 WBS 更新版本号。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param version   更新版本号
     */
    private void checkWBSRevision(Long orgId, Long projectId, long version) {

        Long latestVersion = wbsSearchService.getWBSVersion(projectId);

        if (latestVersion == null) {
            throw new NotFoundError();
        }

        if (latestVersion != version) {
            throw new ConflictError();
        }

    }

    @Override
    @Operation(description = "更新实体工序计划条目")
    @WithPrivilege
    public JsonObjectResponseBody<WBSEntry> updateEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体工序计划条目 ID") Long entityEntryId,
        @RequestParam @Parameter(description = "WBS 版本号") long version,
        @RequestBody @Parameter(description = "资源设置") WBSEntityPatchDTO wbsEntityPatchDTO
    ) {
        return updateEntry(orgId, projectId, entityEntryId, version, wbsEntityPatchDTO);
    }

    @Override
    @Operation(description = "更新工作计划条目")
    @WithPrivilege
    public JsonObjectResponseBody<WBSEntry> updateEntry(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "工作计划条目 ID") Long workEntryId,
        @RequestParam @Parameter(description = "WBS 版本号") long version,
        @RequestBody @Parameter(description = "资源设置") WBSEntryPatchDTO wbsEntryPatchDTO
    ) {

        checkWBSRevision(orgId, projectId, version);

        WBSEntry wbsEntry = planService.updateEntry(
            getContext().getOperator(),
            orgId,
            projectId,
            workEntryId,
            wbsEntryPatchDTO
        );

        getResponse().setHeader(
            DATA_REVISION,
            "" + wbsSearchService.getWBSVersion(projectId)
        );

        return new JsonObjectResponseBody<>(wbsEntry);
    }

    @Override
    @Operation(description = "删除计划条目")
    @WithPrivilege
    public JsonObjectResponseBody<VersionQLDTO> deleteEntry(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "WBS 条目 ID") Long wbsEntryId,
        @RequestParam @Parameter(description = "WBS 版本号") long version
    ) {

        checkWBSRevision(orgId, projectId, version);

        Long entryUpdateVersion = planService.deleteEntry(
            getContext().getOperator(),
            orgId,
            projectId,
            wbsEntryId
        );

        getResponse().setHeader(
            DATA_REVISION,
            "" + wbsSearchService.getWBSVersion(projectId)
        );

        return new JsonObjectResponseBody<>(new VersionQLDTO(entryUpdateVersion));
    }

    @Override
    @Operation(description = "自动绑定实体资源")
    @WithPrivilege
    public JsonResponseBody bindEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "WBS 条目 ID") Long wbsEntryId,
        @RequestParam(name = "updateModule", required = false, defaultValue = "true")
        @Parameter(description = "更新此模块 还是整个项目")
        boolean updateModule
    ) {

        final Project project = projectService.get(orgId, projectId);
        final ContextDTO context = getContext();
        final OperatorDTO operator = context.getOperator();

        context.setRequestMethod(context.getRequest().getMethod());
        batchTaskService
            .conflictWith(projectId, PROJECT_PLAN_IMPORT, ENTITY_PROCESS_WBS_GENERATE)
            .run(
                context,
                project,
                ENTITY_PROCESS_WBS_GENERATE,
                batchTask -> {

                    BatchResultDTO result = planImportService
                        .generateEntityProcessWBSEntries(batchTask, operator, orgId, projectId, wbsEntryId, updateModule);

                    return result;

                }
            );

        getResponse().setHeader(
            DATA_REVISION,
            "" + wbsSearchService.getWBSVersion(projectId)
        );

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "增加生成四级计划的队列")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/plan-queue/{wbsEntryId}"
    )
    @ResponseStatus(OK)
    @WithPrivilege
    public JsonResponseBody addPlanQueue(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "WBS 条目 ID") Long wbsEntryId,
        @RequestBody WBSEntryDTO wbsEntryDTO
    ) {

        final ContextDTO context = getContext();

        planExecutionService.pushQueue(context, projectId, wbsEntryId);

        return new JsonResponseBody();
    }

    @Operation(description = "增加生成增量四级计划的队列")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/added-plan-queue/{wbsEntryId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    @WithPrivilege
    public JsonResponseBody addAddedPlanQueue(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "WBS 条目 ID") Long wbsEntryId,
        @RequestBody WBSEntryDTO wbsEntryDTO
    ) {
        final ContextDTO context = getContext();

        planExecutionService.pushAddedEntityQueue(context, projectId, wbsEntryId);

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "获取生成四级计划的队列")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/plan-queue"
    )
    @ResponseStatus(OK)
    @WithPrivilege
    public JsonListResponseBody<PlanQueueDTO> getPlanQueue(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        PageDTO pageDTO
    ) {


        return new JsonListResponseBody(
            planExecutionService.getQueue(projectId, pageDTO)
        );
    }

    @Override
    @Operation(description = "自动对增加的实体 生成四级计划 及关系")
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody bindAddedEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "WBS 条目 ID") Long wbsEntryId,
        @RequestBody WBSEntryDTO wbsEntryDTO
    ) {

        final Project project = projectService.get(orgId, projectId);
        final ContextDTO context = getContext();
        final OperatorDTO operator = context.getOperator();

        context.setRequestMethod(context.getRequest().getMethod());
        batchTaskService
            .conflictWith(projectId, PROJECT_PLAN_IMPORT, ENTITY_PROCESS_WBS_GENERATE)
            .run(
                context,
                project,
                ENTITY_PROCESS_WBS_GENERATE,
                batchTask -> {
                    BatchResultDTO result = planImportService
                        .generateWbsEntryFromAddedWbsEntity(batchTask, operator, orgId, projectId, wbsEntryId);
                    return result;
                }
            );

        getResponse().setHeader(
            DATA_REVISION,
            "" + wbsSearchService.getWBSVersion(projectId)
        );

        // 针对pending状态的焊口任务重新跑启动接口
//        logger.info("开始启动PENDING任务");
//        RepairWbsSearchDTO repairWbsSearchDTO = new RepairWbsSearchDTO();
//        repairWbsSearchDTO.setProcess("FITUP");
//        repairWbsSearchDTO.setFetchAll(true);
//        repairDataService.rePendingStart(orgId, projectId, repairWbsSearchDTO, context);
//
//        RepairWbsSearchDTO deliveryDTO1 = new RepairWbsSearchDTO();
//        deliveryDTO1.setProcess("DISTRIBUTION_TO_FABRICATION");
//        deliveryDTO1.setFetchAll(true);
//        repairDataService.rePendingStart(orgId, projectId, deliveryDTO1, context);
//
//        RepairWbsSearchDTO deliveryDTO2 = new RepairWbsSearchDTO();
//        deliveryDTO2.setProcess("DISTRIBUTION_TO_ASSEMBLY");
//        deliveryDTO2.setFetchAll(true);
//        repairDataService.rePendingStart(orgId, projectId, deliveryDTO2, context);
//
//        RepairWbsSearchDTO deliveryDTO3 = new RepairWbsSearchDTO();
//        deliveryDTO3.setProcess("HANDOVER_PRE_PAINTING");
////        deliveryDTO3.setStage("CUTTING");
//        deliveryDTO3.setFetchAll(true);
//        repairDataService.rePendingStart(orgId, projectId, deliveryDTO3, context);

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "任务包 生成四级计划 及关系")
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody bindAddedTaskPackageEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "WBS 条目 ID") Long taskPackageId,
        @RequestBody WBSEntryDTO wbsEntryDTO
    ) {

        final Project project = projectService.get(orgId, projectId);
        final ContextDTO context = getContext();
        final OperatorDTO operator = context.getOperator();

        context.setRequestMethod(context.getRequest().getMethod());
        batchTaskService
            .conflictWith(projectId, PROJECT_PLAN_IMPORT, ENTITY_PROCESS_WBS_GENERATE)
            .run(
                context,
                project,
                ENTITY_PROCESS_WBS_GENERATE,
                batchTask -> {
                    BatchResultDTO result = planImportService
                        .generateWbsEntryFromTaskPackageWbsEntity(batchTask, operator, orgId, projectId, taskPackageId);
                    return result;
                }
            );

        getResponse().setHeader(
            DATA_REVISION,
            "" + wbsSearchService.getWBSVersion(projectId)
        );

        return new JsonResponseBody();
    }


    @Override
    @Operation(description = "取得 WBS 条目详细信息")
    @WithPrivilege
    @SetOrgInfo
    public JsonObjectResponseBody<WBSEntry> getWBSEntry(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "WBS 条目 ID") Long wbsEntryId
    ) {

        WBSEntry entry = wbsSearchService.get(projectId, wbsEntryId);

        if (entry == null) {
            throw new NotFoundError();
        }

        return new JsonObjectResponseBody<>(entry);
    }

    @Override
    @Operation(description = "取得 WBS 条目前置任务列表")
    @WithPrivilege
    public JsonListResponseBody<WBSEntryPredecessorDetail> predecessors(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "WBS 条目 ID") Long wbsEntryId,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            wbsSearchService.predecessors(projectId, wbsEntryId, pageDTO.toPageable())
        );
    }

    @Override
    @Operation(description = "取得 WBS 条目后置任务列表")
    @WithPrivilege
    public JsonListResponseBody<WBSEntrySuccessorDetail> successors(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "WBS 条目 ID") Long wbsEntryId,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            wbsSearchService.successors(projectId, wbsEntryId, pageDTO.toPageable())
        );
    }

    @Override
    @Operation(
        summary = "取得计划条目的所有上级",
        description = "条目自身的信息将作为列表中的最后一个项目返回。"
    )
    @WithPrivilege
    @SetOrgInfo
    public JsonListResponseBody<WBSEntryPlain> getParents(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "WBS 条目 ID") Long wbsEntryId,
        @RequestParam(required = false, defaultValue = "false") @Parameter(description = "是否返回条目自身信息") boolean appendSelf
    ) {
        return new JsonListResponseBody<>(
            wbsSearchService.getParents(projectId, wbsEntryId, appendSelf)
        );
    }

    @Override
    @Operation(
        summary = "设置 WBS 条目的权重",
        description = "设权重增量为 i：当存在上级条目时，上级条目的权重都会增加 i；当存在子条目时，所有子条目的权重都会根据等比例增加，以使所有子节点的增量之和等于 i。"
    )
    @WithPrivilege
    public JsonResponseBody setEntryScore(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "条目 ID") Long wbsEntryId,
        @RequestParam @Parameter(description = "WBS 版本号") long version,
        @RequestBody @Valid WBSScorePutDTO scorePutDTO
    ) {

        checkWBSRevision(orgId, projectId, version);

        planService.setScore(
            getContext().getOperator(),
            orgId,
            projectId,
            wbsEntryId,
            scorePutDTO.getScore()
        );

        getResponse().setHeader(
            DATA_REVISION,
            "" + wbsSearchService.getWBSVersion(projectId)
        );

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "将 WBS 条目标记为挂起")
    @WithPrivilege
    public JsonResponseBody setAsSuspended(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "条目 ID") Long wbsEntryId
    ) {

        planExecutionService.suspend(
            getContext().getOperator(),
            orgId,
            projectId,
            wbsEntryId
        );

        getResponse().setHeader(
            DATA_REVISION,
            "" + wbsSearchService.getWBSVersion(projectId)
        );

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "恢复挂起的 WBS 条目")
    @WithPrivilege
    public JsonResponseBody resume(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "条目 ID") Long wbsEntryId
    ) {
        ContextDTO context = getContext();

        planExecutionService.resume(
            context,
            todoTaskDispatchService,
            getContext().getOperator(),
            orgId,
            projectId,
            wbsEntryId
        );

        getResponse().setHeader(
            DATA_REVISION,
            "" + wbsSearchService.getWBSVersion(projectId)
        );

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "将 WBS 条目标记为已完成")
    @WithPrivilege
    public JsonResponseBody setAsFinished(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "条目 ID") Long wbsEntryId,
        @RequestBody @Valid WBSEntryFinishDTO finishDTO
    ) {


        ContextDTO context = getContext();
        planExecutionService.finish(
            context,
            todoTaskDispatchService,
            context.getOperator(),
            orgId,
            projectId,
            wbsEntryId,
            finishDTO.getApproved(),
            finishDTO.getHours(),
            finishDTO.getHalt(),
            false
        );

        getResponse().setHeader(
            DATA_REVISION,
            "" + wbsSearchService.getWBSVersion(projectId)
        );

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "为计划任务指派工作组")
    @WithPrivilege
    public JsonResponseBody dispatchTeam(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "WBS 条目 ID") Long wbsEntryId,
        @RequestBody @Valid WBSTeamPutDTO teamPutDTO
    ) {

        Long companyId = null;
        Organization team = null;


        if (teamPutDTO.getTeamId() != null) {

            team = organizationFeignAPI
                .details(teamPutDTO.getTeamId(), orgId)
                .getData();

            if (team == null) {
                throw new BusinessError("error.work-team.not-found");
            }

            companyId = team.getCompanyId();
        }


        if (companyId == null) {

            Organization org = organizationFeignAPI.details(orgId, null).getData();

            if (org == null) {
                throw new NotFoundError();
            }

            companyId = org.getCompanyId();
        }

        WorkSite workSite = null;


        if (teamPutDTO.getWorkSiteId() != null) {

            workSite = workSiteService.get(companyId, projectId, teamPutDTO.getWorkSiteId());

            if (workSite == null) {
                throw new BusinessError("工作场地不存在");
            }

        }


        planExecutionService.dispatch(
            getContext().getOperator(),
            orgId,
            projectId,
            wbsEntryId,
            team,
            workSite
        );

        getResponse().setHeader(
            DATA_REVISION,
            "" + wbsSearchService.getWBSVersion(projectId)
        );

        return new JsonResponseBody();
    }

    /**
     * 任务分配人。
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param wbsEntryId    WBS条目ID
     * @param wbsUserPutDTO 分配信息
     */
    @Override
    @Operation(description = "三级计划人员分配")
    @WithPrivilege
    public JsonResponseBody dispatchUser(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "WBS条目ID") Long wbsEntryId,
        @RequestBody WBSUserPutDTO wbsUserPutDTO
    ) {

        if (wbsUserPutDTO.getTeamId() != null) {
            Organization team = organizationFeignAPI.details(wbsUserPutDTO.getTeamId(), orgId).getData();
            if (team == null) {
                throw new NotFoundError("team is not found");
            }
        }

        ContextDTO context = getContext();

        planExecutionService.dispatch(
            context.getOperator().getId(),
            orgId,
            projectId,
            wbsEntryId,
            wbsUserPutDTO.getTeamId(),
            wbsUserPutDTO.getUserId(),
            wbsUserPutDTO.getDelegateId(),
            wbsUserPutDTO.getPrivilege()
        );

        getResponse().setHeader(
            DATA_REVISION,
            "" + wbsSearchService.getWBSVersion(projectId)
        );

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "启动模块计划任务")
    @WithPrivilege
    public JsonResponseBody startWBS(
        @PathVariable @Parameter(description = "组织 ID") final Long orgId,
        @PathVariable @Parameter(description = "项目 ID") final Long projectId,
        @PathVariable @Parameter(description = "模块层级节点 ID") final Long moduleId
    ) {
        ContextDTO context = getContext();
        final OperatorDTO operator = context.getOperator();

        wbsSearchService.getWBSEntityEntries(orgId, projectId, moduleId).forEach(wbsEntityEntry ->
            planExecutionService.startWBSEntry(
                context,
                todoTaskDispatchService,
                operator,
                orgId,
                projectId,
                wbsEntityEntry,
                wbsEntityEntry.getProcessId(),
                false
            )
        );

        getResponse().setHeader(
            DATA_REVISION,
            "" + wbsSearchService.getWBSVersion(projectId)
        );

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "启动计划任务条目")
    @WithPrivilege
    public JsonResponseBody startWBSEntry(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "计划条目 ID") Long entryId
    ) {
        ContextDTO context = getContext();
        planExecutionService.startWBSEntry(
            context,
            todoTaskDispatchService,
            getContext().getOperator(),
            orgId,
            projectId,
            entryId,
            true
        );


        getResponse().setHeader(
            DATA_REVISION,
            "" + wbsSearchService.getWBSVersion(projectId)
        );

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "启动计划任务条目")
    @WithPrivilege
    public JsonResponseBody startAllWBSEntry(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody WBSEntriesDTO wBSEntryDTO
    ) {
        ContextDTO context = getContext();

        planExecutionService.batchStartWBSEntry(context, orgId, projectId, context.getOperator(), wBSEntryDTO);

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "取得 WBS 三级计划跟四级计划 施工组织跟用户信息")
    @WithPrivilege
    public JsonObjectResponseBody<WBSEntryDelegateDTO> getWBSEntryDelegateAndTeam(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "WBS 条目 ID") Long wbsEntryId
    ) {
        return new JsonObjectResponseBody<>(
            wbsSearchService.getWBSEntryDelegateAndTeam(orgId, projectId, wbsEntryId)
        );
    }

    @Override
    @Operation(description = "查询当前用户的可执行任务列表")
    @WithPrivilege
    @SetOrgInfo
    public JsonListResponseBody<WBSEntryActivityInstance> searchTasks(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "计划条目 ID") Long entryId,
        PageDTO pageDTO
    ) {

        projectService.get(orgId, projectId);

        return new JsonListResponseBody<>(
            wbsSearchService.getWBSTasks(
                getContext().getOperator().getId(),
                UserPrivilegeDTO.toMap(privilegeFeignAPI.getUserPrivileges(orgId).getData()),
                projectId,
                entryId,
                pageDTO.toPageable()
            )
        );

    }

    @Override
    @Operation(description = "导出三级计划")
    @WithPrivilege
    public void exportWBSWorks(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestParam(required = false) @Parameter(description = "跟节点 ID（可选）") Long rootId
    ) throws IOException {

        HttpServletResponse response = getContext().getResponse();

        File file = planExportService.exportWBSWorkEntries(
            getContext().getOperator(),
            orgId,
            projectId,
            rootId
        );

        response.setHeader(
            CONTENT_DISPOSITION,
            "attachment; filename=\"wbs-work-entries-" + DateUtils.toNumbers(new Date()) + ".xlsx\""
        );

        IOUtils.copy(new FileInputStream(file), response.getOutputStream());

        response.flushBuffer();
    }

    @Override
    @Operation(description = "查询实体")
    @WithPrivilege
    public JsonListResponseBody<ProcessEntity> entities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "工序阶段名称") String stageName,
        @PathVariable @Parameter(description = "工序名称") String processName,
        @PathVariable @Parameter(description = "实体类型") String entityType,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            planService.searchEntities(
                orgId,
                projectId,
                stageName,
                processName,
                entityType,
                pageDTO.toPageable()
            )
        );
    }

    @Override
    @Operation(description = "导出四级计划")
    @WithPrivilege
    public void exportWBSEntityProcesses(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "三级计划 ID") Long workId
    ) throws IOException {

        HttpServletResponse response = getContext().getResponse();

        File file = planExportService.exportWBSEntityEntries(
            getContext().getOperator(),
            orgId,
            projectId,
            workId
        );

        response.setHeader(
            CONTENT_DISPOSITION,
            "attachment; filename=\"wbs-entity-processes-" + DateUtils.toNumbers(new Date()) + ".xlsx\""
        );

        IOUtils.copy(new FileInputStream(file), response.getOutputStream());

        response.flushBuffer();
    }


    @Override
    @Operation(description = "重新生成四级计划前后置关系")
    @WithPrivilege
    public JsonResponseBody regenerateWBSRelations(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId
    ) {
        planImportService.regenerateWBSEntryRelations(
            getContext().getOperator(),
            orgId,
            projectId
        );
        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "设置手动进度")
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody setWBSEntryManualProgress(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "WBS 条目 ID") Long wbsEntryId,
        @RequestParam @Parameter(description = "手动进度百分比") String manualProgress
    ) {

        planService.updateWBSEntryManualProgress(orgId, projectId, wbsEntryId, manualProgress);

        return new JsonResponseBody();
    }

    /**
     * 根据搜索条件，取得扁平的WBS计划列表，包括3级和4级计划
     * * 查询条件
     * * 实体类型                         List<String>
     * * 实体子类型                        List<String>
     * * WBS 类型 type，3级计划和4级计划    List, WORK UNIT ENTITY
     * * 实体类型 WELD，ISO等，            List<String>
     * * 实体子类型 FBW，SBW等，            List<String>
     * * 开始时间                          Date
     * * 结束时间                          Date
     * * runningstatus，运行状态           List<String>
     * * 前置任务是否完成                   boolean
     * * 所属模块、子系统 Hierarchy Id      List <String>
     * * 工序阶段                          List<String>
     * * 工序                              List<String>
     * * 材料匹配率                        Double
     * * 发图情况                          boolean
     */
    @Operation(description = "取得扁平的WBS计划列表，包括3级和4级计划")
    @WithPrivilege
    @Override
    public JsonListResponseBody<WBSEntryPlain> getPlainWBS(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                                           @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                           WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO) {

        Long version = wbsSearchService.getWBSVersion(projectId);

        if (version == null) {
            throw new NotFoundError();
        }

        Page<WBSEntryPlain> wbsEntries = planPlainService.searchWbs(
            orgId,
            projectId,
            wbsEntryPlainQueryDTO
        );


        JsonListResponseBody<WBSEntryPlain> result = new JsonListResponseBody<>(wbsEntries);
        Map<Long, Object> versionMap = new HashMap<Long, Object>() {{
            put(BpmCode.VERSION_L, version);
        }};
        result.addIncluded(versionMap);
        result.addIncluded(getTeamInfo(wbsEntries.getContent(), true));
        result.addIncluded(getTaskPackageInfo(wbsEntries.getContent()));

        return result;

        /*return new JsonListResponseBody<>(
                planPlainService.searchWbs(
                        orgId,
                        projectId,
                        wbsEntryPlainQueryDTO
                )
        );*/
    }

    /**
     * 根据搜索条件，取得扁平的WBS汇总的计划列表，包括3级和4级计划的汇总情况
     */
    @Operation(description = "取得扁平的WBS计划汇总列表，包括3级和4级计划汇总情况")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wbs-plain-group",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonListResponseBody<WBSEntryGroupPlainDTO> getPlainGroupWBS(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                                                        @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                                        WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO) {

        Long version = wbsSearchService.getWBSVersion(projectId);

        if (version == null) {
            throw new NotFoundError();
        }

        Page<WBSEntryGroupPlainDTO> wbsEntries = planPlainService.searchGroupWbs(
            orgId,
            projectId,
            wbsEntryPlainQueryDTO
        );


        JsonListResponseBody<WBSEntryGroupPlainDTO> result = new JsonListResponseBody<>(wbsEntries);
        Map<Long, Object> versionMap = new HashMap<Long, Object>() {{
            put(BpmCode.VERSION_L, version);
        }};
        result.addIncluded(versionMap);

        return result;

        /*return new JsonListResponseBody<>(
                planPlainService.searchGroupWbs(
                        orgId,
                        projectId,
                        wbsEntryPlainQueryDTO
                )
        );*/
    }

    /**
     * 取得扁平WBS计划中的材料汇总信息
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    @Operation(description = "取得四级计划上的材料汇总信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wbs-plain-material",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonObjectResponseBody<MaterialMatchSummaryDTO> getWbsPlainMaterial(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @Parameter(description = "扁平计划查询条件") WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO
    ) {
        return new JsonObjectResponseBody(
            planPlainService.getMaterialInfo(orgId, projectId, wbsEntryPlainQueryDTO)
        );
    }

    /**
     * 取得扁平WBS计划中的ISO材料信息
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    @Operation(description = "取得四级计划上的ISO材料信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wbs-plain-iso-material",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonObjectResponseBody<MaterialISOMatchSummaryDTO> getWbsPlainIsoMaterial(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @Parameter(description = "扁平计划查询条件") WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO
    ) {
        return new JsonObjectResponseBody(
            planPlainService.getIsoMaterialInfo(orgId, projectId, wbsEntryPlainQueryDTO)
        );
    }

    /**
     * 取得扁平WBS计划中的图纸信息
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    @Operation(description = "取得四级计划上的图纸信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wbs-plain-dwg",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonObjectResponseBody<WBSEntryDwgSummaryDTO> getWbsPlainDwg(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @Parameter(description = "扁平计划查询条件") WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO
    ) {
        return new JsonObjectResponseBody<>(
            planPlainService.getDwgInfo(orgId, projectId, wbsEntryPlainQueryDTO)
        );
    }

    /**
     * 取得扁平wbs_entry_execution_history中的未生成四级计划的实体，并生成或删除四级计划
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wbs-generate",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    public JsonResponseBody generateWbsFromEntity(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                                  @PathVariable @Parameter(description = "项目 ID") Long projectId) {

        final Project project = projectService.get(orgId, projectId);
        final ContextDTO context = getContext();
        final OperatorDTO operator = context.getOperator();

        context.setRequestMethod(context.getRequest().getMethod());
        batchTaskService
            .conflictWith(projectId, PROJECT_PLAN_IMPORT, ENTITY_PROCESS_WBS_GENERATE)
            .run(
                context,
                project,
                ENTITY_PROCESS_WBS_GENERATE,
                batchTask -> {
                    BatchResultDTO result = planImportService
                        .generateWBSEntryFromEntity(batchTask, operator, orgId, projectId);
                    if (result.getProcessedCount() > 0) {
                        taskPackageService.setWBSTaskPackageInfo(operator, orgId, projectId);
                    }
                    return result;
                }
            );

        getResponse().setHeader(
            DATA_REVISION,
            "" + wbsSearchService.getWBSVersion(projectId)
        );

        return new JsonResponseBody();
    }


    /**
     * 返回计划管理页面工序阶段和工序
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/stage-processes"
    )
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<TaskHierarchyDTO> getStageProcesses(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                                                      @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                                      Long stageId) {

        TaskHierarchyDTO dto = planPlainService.getStageProcess(orgId, projectId, stageId);
        return new JsonObjectResponseBody<>(getContext(), dto);
    }

    /**
     * 更新当前 计划再REDIS 中的状态值 COLD HOT
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/reset-plan-queue"
    )
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody resetPlanQueue(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                           @PathVariable @Parameter(description = "项目 ID") Long projectId) {

        planExecutionService.resetPlanQueue(orgId, projectId);
        return new JsonObjectResponseBody<>();
    }

    /**
     * 检查实体是否可以进行实体变更
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/plain/check/{entityId}"
    )
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<PlainCheckDTO> checkChange(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                                             @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                             @PathVariable @Parameter(description = "实体 ID") Long entityId
    ) {
        PlainCheckDTO result = new PlainCheckDTO();
        result.setStatus(planImportService.checkChange(orgId, projectId, entityId));

        return new JsonObjectResponseBody<>(getContext(), result);
    }

    @Override
    @Operation(
        summary = "启动工序下的四级计划及任务",
        description = "启动工序下的四级计划及任务。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/process/plain"
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonResponseBody startPlan(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @RequestBody WBSEntryPlainStartDTO wBSEntryPlainStartDTO
    ) {
        asyncPlanService.callAsyncPlanByProcessExecuteFinish(getContext(), orgId, projectId, wBSEntryPlainStartDTO, false);
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "启动实体下的四级计划及任务",
        description = "启动实体下的四级计划及任务。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/weld/plain"
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonResponseBody startWeldPlan(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @RequestBody WBSEntryWeldPlainStartDTO wBSEntryWeldPlainStartDTO
    ) {

        final Project project = projectService.get(orgId, projectId);
        final ContextDTO context = getContext();
        final OperatorDTO operator = context.getOperator();

        context.setRequestMethod(context.getRequest().getMethod());
        batchTaskService
            .conflictWith(projectId, PROJECT_PLAN_IMPORT, ENTITY_PROCESS_WBS_GENERATE)
            .run(
                context,
                project,
                ENTITY_PROCESS_WBS_GENERATE,
                batchTask -> {
                    BatchResultDTO result = planImportService
                        .generateWbsEntryFromSelectedEntities(batchTask, operator, orgId, projectId, wBSEntryWeldPlainStartDTO.getEntityIds());
                    return result;
                }
            );

        getResponse().setHeader(
            DATA_REVISION,
            "" + wbsSearchService.getWBSVersion(projectId)
        );

        return new JsonResponseBody();
    }

    /**
     * 取得 WBS_Plain 条目的工作组信息。
     *
     * @param entries 条目信息列表
     * @return 工作组信息列表
     */
    private Map<Long, Object> getTaskPackageInfo(List<WBSEntryPlain> entries) {

        List<Long> taskPackageIds = new ArrayList<>();

        if (entries == null || entries.size() == 0) {
            return null;
        }

        for (WBSEntryPlain entry : entries) {
            WBSEntryState wbsEntryState = wbsEntryStateRepository.findByWbsEntryId(entry.getId());
            if (wbsEntryState != null && !LongUtils.isEmpty(wbsEntryState.getTaskPackageId())) {
                taskPackageIds.add(wbsEntryState.getTaskPackageId());
            }
        }

        if (taskPackageIds.size() > 0) {
            List<TaskPackage> taskPackages = taskPackageRepository.findByIdIn(taskPackageIds);

            Map<Long, Object> taskPackage = new HashMap<>();

            if (taskPackages.size() > 0) {
                for (TaskPackage item : taskPackages) {
                    taskPackage.put(item.getId(), item);
                }
            }
            return taskPackage;
        }

        return new HashMap<>();
    }

    /**
     * 删除 增加的实体 生成的四级计划 及关系。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/deleted-added-wbs/{originalBatchTaskId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonResponseBody deleteBindedEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "原来生成计划的批处理 ID") Long originalBatchTaskId
    ) {
        final Project project = projectService.get(orgId, projectId);
        final ContextDTO context = getContext();
        final OperatorDTO operator = context.getOperator();

        context.setRequestMethod(context.getRequest().getMethod());
        batchTaskService
            .conflictWith(projectId, PROJECT_PLAN_IMPORT, ENTITY_PROCESS_WBS_GENERATE)
            .run(
                context,
                project,
                ENTITY_PROCESS_WBS_GENERATE,
                batchTask -> {
                    BatchResultDTO result = planImportService
                        .deleteGenerateWbsEntryFromAddedWbsEntity(batchTask, operator, orgId, projectId, originalBatchTaskId);

                    return result;
                }
            );

        getResponse().setHeader(
            DATA_REVISION,
            "" + wbsSearchService.getWBSVersion(projectId)
        );

        return new JsonResponseBody();
    }

    /**
     * 恢复服务。
     */
    @RequestMapping(method = POST, value = "/plan-queue",
        produces = APPLICATION_JSON_VALUE)
    public void getServerContext(
        @RequestBody EventModel eventModel
    ) {
        ContextDTO contextDTO = getContext();
        OperatorDTO operatorDTO = new OperatorDTO(eventModel.getOperatorId());
        contextDTO.setOperator(operatorDTO);
        String authorization = eventModel.getAuthorization();
        String userAgent = eventModel.getUserAgent();
        String taskDefKey = eventModel.getTaskDefKey();
        if (!contextDTO.isContextSet()) {
            final RequestAttributes attributes = new ServletRequestAttributes(
                new RequestWrapper(contextDTO.getRequest(), authorization, userAgent),
                null
            );

            RequestContextHolder.setRequestAttributes(attributes, true);
            contextDTO.setContextSet(true);
        }

        AuthCheckDTO authCheckDTO = new AuthCheckDTO();
        authCheckDTO.setAuthorization(authorization);
        authCheckDTO.setUserAgent(userAgent);
        authCheckDTO.setRemoteAddr(contextDTO.getRemoteAddr());

        privilegeAPI
            .checkAuth(authCheckDTO);

        Long projectId = eventModel.getProjectId();
        Project project = projectRepository.findById(projectId).orElse(null);
        Long orgId = project.getOrgId();
        Long wbsEntryId = eventModel.getEntityId();


        batchTaskService
            .conflictWith(projectId, PROJECT_PLAN_IMPORT, ENTITY_PROCESS_WBS_GENERATE)
            .run(
                contextDTO,
                project,
                ENTITY_PROCESS_WBS_GENERATE,
                batchTask -> {
                    BatchResultDTO result = null;
                    if ("ADDED".equalsIgnoreCase(taskDefKey)) {
                        result = planImportService
                            .generateWbsEntryFromAddedWbsEntity(batchTask, operatorDTO, orgId, projectId, wbsEntryId);
                    } else {
                        result = planImportService
                            .generateEntityProcessWBSEntries(batchTask, operatorDTO, orgId, projectId, wbsEntryId, true);
                    }
                    return result;
                }
            );

    }

}
