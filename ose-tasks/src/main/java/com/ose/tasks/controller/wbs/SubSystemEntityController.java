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
import com.ose.tasks.api.wbs.SubSystemEntityAPI;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.SubSystemEntryCriteriaDTO;
import com.ose.tasks.dto.SubSystemEntryUpdateDTO;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.entity.SubSystemEntityBase;
import com.ose.util.BeanUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "子系统实体管理接口")
@RestController
public class SubSystemEntityController extends BaseController implements SubSystemEntityAPI {


    private BaseWBSEntityInterface<SubSystemEntityBase, WBSEntryCriteriaBaseDTO> ssEntityService;


    private ProjectInterface projectService;

    /**
     * 构造方法。
     */
    @Autowired
    public SubSystemEntityController(
        BaseWBSEntityInterface<SubSystemEntityBase, WBSEntryCriteriaBaseDTO> ssEntityService,
        ProjectInterface projectService
    ) {
        this.ssEntityService = ssEntityService;
        this.projectService = projectService;
    }

    @Override
    @Operation(description = "查询子系统实体")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/sub-system-entities",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<? extends SubSystemEntityBase> searchSubSystemEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        SubSystemEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            ssEntityService.search(orgId, projectId, criteriaDTO, pageDTO)
        );
    }

    @Override
    @Operation(description = "取得子系统实体详细信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/sub-system-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<SubSystemEntityBase> getSubSystemEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId
    ) {
        return new JsonObjectResponseBody<>(
            ssEntityService.get(orgId, projectId, entityId)
        );
    }


    @Override
    @Operation(description = "更新子系统实体")
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/sub-system-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody updateSubSystemEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody @Parameter(description = "子系统实体信息") SubSystemEntryUpdateDTO ssEntryUpdateDTO
    ) {

        final OperatorDTO operator = getContext().getOperator();
        projectService.get(orgId, projectId, version);

        SubSystemEntityBase ssEntityBase = ssEntityService.get(orgId, projectId, entityId);
        SubSystemEntityBase ssEntity = BeanUtils.copyProperties(ssEntityBase, new SubSystemEntityBase());
        BeanUtils.copyProperties(ssEntryUpdateDTO, ssEntity);

        ssEntity.setRemarks(ssEntryUpdateDTO.getRemarks());

        SubSystemEntityBase entityResult = ssEntityService.update(operator, orgId, projectId, ssEntity);
        return new JsonObjectResponseBody<>(getContext(), entityResult);
    }

    /**
     * 删除子系统实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     * @param version   项目版本号
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/sub-system-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(description = "删除子系统实体")
    @WithPrivilege
    @Override
    public JsonResponseBody deleteSubSystemEntity(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                                  @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                  @PathVariable @Parameter(description = "实体 ID") Long entityId,
                                                  @RequestParam @Parameter(description = "项目更新版本号") long version) {

        Project project = projectService.get(orgId, projectId, version);

        ssEntityService.delete(
            getContext().getOperator(),
            orgId,
            project,
            entityId
        );


        return new JsonResponseBody(getContext());
    }

    /**
     * 按条件下载子系统实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/sub-system-entities/download"
    )
    @Operation(description = "按条件下载子系统实体列表")
    @WithPrivilege
    @Override
    public synchronized void downloadSubSystemEntities(@PathVariable("orgId") Long orgId,
                                          @PathVariable("projectId") Long projectId,
                                          SubSystemEntryCriteriaDTO criteriaDTO) throws IOException {

        final OperatorDTO operator = getContext().getOperator();
        File excel = ssEntityService.saveDownloadFile(orgId, projectId, criteriaDTO, operator.getId());

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-project-entities-ss.xlsx\""
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
