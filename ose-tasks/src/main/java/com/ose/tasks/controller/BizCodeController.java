package com.ose.tasks.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.EventModel;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.feign.RequestWrapper;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.Listener.handler.BpmReportHandleErrorHandler;
import com.ose.tasks.api.BizCodeAPI;
import com.ose.tasks.domain.model.service.BizCodeInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.dto.bizcode.BizCodePatchDTO;
import com.ose.tasks.dto.bizcode.BizCodePostDTO;
import com.ose.tasks.dto.bizcode.BizCodeTypeDTO;
import com.ose.tasks.dto.bpm.BpmDelegateClassDTO;
import com.ose.tasks.entity.BizCode;
import com.ose.tasks.entity.Project;
import com.ose.tasks.vo.qc.ReportSubType;
import com.ose.tasks.vo.setting.BizCodeType;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.validation.Valid;
import java.util.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "业务代码参照表")
@RestController
public class BizCodeController extends BaseController implements BizCodeAPI {


    private BizCodeInterface bizCodeService;


    private ProjectInterface projectService;

    private final BpmReportHandleErrorHandler bpmReportHandleErrorHandler;


    /**
     * 构造方法。
     */
    @Autowired
    public BizCodeController(
        BizCodeInterface bizCodeService,
        ProjectInterface projectService,
        BpmReportHandleErrorHandler bpmReportHandleErrorHandler) {
        this.bizCodeService = bizCodeService;
        this.projectService = projectService;
        this.bpmReportHandleErrorHandler = bpmReportHandleErrorHandler;
    }

    @Override
    @Operation(description = "取得系统业务代码列表")
    @RequestMapping(
        method = GET,
        value = "/biz-code-types/{bizCodeType}/biz-codes",
        produces = APPLICATION_JSON_VALUE
    )
    @SetUserInfo
    public JsonListResponseBody<BizCode> list(
        @PathVariable @Parameter(description = "业务代码类型") BizCodeType bizCodeType,
        @Parameter(description = "分页参数") PageDTO pageDTO
    ) {
        return list(null, null, bizCodeType.name(), pageDTO);
    }

    @Override
    @Operation(description = "取得工程业务代码列表")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/biz-code-types/{bizCodeType}/biz-codes",
        produces = APPLICATION_JSON_VALUE
    )
    @SetUserInfo
    @WithPrivilege
    public JsonListResponseBody<BizCode> list(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "业务代码类型") String bizCodeType,
        @Parameter(description = "分页参数") PageDTO pageDTO
    ) {


        BizCodeType[] bizCodeTypes = BizCodeType.values();
        if (!Arrays.deepToString(bizCodeTypes).contains(bizCodeType)) {
            return new JsonListResponseBody<>(
                bizCodeService.list(orgId, projectId, bizCodeType, pageDTO.toPageable())
            );
        }


        return new JsonListResponseBody<>(
            BizCode.list(BizCodeType.valueOf(bizCodeType), pageDTO)
        );

    }

    /**
     * 取得枚举类型的业务代码大类型列表。
     * 这部分是系统业务代码大类型，不可变更
     */
    @RequestMapping(
        method = GET,
        value = "/biz-code-types-test",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @Operation(description = "取得系统业务代码大类型")
    @SetUserInfo
    @WithPrivilege
    public JsonListResponseBody<BizCodeTypeDTO> listSystemBizCodeTypes() {


        BizCodeType[] bizCodeTypes = BizCodeType.values();
        List<BizCodeTypeDTO> bizCodeTypeDTOList = new ArrayList<>();
        for (BizCodeType bizCodeType : bizCodeTypes) {
            BizCodeTypeDTO bizCodeTypeDTO = new BizCodeTypeDTO();
            bizCodeTypeDTO.setType(bizCodeType.name());
            bizCodeTypeDTO.setTypeName(bizCodeType.getDisplayName());
            bizCodeTypeDTOList.add(bizCodeTypeDTO);
        }
        return new JsonListResponseBody<>(bizCodeTypeDTOList);
    }


    /**
     * 添加枚举类型的业务代码大类型。
     * 这部分是系统业务代码大类型
     */
    @Operation(description = "添加枚举类型的业务代码大类型")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/biz-code-types",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonObjectResponseBody<BizCode> addType(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody BizCodePostDTO bizCodePostDTO
    ) {
        Project project = null;

        if (projectId != null) {
            project = projectService.get(orgId, projectId);
        }


        return new JsonObjectResponseBody<>(bizCodeService.add(
            getContext().getOperator(),
            project,
            bizCodePostDTO.getBizCodeType(),
            bizCodePostDTO
        ));
    }


    /**
     * 取得业务代码大类型列表。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/biz-code-types",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @Operation(description = "取得项目业务代码大类型")
    @SetUserInfo
    @WithPrivilege
    public JsonListResponseBody<BizCodeTypeDTO> listBizCodeTypes(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    ) {
        return new JsonListResponseBody<>(
            bizCodeService.listBizCodeType(orgId, projectId)
        );
    }

    @Override
    @Operation(description = "添加系统业务代码")
    @RequestMapping(
        method = POST,
        value = "/biz-code-types/{bizCodeType}/biz-codes",
        produces = APPLICATION_JSON_VALUE
    )
    @SetUserInfo
    @WithPrivilege
    public JsonObjectResponseBody<BizCode> add(
        @PathVariable @Parameter(description = "业务代码类型") BizCodeType bizCodeType,
        @RequestBody @Valid BizCodePostDTO bizCodePostDTO
    ) {
        return add(null, null, bizCodeType.name(), bizCodePostDTO);
    }

    @Override
    @Operation(description = "添加项目业务代码")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/biz-code-types/{bizCodeType}/biz-codes",
        produces = APPLICATION_JSON_VALUE
    )
    @SetUserInfo
    @WithPrivilege
    public JsonObjectResponseBody<BizCode> add(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "业务代码类型") String bizCodeType,
        @RequestBody @Valid BizCodePostDTO bizCodePostDTO
    ) {

        Project project = null;

        if (projectId != null) {
            project = projectService.get(orgId, projectId);
        }

        return new JsonObjectResponseBody<>(bizCodeService.add(
            getContext().getOperator(),
            project,
            bizCodeType,
            bizCodePostDTO
        ));
    }

    @Override
    @Operation(description = "更新系统业务代码")
    @RequestMapping(
        method = PATCH,
        value = "/biz-code-types/{bizCodeType}/biz-codes/{bizCode}",
        produces = APPLICATION_JSON_VALUE
    )
    @SetUserInfo
    @WithPrivilege
    public JsonObjectResponseBody<BizCode> update(
        @PathVariable @Parameter(description = "业务代码类型") BizCodeType bizCodeType,
        @PathVariable @Parameter(description = "业务代码") String bizCode,
        @RequestParam @Parameter(description = "业务代码版本号") Long version,
        @RequestBody @Valid BizCodePatchDTO bizCodePatchDTO
    ) {
        return update(null, null, bizCodeType.name(), bizCode, version, bizCodePatchDTO);
    }

    @Override
    @Operation(description = "更新项目业务代码")
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/biz-code-types/{bizCodeType}/biz-codes/{bizCode}",
        produces = APPLICATION_JSON_VALUE
    )
    @SetUserInfo
    @WithPrivilege
    public JsonObjectResponseBody<BizCode> update(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "业务代码类型") String bizCodeType,
        @PathVariable @Parameter(description = "业务代码") String bizCode,
        @RequestParam @Parameter(description = "业务代码版本号") Long version,
        @RequestBody @Valid BizCodePatchDTO bizCodePatchDTO
    ) {

        Project project = null;

        if (projectId != null) {
            project = projectService.get(orgId, projectId);
        }


        BizCodeType[] bizCodeTypes = BizCodeType.values();
        if (Arrays.deepToString(bizCodeTypes).contains(bizCodeType)) {
            throw new BusinessError("error.biz-code.is-enum");
        }

        return new JsonObjectResponseBody<>(bizCodeService.update(
            getContext().getOperator(),
            project,
            bizCodeType,
            bizCode,
            version,
            bizCodePatchDTO
        ));

    }

    @Override
    @Operation(description = "删除系统业务代码")
    @RequestMapping(
        method = DELETE,
        value = "/biz-code-types/{bizCodeType}/biz-codes/{bizCode}",
        produces = APPLICATION_JSON_VALUE
    )
    @SetUserInfo
    @WithPrivilege
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "业务代码类型") BizCodeType bizCodeType,
        @PathVariable @Parameter(description = "业务代码") String bizCode,
        @RequestParam @Parameter(description = "业务代码版本号") Long version
    ) {
        return delete(null, null, bizCodeType.name(), bizCode, version);
    }

    @Override
    @Operation(description = "删除项目业务代码")
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/biz-code-types/{bizCodeType}/biz-codes/{bizCode}",
        produces = APPLICATION_JSON_VALUE
    )
    @SetUserInfo
    @WithPrivilege
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "业务代码类型") String bizCodeType,
        @PathVariable @Parameter(description = "业务代码") String bizCode,
        @RequestParam @Parameter(description = "业务代码版本号") Long version
    ) {

        Project project = null;

        if (projectId != null) {
            project = projectService.get(orgId, projectId);
        }


        BizCodeType[] bizCodeTypes = BizCodeType.values();
        if (Arrays.deepToString(bizCodeTypes).contains(bizCodeType)) {
            throw new BusinessError("error.biz-code.is-enum");
        }

        if (1 == bizCodeService.countByOrgIdAndProjectIdAndBizCodeType(orgId, projectId, bizCodeType)) {
            throw new BusinessError("", "该大分类只剩1个业务代码，不能删除。");
        }

        bizCodeService.delete(
            getContext().getOperator(),
            project,
            bizCodeType,
            bizCode,
            version
        );

        return new JsonResponseBody();

    }


    /**
     * 查询任务代理类名称列表。
     *
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "/delegate-classes",
        produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "任务代理类列表", description = "任务代理类列表。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BpmDelegateClassDTO> getDelegateClasses() {

        List<BpmDelegateClassDTO> bpmDelegateClasses = new ArrayList<>(bizCodeService.getTaskClasses());
        Collections.sort(bpmDelegateClasses, (a, b) -> StringUtils.compare(a.getDelegateClassName(), b.getDelegateClassName(), true));
        return new JsonListResponseBody<>(getContext(),
            bpmDelegateClasses);
    }

    /**
     * 查询日志代理类名称列表。
     *
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "/log-delegate-classes",
        produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "日志代理类列表", description = "日志代理类列表。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BpmDelegateClassDTO> getLogDelegateClasses() {

        List<BpmDelegateClassDTO> bpmDelegateClasses = new ArrayList<>(bizCodeService.getLogTaskClasses());
        Collections.sort(bpmDelegateClasses, (a, b) -> StringUtils.compare(a.getDelegateClassName(), b.getDelegateClassName(), true));
        return new JsonListResponseBody<>(getContext(),
            bpmDelegateClasses);
    }



    /**
     * 查询报告子类型列表。
     *
     * @return 报告子类型
     */
    @RequestMapping(
        method = GET,
        value = "/report-sub-type/{reportType}",
        produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @WithPrivilege
    @Override
    public JsonListResponseBody<BizCode> getReportSubTypes(@PathVariable("reportType") String reportType) {
            List<BizCode> reportSubTypes = new ArrayList<>();
            for(ReportSubType reportSubType : ReportSubType.values()) {
                if(reportSubType.getReportType().equalsIgnoreCase(reportType)) {
                    BizCode bizCode = new BizCode();
                    bizCode.setCode(reportSubType.name());
                    bizCode.setName(reportSubType.getDisplayName());
                    bizCode.setDescription(reportSubType.getTemplateName());

                    reportSubTypes.add(bizCode);
                }
            }

            return new JsonListResponseBody<>(reportSubTypes);

    }

    /**
     * 恢复服务。
     *
     */
    @RequestMapping(method = POST, value = "/context",
        produces = APPLICATION_JSON_VALUE)
    public void getServerContext(
        @RequestBody EventModel eventModel
    ) {
        ContextDTO contextDTO = getContext();
        String authorization = eventModel.getAuthorization();
        String userAgent = eventModel.getUserAgent();
        if(!contextDTO.isContextSet()) {
            final RequestAttributes attributes = new ServletRequestAttributes(
                new RequestWrapper(contextDTO.getRequest(), authorization, userAgent),
                null
            );


            RequestContextHolder.setRequestAttributes(attributes, true);
            contextDTO.setContextSet(true);
        }

        bpmReportHandleErrorHandler.handleTask(contextDTO, eventModel);

    }
}
