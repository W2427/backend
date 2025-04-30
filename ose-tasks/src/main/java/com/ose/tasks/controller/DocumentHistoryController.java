package com.ose.tasks.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.DocumentHistoryAPI;
import com.ose.tasks.domain.model.service.DocumentHistoryInterface;
import com.ose.tasks.dto.DocumentUploadHistorySearchDTO;
import com.ose.tasks.dto.bpm.BpmExInspConfirmResponseDTO;
import com.ose.tasks.dto.drawing.DocumentUploadDTO;
import com.ose.tasks.dto.drawing.ProofreadSubDrawingPreviewDTO;
import com.ose.tasks.entity.DocumentHistory;
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
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "上传文档计算数值")
@RestController
@RequestMapping(value = "/document/history")
public class DocumentHistoryController extends BaseController implements DocumentHistoryAPI {

    private final static Logger logger = LoggerFactory.getLogger(DocumentHistoryController.class);

    @Value("${application.files.temporary}")
    private String temporaryDir;

    private final DocumentHistoryInterface documentHistoryService;

    @Autowired
    public DocumentHistoryController(DocumentHistoryInterface documentHistoryService) {
        this.documentHistoryService = documentHistoryService;
    }


    @RequestMapping(method = GET, value = "")
    @Operation(summary = "sacs上传历史", description = "查询sacs上传历史记录")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<DocumentHistory> uploadHistories(
        DocumentUploadHistorySearchDTO pageDTO) {
        Long operatorId = getContext().getOperator().getId();
        return new JsonListResponseBody<>(
            documentHistoryService.uploadHistories(pageDTO, operatorId));
    }

    @Override
    @Operation(summary = "上传文档", description = "上传文档")
    @RequestMapping(method = POST, value = "/upload", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<BpmExInspConfirmResponseDTO> uploadDocument(
        @RequestBody DocumentUploadDTO uploadDTO) throws FileNotFoundException {
        return new JsonObjectResponseBody<>(
            documentHistoryService.uploadDocument(
                uploadDTO,
                getContext().getOperator(),
                getContext())
        );

    }

    @Override
    @Operation(
        summary = "修改文件信息",
        description = "修改文件信息"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody modify(
        @PathVariable @Parameter(description = "id") Long id,
        @RequestBody DocumentUploadDTO documentUploadDTO) {
        return new JsonObjectResponseBody<>(
            getContext(),
            documentHistoryService.modify(getContext().getOperator(), getContext(), id, documentUploadDTO)
        );
    }

    @Override
    @Operation(
        summary = "删除文件信息",
        description = "删除文件信息"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(@PathVariable @Parameter(description = "id") Long id) {
        documentHistoryService.delete(getContext().getOperator(), id);
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "查询文件信息",
        description = "查询文件信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<DocumentHistory> get(
        @PathVariable @Parameter(description = "id") Long id) {
        return new JsonObjectResponseBody<>(
            getContext(),
            documentHistoryService.get(id)
        );
    }

    @Override
    @Operation(
        summary = "文件预览PDF",
        description = "文件预览PDF"
    )
    @RequestMapping(
        method = GET,
        value = "/preview/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ProofreadSubDrawingPreviewDTO> preview(
        @PathVariable @Parameter(description = "文件 id") Long id
    ) {
        return new JsonObjectResponseBody<>(
            documentHistoryService.preview(
                id
            )
        );
    }


}
