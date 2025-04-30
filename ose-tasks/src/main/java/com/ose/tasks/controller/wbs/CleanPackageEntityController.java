package com.ose.tasks.controller.wbs;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.wbs.CleanPackageEntityAPI;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.CLPEntryCriteriaDTO;
import com.ose.tasks.dto.CleanPackageEntryUpdateDTO;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.entity.CleanPackageEntityBase;
import com.ose.util.BeanUtils;
import io.swagger.annotations.Api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Api(description = "清洁包实体管理接口")
@RestController
public class CleanPackageEntityController extends BaseController implements CleanPackageEntityAPI {


    private BaseWBSEntityInterface<CleanPackageEntityBase, WBSEntryCriteriaBaseDTO> clpEntityService;


    private ProjectInterface projectService;

    /**
     * 构造方法。
     */
    @Autowired
    public CleanPackageEntityController(
        BaseWBSEntityInterface<CleanPackageEntityBase, WBSEntryCriteriaBaseDTO> clpEntityService,
        ProjectInterface projectService
    ) {
        this.clpEntityService = clpEntityService;
        this.projectService = projectService;
    }

    @Override
    @Operation(summary = "查询清洁包实体")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/clean-package-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<? extends CleanPackageEntityBase> searchCleanPackageEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        CLPEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            clpEntityService.search(orgId, projectId, criteriaDTO, pageDTO)
        );
    }

    @Override
    @Operation(summary = "取得清洁包实体详细信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/clean-package-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<CleanPackageEntityBase> getCleanPackageEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId
    ) {
        return new JsonObjectResponseBody<>(
            clpEntityService.get(orgId, projectId, entityId)
        );
    }


    @Override
    @Operation(summary = "更新清洁包实体")
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/clean-package-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege()
    public JsonResponseBody updateCleanPackageEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody @Parameter(description = "试压包实体信息") CleanPackageEntryUpdateDTO clpEntryUpdateDTO
    ) {

        final OperatorDTO operator = getContext().getOperator();
        projectService.get(orgId, projectId, version);

        CleanPackageEntityBase clpEntityBase = clpEntityService.get(orgId, projectId, entityId);
        CleanPackageEntityBase clpEntity = BeanUtils.copyProperties(clpEntityBase, new CleanPackageEntityBase());
        BeanUtils.copyProperties(clpEntryUpdateDTO, clpEntity);

        clpEntity.setRemarks(clpEntryUpdateDTO.getRemarks());


        if (null != clpEntryUpdateDTO.getCleanPressure()) {
            clpEntity.setCleanPressureText(clpEntryUpdateDTO.getCleanPressure()
                + (clpEntryUpdateDTO.getCleanPressureUnit() == null ? "" : clpEntryUpdateDTO.getCleanPressureUnit().toString()));
        }

        CleanPackageEntityBase entityResult = clpEntityService.update(operator, orgId, projectId, clpEntity);
        return new JsonObjectResponseBody<>(this.getContext(), entityResult);
    }

    /**
     * 删除清洁包实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     * @param version   项目版本号
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/clean-package-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(summary = "删除清洁包实体")
    @WithPrivilege
    @Override
    public JsonResponseBody deleteCleanPackageEntity(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                                     @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                     @PathVariable @Parameter(description = "实体 ID") Long entityId,
                                                     @RequestParam @Parameter(description = "项目更新版本号") long version) {

        Project project = projectService.get(orgId, projectId, version);

        clpEntityService.delete(
            getContext().getOperator(),
            orgId,
            project,
            entityId
        );


        return new JsonResponseBody(this.getContext());
    }

    /**
     * 按条件下载清洁包实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/clean-package-entities/download"
    )
    @Operation(summary = "按条件下载清洁包实体列表")
    @WithPrivilege
    @Override
    public synchronized void downloadCLPEntities(@PathVariable("orgId") Long orgId,
                                    @PathVariable("projectId") Long projectId,
                                    CLPEntryCriteriaDTO criteriaDTO) throws IOException {
        final OperatorDTO operator = getContext().getOperator();
        File excel = clpEntityService.saveDownloadFile(orgId, projectId, criteriaDTO, operator.getId());

        HttpServletResponse response = getContext().getResponse();

        try {

            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-project-entities-clp.xlsx\""
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

}
