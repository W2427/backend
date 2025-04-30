package com.ose.tasks.api;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.DocumentUploadHistorySearchDTO;
import com.ose.tasks.dto.bpm.BpmExInspConfirmResponseDTO;
import com.ose.tasks.dto.drawing.DocumentUploadDTO;
import com.ose.tasks.dto.drawing.ProofreadSubDrawingPreviewDTO;
import com.ose.tasks.entity.DocumentHistory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.FileNotFoundException;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping(value = "/document/history")
public interface DocumentHistoryAPI {

    /**
     * 上传历史记录
     */
    @RequestMapping(method = GET, value = "")
    @ResponseStatus(OK)
    JsonListResponseBody<DocumentHistory> uploadHistories(
        DocumentUploadHistorySearchDTO pageDTO);

    /**
     * 上传报告文件
     */
    @RequestMapping(method = POST, value = "/upload")
    JsonObjectResponseBody<BpmExInspConfirmResponseDTO> uploadDocument(
        @RequestBody DocumentUploadDTO uploadDTO
    ) throws FileNotFoundException;

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
        @RequestBody DocumentUploadDTO documentUploadDTO);

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
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "id") Long id);

    @RequestMapping(
        method = GET,
        value = "/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<DocumentHistory> get(
        @PathVariable("id") Long id
    );

    /*
     * 文件预览
     */
    @RequestMapping(
        method = GET,
        value = "/preview/{id}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<ProofreadSubDrawingPreviewDTO> preview(
        @PathVariable("id") Long id
    );
}
