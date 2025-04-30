package com.ose.tasks.controller.wps.simple;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.wps.simple.WelderSimpleAPI;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.SubconInterface;
import com.ose.tasks.domain.model.service.wps.simple.WelderSimpleInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.HierarchyNodeImportDTO;
import com.ose.tasks.dto.wps.simple.*;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wps.simple.WelderGradeWelderSimplifiedRelation;
import com.ose.tasks.entity.wps.simple.WelderSimplified;
import com.ose.util.LongUtils;
import io.swagger.annotations.Api;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;

import static com.ose.tasks.vo.setting.BatchTaskCode.WELDER_IMPORT;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@Api(description = "简版焊工接口")
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/welder-simple")
public class WelderSimpleController extends BaseController implements WelderSimpleAPI {

    private final static Logger logger = LoggerFactory.getLogger(WelderSimpleController.class);

    private final WelderSimpleInterface welderSimpleService;

    private final SubconInterface subconService;

    private final ProjectInterface projectService;

    private final BatchTaskInterface batchTaskService;

    private final UploadFeignAPI uploadFeignAPI;


    @Autowired
    public WelderSimpleController(
        WelderSimpleInterface welderSimpleService,
        SubconInterface subconService,
        ProjectInterface projectService,
        BatchTaskInterface batchTaskService, UploadFeignAPI uploadFeignAPI) {
        this.welderSimpleService = welderSimpleService;
        this.subconService = subconService;
        this.projectService = projectService;
        this.batchTaskService = batchTaskService;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    /**
     * 创建焊工信息
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param welderSimpleCreateDTO 创建信息
     */
    @Operation(
        summary = "创建焊工",
        description = "创建焊工"
    )
    @PostMapping(
        value = "",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonResponseBody create(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestBody WelderSimpleCreateDTO welderSimpleCreateDTO
    ) {
        ContextDTO context = getContext();
        welderSimpleService.create(
            orgId,
            projectId,
            context,
            welderSimpleCreateDTO
        );

        return new JsonResponseBody();
    }

    /**
     * 获取焊工列表
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param welderSimpleSearchDTO 查询参数
     * @return 焊工列表
     */
    @Operation(
        summary = "获取焊工列表",
        description = "获取焊工列表"
    )
    @GetMapping(
        value = "",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    @SetUserInfo
    public JsonListResponseBody<WelderSimplified> search(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        WelderSimpleSearchDTO welderSimpleSearchDTO
    ) {

        ContextDTO context = getContext();

        return new JsonListResponseBody<>(
            context,
            welderSimpleService.search(
                orgId,
                projectId,
                welderSimpleSearchDTO
            )
        )
            .setIncluded(welderSimpleService);
    }

    /**
     * 获取焊工详情
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param welderSimplifiedId wpsID
     * @return 焊工详情
     */
    @Operation(
        summary = "获取焊工详情",
        description = "获取焊工详情"
    )
    @GetMapping(
        value = "/{welderSimplifiedId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    @SetUserInfo
    public JsonObjectResponseBody<WelderSimplified> get(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "welderSimplifiedId") Long welderSimplifiedId
    ) {

        ContextDTO context = getContext();

        return new JsonObjectResponseBody<>(
            context,
            welderSimpleService.detail(
                orgId,
                projectId,
                welderSimplifiedId
            )
        )
            .setIncluded(welderSimpleService);
    }

    /**
     * 更新焊工信息。
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param welderSimplifiedId    wpsID
     * @param welderSimpleUpdateDTO 更新信息
     */
    @Operation(
        summary = "更新焊工信息",
        description = "更新焊工信息"
    )
    @PatchMapping(
        value = "/{welderSimplifiedId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonResponseBody update(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "welderGradeSimplifiedId") Long welderSimplifiedId,
        @RequestBody WelderSimpleUpdateDTO welderSimpleUpdateDTO
    ) {

        ContextDTO context = getContext();

        welderSimpleService.update(
            orgId,
            projectId,
            welderSimplifiedId,
            context,
            welderSimpleUpdateDTO
        );
        return new JsonResponseBody();
    }

    /**
     * 打开焊工账户
     *
     * @param orgId 组织ID
     * @param projectId 项目ID
     * @param welderId 焊工ID
     * @return
     */
    @Operation(
        summary = "打开焊工账户",
        description = "打开焊工账户"
    )
    @PatchMapping(
        value = "/{welderId}/open",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonResponseBody open(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "焊工ID") Long welderId
    ) {
        ContextDTO contextDTO =getContext();
        welderSimpleService.open(orgId,projectId,welderId,contextDTO);
        return new JsonResponseBody();
    }

    /**
     * 停用焊工账户
     *
     * @param orgId 组织ID
     * @param projectId 项目ID
     * @param welderId 焊工ID
     * @return
     */
    @Operation(
        summary = "停用焊工账户",
        description = "停用焊工账户"
    )
    @PatchMapping(
        value = "/{welderId}/deactivate",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonResponseBody deactivate(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "焊工ID") Long welderId
    ) {
        ContextDTO contextDTO =getContext();
        welderSimpleService.deactivate(orgId,projectId,welderId,contextDTO);
        return new JsonResponseBody();
    }


    /**
     * 删除焊工信息。
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param welderSimplifiedId wpsID
     */
    @Operation(
        summary = "删除焊工信息",
        description = "删除焊工信息"
    )
    @DeleteMapping(
        value = "/{welderSimplifiedId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "welderSimplifiedId") Long welderSimplifiedId
    ) {

        ContextDTO context = getContext();
        welderSimpleService.delete(
            orgId,
            projectId,
            welderSimplifiedId,
            context
        );
        return new JsonResponseBody();
    }

    /**
     * 创建焊工等级
     *
     * @param orgId                组织ID
     * @param projectId            项目ID
     * @param wpsSimpleRelationDTO 创建信息
     */
    @Operation(
        summary = "创建焊工等级",
        description = "创建焊工等级"
    )
    @PostMapping(
        value = "/{welderSimplifiedId}/welderGrade",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonResponseBody addWelderGrade(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "welderSimplifiedId") Long welderSimplifiedId,
        @RequestBody WpsSimpleRelationDTO wpsSimpleRelationDTO
    ) {
        ContextDTO context = getContext();
        welderSimpleService.addWelderGrade(
            orgId,
            projectId,
            welderSimplifiedId,
            wpsSimpleRelationDTO,
            context
        );

        return new JsonResponseBody();
    }

    /**
     * 获取焊工等级列表
     *
     * @param orgId                      组织ID
     * @param projectId                  项目ID
     * @param welderSimplifiedId         项目ID
     * @param wpsSimpleRelationSearchDTO 查询参数
     * @return 焊工列表
     */
    @Operation(
        summary = "获取焊工列表",
        description = "获取焊工列表"
    )
    @GetMapping(
        value = "/{welderSimplifiedId}/welderGrade",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonListResponseBody<WelderGradeWelderSimplifiedRelation> searchWelderGrade(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "welderSimplifiedId") Long welderSimplifiedId,
        WpsSimpleRelationSearchDTO wpsSimpleRelationSearchDTO
    ) {

        ContextDTO context = getContext();

        return new JsonListResponseBody<>(
            context,
            welderSimpleService.searchWelderGrade(
                orgId,
                projectId,
                welderSimplifiedId,
                wpsSimpleRelationSearchDTO
            )
        );
    }

    /**
     * 删除焊工等级。
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param welderSimplifiedId wpsID
     */
    @Operation(
        summary = "删除焊工信息",
        description = "删除焊工信息"
    )
    @DeleteMapping(
        value = "/{welderSimplifiedId}/welderGrade/{welderGradeRelationSimpleId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonResponseBody deleteWelderGrade(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "welderSimplifiedId") Long welderSimplifiedId,
        @PathVariable @Parameter(description = "welderGradeRelationSimpleId") Long welderGradeRelationSimpleId
    ) {

        ContextDTO context = getContext();
        welderSimpleService.deleteWelderGrade(
            orgId,
            projectId,
            welderSimplifiedId,
            welderGradeRelationSimpleId,
            context
        );
        return new JsonResponseBody();
    }

    /**
     * 按条件下载焊工管理列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param welderSimpleSearchDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/download"
    )
    @Operation(
        summary = "按条件下载焊工管理列表%",
        description = "按条件下载焊工管理列表"
    )
    @WithPrivilege
    @Override
    public synchronized void downloadWeldSimpleEntities(@PathVariable("orgId") Long orgId,
                                                        @PathVariable("projectId") Long projectId,
                                                        WelderSimpleSearchDTO welderSimpleSearchDTO) throws IOException {


        final OperatorDTO operator = getContext().getOperator();
        File excel = welderSimpleService.saveDownloadFile(orgId, projectId, welderSimpleSearchDTO, operator.getId());

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-project-entities-welder-simplify.xlsx\""
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
        summary = "导入焊工%",
        description = "请先通过文档管理服务的 <code>POST /orgs/{orgId}/welder-file</code> 接口将导入文件上传到服务器，\"\n" +
            "            + \"导入文件请参照 <a href=\\\"/template/import-welder-template.xlsx\\\">import-welder-template.xlsx</a>。"
    )
    @RequestMapping(
        method = POST,
        value = "/import",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody importWelder(@PathVariable @Parameter(description = "组织ID") Long orgId,
                                         @PathVariable @Parameter(description = "项目ID") Long projectId,
                                         @RequestBody HierarchyNodeImportDTO welderImportDTO) {

        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId);
        ContextDTO context = getContext();

        batchTaskService.run(
            getContext(), project, WELDER_IMPORT,
            batchTask -> {

                BatchResultDTO result = welderSimpleService.importWelder(
                    batchTask,
                    operator,
                    project,
                    context,
                    welderImportDTO
                );

                logger.error("焊工1 保存docs服务->开始");
                JsonObjectResponseBody<FileES> responseBody
                    = uploadFeignAPI.save(
                    orgId.toString(),
                    projectId.toString(),
                    welderImportDTO.getFilename(),
                    new FilePostDTO()
                );
                logger.error("焊工1 保存docs服务->结束");
                batchTask.setImportFile(
                    LongUtils.parseLong(
                        responseBody.getData().getId()
                    ));

                return result;
            }
        );

        return new JsonResponseBody();
    }

}
