package com.ose.tasks.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.api.SacsAPI;
import com.ose.tasks.domain.model.service.SacsInterface;
import com.ose.tasks.dto.SacsUploadHistorySearchDTO;
import com.ose.tasks.dto.bpm.BpmExInspConfirmResponseDTO;
import com.ose.tasks.dto.drawing.DrawingUploadDTO;
import com.ose.tasks.entity.SacsUploadHistory;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.MimeTypeUtils.ALL_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "上传文档计算数值")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class SacsController extends BaseController implements SacsAPI {

    private final static Logger logger = LoggerFactory.getLogger(SacsController.class);

    @Value("${application.files.temporary}")
    private String temporaryDir;

    private final SacsInterface sacsService;

    @Autowired
    public SacsController(SacsInterface sacsService) {
        this.sacsService = sacsService;
    }


    @RequestMapping(method = GET, value = "sacs-upload-histories")
    @Operation(summary = "sacs上传历史", description = "查询sacs上传历史记录")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<SacsUploadHistory> externalInspectionUploadHistories(
        @PathVariable @Parameter(description = "orgId") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        SacsUploadHistorySearchDTO pageDTO) {
        Long operatorId = getContext().getOperator().getId();
        return new JsonListResponseBody<>(
            sacsService.sacsUploadHistories(orgId, projectId, pageDTO, operatorId));
    }

    @Override
    @Operation(summary = "上传文档", description = "上传文档")
    @RequestMapping(method = POST, value = "upload-sacs", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<BpmExInspConfirmResponseDTO> uploadSacs(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody DrawingUploadDTO uploadDTO) throws FileNotFoundException {
        return new JsonObjectResponseBody<>(
            sacsService.uploadSacs(
                orgId,
                projectId,
                uploadDTO,
                getContext().getOperator(),
                getContext())
        );

    }
}
