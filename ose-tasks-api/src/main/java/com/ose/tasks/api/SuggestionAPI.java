package com.ose.tasks.api;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.SuggestionAddDTO;
import com.ose.tasks.dto.SuggestionEditDTO;
import com.ose.tasks.dto.SuggestionSearchDTO;
import com.ose.tasks.entity.Suggestion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping(value = "/system/suggestion")
public interface SuggestionAPI {

    /**
     * 新增
     */
    @RequestMapping(method = POST, value = "/create")
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<Suggestion> create(
        @RequestBody SuggestionAddDTO dto);


    @RequestMapping(method = GET, value = "")
    @ResponseStatus(OK)
    JsonListResponseBody<Suggestion> getList(
        SuggestionSearchDTO dto);


    @RequestMapping(
        method = GET,
        value = "/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<Suggestion> get(
        @PathVariable("id") Long id
    );

    @Operation(
        summary = "修改意见",
        description = "修改意见"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody modify(
        @PathVariable @Parameter(description = "id") Long id,
        @RequestBody SuggestionEditDTO dto);


    @Operation(
        summary = "关闭意见",
        description = "关闭意见"
    )
    @RequestMapping(
        method = POST,
        value = "/close/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody close(
        @PathVariable @Parameter(description = "id") Long id);

//    /**
//     * 上传报告文件
//     */
//    @RequestMapping(method = POST, value = "/upload")
//    JsonObjectResponseBody<BpmExInspConfirmResponseDTO> uploadDocument(
//        @RequestBody DocumentUploadDTO uploadDTO
//    ) throws FileNotFoundException;
//

//    /*
//     * 文件预览
//     */
//    @RequestMapping(
//        method = GET,
//        value = "/preview/{id}",
//        consumes = ALL_VALUE,
//        produces = APPLICATION_JSON_VALUE
//    )
//    @ResponseStatus(OK)
//    JsonObjectResponseBody<ProofreadSubDrawingPreviewDTO> preview(
//        @PathVariable("id") Long id
//    );
}
