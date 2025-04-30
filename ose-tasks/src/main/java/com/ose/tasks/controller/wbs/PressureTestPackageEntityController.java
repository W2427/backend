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
import com.ose.tasks.api.wbs.PressureTestPackageEntityAPI;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;
import com.ose.tasks.dto.wbs.PTPEntryCriteriaDTO;
import com.ose.tasks.dto.wbs.PressureTestPackageEntryUpdateDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.entity.PressureTestPackageEntityBase;
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

@Api(description = "试压包实体管理接口")
@RestController
public class PressureTestPackageEntityController extends BaseController implements PressureTestPackageEntityAPI {


    private BaseWBSEntityInterface<PressureTestPackageEntityBase, WBSEntryCriteriaBaseDTO> ptpEntityService;


    private ProjectInterface projectService;

    /**
     * 构造方法。
     */
    @Autowired
    public PressureTestPackageEntityController(
        BaseWBSEntityInterface<PressureTestPackageEntityBase, WBSEntryCriteriaBaseDTO> ptpEntityService,
        ProjectInterface projectService
    ) {
        this.ptpEntityService = ptpEntityService;
        this.projectService = projectService;
    }

    @Override
    @Operation(summary = "查询试压包实体")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/pressure-test-package-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<? extends PressureTestPackageEntityBase> searchPressureTestPackageEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        PTPEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            ptpEntityService.search(orgId, projectId, criteriaDTO, pageDTO)
        );
    }

    @Override
    @Operation(summary = "取得试压包实体详细信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/pressure-test-package-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<PressureTestPackageEntityBase> getPressureTestPackageEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId
    ) {
        return new JsonObjectResponseBody<>(
            ptpEntityService.get(orgId, projectId, entityId)
        );
    }


    @Override
    @Operation(summary = "更新试压包实体")
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/pressure-test-package-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody updatePressureTestPackageEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody @Parameter(description = "试压包实体信息") PressureTestPackageEntryUpdateDTO ptpEntryUpdateDTO
    ) {

        final OperatorDTO operator = getContext().getOperator();
        projectService.get(orgId, projectId, version);

        PressureTestPackageEntityBase ptpEntityBase = ptpEntityService.get(orgId, projectId, entityId);
        PressureTestPackageEntityBase ptpEntity = BeanUtils.copyProperties(ptpEntityBase, new PressureTestPackageEntityBase());
        BeanUtils.copyProperties(ptpEntryUpdateDTO, ptpEntity);

        ptpEntity.setRemarks(ptpEntryUpdateDTO.getRemarks());


        if (null != ptpEntryUpdateDTO.getTestPressure()) {
            ptpEntity.setTestPressureText(ptpEntryUpdateDTO.getTestPressure()
                + (ptpEntryUpdateDTO.getTestPressureUnit() == null ? "" : ptpEntryUpdateDTO.getTestPressureUnit().toString()));
        }

        if (null != ptpEntryUpdateDTO.getMaxOperatingPressure()) {
            ptpEntity.setMaxOperatingPressureText(ptpEntryUpdateDTO.getMaxOperatingPressure()
                + (ptpEntryUpdateDTO.getMaxOperatingPressureUnit() == null ? "" : ptpEntryUpdateDTO.getMaxOperatingPressureUnit().toString()));
        }


        if (null != ptpEntryUpdateDTO.getMaxDesignPressure()) {
            ptpEntity.setMaxDesignPressureText(ptpEntryUpdateDTO.getMaxDesignPressure()
                + (ptpEntryUpdateDTO.getMaxDesignPressureUnit() == null ? "" : ptpEntryUpdateDTO.getMaxDesignPressureUnit().toString()));
        }

        PressureTestPackageEntityBase entityResult = ptpEntityService.update(operator, orgId, projectId, ptpEntity);
        return new JsonObjectResponseBody<>(this.getContext(), entityResult);
    }

    /**
     * 删除试压包实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     * @param version   项目版本号
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/pressure-test-package-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(summary = "删除试压包实体")
    @WithPrivilege
    @Override
    public JsonResponseBody deletePressureTestPackageEntity(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                                            @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                            @PathVariable @Parameter(description = "实体 ID") Long entityId,
                                                            @RequestParam @Parameter(description = "项目更新版本号") long version) {

        Project project = projectService.get(orgId, projectId, version);

        ptpEntityService.delete(
            getContext().getOperator(),
            orgId,
            project,
            entityId
        );


        return new JsonResponseBody(this.getContext());
    }

    /**
     * 按条件下载试压包实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/pressure-test-package-entities/download"
    )
    @Operation(summary = "按条件下载试压包实体列表")
    @WithPrivilege
    @Override
    public synchronized void downloadPTPEntities(@PathVariable("orgId") Long orgId,
                                    @PathVariable("projectId") Long projectId,
                                    PTPEntryCriteriaDTO criteriaDTO) throws IOException {
        final OperatorDTO operator = getContext().getOperator();
        File excel = ptpEntityService.saveDownloadFile(orgId, projectId, criteriaDTO, operator.getId());

        HttpServletResponse response = getContext().getResponse();

        try {

            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-project-entities-ptp.xlsx\""
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
