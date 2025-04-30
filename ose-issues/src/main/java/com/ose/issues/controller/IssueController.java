package com.ose.issues.controller;

import com.ose.auth.annotation.SetOrgInfo;
import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.auth.api.OrganizationFeignAPI;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.feign.RequestWrapper;
import com.ose.issues.api.IssueAPI;
import com.ose.issues.domain.model.service.IssueImportInterface;
import com.ose.issues.domain.model.service.IssueInterface;
import com.ose.issues.dto.*;
import com.ose.issues.entity.Issue;
import com.ose.issues.entity.IssueImportRecord;
import com.ose.issues.entity.IssueImportTemplate;
import com.ose.issues.vo.IssueSource;
import com.ose.issues.vo.IssueType;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ose.constant.HttpResponseHeaders.DATA_REVISION;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@Tag(name = "遗留问题")
public class IssueController extends BaseController implements IssueAPI {

    private final IssueInterface issueService;
    private final IssueImportInterface issueImportService;
    private final OrganizationFeignAPI orgFeignAPI;

    /**
     * 构造方法。
     */
    @Autowired
    public IssueController(
        IssueInterface issueService,
        IssueImportInterface issueImportService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            OrganizationFeignAPI orgFeignAPI) {
        this.issueService = issueService;
        this.issueImportService = issueImportService;
        this.orgFeignAPI = orgFeignAPI;
    }

    @Override
    @Operation(description = "创建遗留问题")
    @ResponseStatus(CREATED)
    @WithPrivilege
    public JsonObjectResponseBody<Issue> create(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Valid IssueCreateDTO issueCreateDTO
    ) {
        return new JsonObjectResponseBody<>(
            issueService.create(
                getContext().getOperator().getId(),
                orgId,
                projectId,
                issueCreateDTO
            )
        );
    }

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/issues/{issueId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @Operation(description = "更新遗留问题信息")
    @WithPrivilege
    public JsonResponseBody update(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "问题 ID") Long issueId,
        @RequestBody @Valid IssueUpdateDTO issueUpdateDTO
    ) {

        Issue issue = issueService.update(
            getContext().getOperator().getId(),
            orgId,
            projectId,
            issueId,
            issueUpdateDTO
        );

        getResponse().setHeader(DATA_REVISION, "" + issue.getVersion());

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "获取遗留问题列表")
    @WithPrivilege
    @SetUserInfo
    @SetOrgInfo
    public JsonListResponseBody<Issue> search(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        IssueCriteriaDTO issueCriteriaDTO,
        PageDTO pageDTO
    ) {

        Page<Issue> issues = issueService.search(
            orgId,
            projectId,
            issueCriteriaDTO,
            pageDTO.toPageable()
        );
//        List<String> departments = issueService.getDepartments(projectId, issueCriteriaDTO);
//
//        List<String> modules = issueService.getModules(projectId, issueCriteriaDTO);
//
//        Map<String, String> columnHeaderMap = issueService.getColumnHeaderMap(projectId);
//
//        List<String> systems = issueService.getSystems(projectId, issueCriteriaDTO);
//
//        List<String> disciplines = issueService.getDisciplines(projectId, issueCriteriaDTO);
//
//        IssueResponseDTO issueResponseDTO = new IssueResponseDTO();
//        issueResponseDTO.setColumnHeaderMap(columnHeaderMap);
//        issueResponseDTO.setDepartments(departments);
//        issueResponseDTO.setIssues(issues);
//        issueResponseDTO.setModules(modules);
//        issueResponseDTO.setSystems(systems);
//        issueResponseDTO.setDisciplines(disciplines);

        return new JsonListResponseBody<>(
            getContext(),
            issues
        );
    }

    @Override
    @Operation(description = "获取遗留问题详情")
    @WithPrivilege
    @SetUserInfo
    @SetOrgInfo
    public JsonObjectResponseBody<Issue> get(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "问题 ID") Long issueId
    ) {
        return new JsonObjectResponseBody<>(
            getContext(),
            issueService.get(projectId, issueId)
        );
    }

    @Override
    @Operation(description = "批量取得问题信息")
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<Issue> batchGet(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody IssueCriteriaDTO issueCriteriaDTO
    ) {
        return new JsonListResponseBody<>(
            getContext(),
            issueService.search(
                orgId,
                projectId,
                issueCriteriaDTO
            )
        );
    }

    @Override
    @Operation(description = "取得遗留问题导入文件")
    @WithPrivilege
    public JsonObjectResponseBody<IssueImportTemplate> getImportFile(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId
    ) {

        IssueImportTemplate template = issueImportService
            .getImportFile(projectId, IssueType.ISSUE);

        if (template == null) {
            template = issueImportService.generateImportTemplate(
                getContext().getOperator().getId(),
                orgId, projectId, IssueType.ISSUE
            );
        }

        return new JsonObjectResponseBody<>(template);
    }

    @Override
    @Operation(description = "导入遗留问题")
    @WithPrivilege
    public JsonResponseBody importIssues(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Valid IssueImportDTO issueImportDTO
    ) {
        ContextDTO context = getContext();
        Long operatorId = context.getOperator().getId();

        String authorization = context.getAuthorization();
        if(!context.isContextSet()) {
            String userAgent = context.getUserAgent();
            RequestAttributes attributes = new ServletRequestAttributes(
                new RequestWrapper(context.getRequest(), authorization, userAgent),
                null
            );

            RequestContextHolder.setRequestAttributes(attributes, true);
            context.setContextSet(true);
        }
        new Thread(() -> {
            System.out.println("START IMPORT");
            issueImportService.importIssues(
                operatorId,
                orgId,
                projectId,
                issueImportDTO
            );


        }).start();

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "移交遗留问题")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/issues-transfer",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody transfer(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestBody IssueTransferDTO issueTransferDTO
    ) {

        issueService.transfer(
            getContext().getOperator().getId(),
            orgId,
            projectId,
            issueTransferDTO
        );

        return new JsonResponseBody();
    }

    @Operation(description = "获取遗留问题部门列表")
    @WithPrivilege
    @Override
    public JsonListResponseBody<IssueDepartmentDTO> departments(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        IssueCriteriaDTO issueCriteriaDTO) {

        List<Map<String, Object>> departmentTuples = issueService.getDepartments(projectId, issueCriteriaDTO);
        List<IssueDepartmentDTO> departmentDTOs = new ArrayList<>();
        departmentTuples.forEach(dt ->{
            BigInteger dId = (BigInteger)dt.get("departmentId");
            Long dlId = null;
            dlId = dId == null ? null : dId.longValue();
            String department = (String) dt.get("department");
            if(StringUtils.isEmpty(department)) return;
            IssueDepartmentDTO issueDepartmentDTO = new IssueDepartmentDTO(department, dlId);

            departmentDTOs.add(issueDepartmentDTO);
        });

        return new JsonListResponseBody<>(
            getContext(),
            departmentDTOs
        );
    }

    @Operation(description = "获取遗留问题来源列表")
    @WithPrivilege
    @Override
    public JsonListResponseBody<IssueSourceDTO> sources(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        IssueCriteriaDTO issueCriteriaDTO) {

        List<Map<String, Object>> sourceTuples = issueService.getSources(projectId, issueCriteriaDTO);
        List<IssueSourceDTO> sources = new ArrayList<>();
        sourceTuples.forEach(dt ->{
            String source = (String) dt.get("punch_source");
            if(source == null || source.equalsIgnoreCase("")) return;
            IssueSourceDTO sourceDTO = new IssueSourceDTO();
            sourceDTO.setSource(source);

            sources.add(sourceDTO);
        });

        return new JsonListResponseBody<>(
            getContext(),
            sources
        );
    }

    @Operation(description = "获取遗留问题来源所有列表")
    @WithPrivilege
    @Override
    public JsonListResponseBody<IssueSourceDTO> getAllSources(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        IssueCriteriaDTO issueCriteriaDTO) {

        IssueSource[] issueSources = IssueSource.values();
        List<IssueSourceDTO> sources = new ArrayList<>();

        for(IssueSource is : issueSources) {
            String source = is.name();
            if(source == null || source.equalsIgnoreCase("")) continue;
            IssueSourceDTO sourceDTO = new IssueSourceDTO();
            sourceDTO.setSource(source);

            sources.add(sourceDTO);
        }

        return new JsonListResponseBody<>(
            getContext(),
            sources
        );
    }

    @Operation(description = "获取遗留问题专业列表")
    @WithPrivilege
    @Override
    public JsonListResponseBody<IssueDisciplineDTO> disciplines(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        IssueCriteriaDTO issueCriteriaDTO) {

        List<Map<String, Object>> disciplineTuples = issueService.getDisciplines(projectId, issueCriteriaDTO);
        List<IssueDisciplineDTO> disciplines = new ArrayList<>();
        disciplineTuples.forEach(dt ->{
            String discipline = (String) dt.get("discipline");
            if(discipline == null || discipline.equalsIgnoreCase("")) return;
            IssueDisciplineDTO disciplineDTO = new IssueDisciplineDTO();
            disciplineDTO.setDiscipline(discipline);

            disciplines.add(disciplineDTO);
        });

        return new JsonListResponseBody<>(
            getContext(),
            disciplines
        );
    }

    @Operation(description = "获取遗留问题模块列表")
    @WithPrivilege
    @Override
    public JsonListResponseBody<IssueModuleDTO> modules(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        IssueCriteriaDTO issueCriteriaDTO) {

        List<Map<String, Object>> moduleTuples = issueService.getModules(projectId, issueCriteriaDTO);
        List<IssueModuleDTO> modules = new ArrayList<>();
        moduleTuples.forEach(dt ->{
            String module = (String) dt.get("module");
            if(module == null || module.equalsIgnoreCase("")) return;
            IssueModuleDTO moduleDTO = new IssueModuleDTO();
            moduleDTO.setModule(module);

            modules.add(moduleDTO);
        });

        return new JsonListResponseBody<>(
            getContext(),
            modules
        );
    }

    @Operation(description = "获取遗留问题子系统列表")
    @WithPrivilege
    @Override
    public JsonListResponseBody<IssueSystemDTO> systems(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        IssueCriteriaDTO issueCriteriaDTO) {

        List<Map<String, Object>> systemTuples = issueService.getSystems(projectId, issueCriteriaDTO);
        List<IssueSystemDTO> systems = new ArrayList<>();
        systemTuples.forEach(dt ->{
            String system = (String) dt.get("sub_system");
            if(system == null || system.equalsIgnoreCase("")) return;
            IssueSystemDTO systemDTO = new IssueSystemDTO();
            systemDTO.setSystem(system);

            systems.add(systemDTO);
        });

        return new JsonListResponseBody<>(
            getContext(),
            systems
        );
    }


    @Operation(description = "导出遗留问题到xls")
    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/issues/export-xls",
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public void export_xls(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        String issueType) throws IOException {
        final OperatorDTO operator = getContext().getOperator();
        File excel = issueService.saveDownloadFile(orgId, projectId, issueType, operator.getId());

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-issue-expierences.xlsx\""
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

    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/punchlist/import-record",
        method = GET,
        consumes = ALL_VALUE
    )
    @WithPrivilege
    @Override
    @SetUserInfo
    public JsonListResponseBody<IssueImportRecord> importHistory(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO pageDTO
    ){
        Page<IssueImportRecord> issueImportRecords = issueImportService.getImportHistory(projectId, pageDTO);
        return new JsonListResponseBody<>(issueImportRecords).setIncluded(issueImportService);

    }

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/issues/{issueId}/open",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @Operation(description = "打开遗留问题")
    @WithPrivilege
    public JsonResponseBody open(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "问题 ID") Long issueId
    ) {

        issueService.open(
            orgId,
            projectId,
            issueId,
            getContext()
        );

        return new JsonResponseBody();
    }


    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/issues/{issueId}/close",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @Operation(description = "关闭遗留问题")
    @WithPrivilege
    public JsonResponseBody close(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "问题 ID") Long issueId
    ) {

        issueService.close(
            orgId,
            projectId,
            issueId,
            getContext()
        );
        return new JsonResponseBody();
    }
}
