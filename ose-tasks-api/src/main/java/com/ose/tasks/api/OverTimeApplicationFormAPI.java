package com.ose.tasks.api;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.OverTimeApplicationFormSearchDTO;
import com.ose.tasks.dto.drawing.OverTimeApplicationFormCreateDTO;
import com.ose.tasks.dto.drawing.OverTimeApplicationFormFilterDTO;
import com.ose.tasks.dto.drawing.OverTimeApplicationFormHandleDTO;
import com.ose.tasks.dto.drawing.OverTimeApplicationFormTransferDTO;
import com.ose.tasks.entity.OverTimeApplicationForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.text.ParseException;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping(value = "/overTime/applyForm")
public interface OverTimeApplicationFormAPI {

    @Operation(
        summary = "search",
        description = "search"
    )
    @RequestMapping(method = GET)
    @ResponseStatus(OK)
    JsonListResponseBody<OverTimeApplicationForm> getList(
        OverTimeApplicationFormSearchDTO dto);

    @Operation(
        summary = "searchAll",
        description = "searchAll"
    )
    @RequestMapping(method = GET, value = "/all")
    @ResponseStatus(OK)
    JsonListResponseBody<OverTimeApplicationForm> getAllList(
        OverTimeApplicationFormSearchDTO dto);



    @Operation(
        summary = "create",
        description = "create"
    )
    @RequestMapping(
        method = POST
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<OverTimeApplicationForm> create(
        @RequestBody OverTimeApplicationFormCreateDTO dto
    ) throws ParseException;


    @Operation(
        summary = "update",
        description = "update"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody modify(
        @PathVariable @Parameter(description = "id") Long id,
        @RequestBody OverTimeApplicationFormCreateDTO dto) throws ParseException;



    @Operation(
        summary = "delete",
        description = "delete"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "id") Long id);


    @Operation(
        summary = "detail",
        description = "detail"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<OverTimeApplicationForm> get(
        @PathVariable("id") Long id
    );

    @Operation(
        summary = "handle",
        description = "handle"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}/handle"
    )
    @ResponseStatus(OK)
    public JsonResponseBody handle(
        @PathVariable("id") Long id,
        @RequestBody OverTimeApplicationFormHandleDTO dto
    );

    @Operation(
        summary = "review-handle",
        description = "review-handle"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}/review-handle"
    )
    @ResponseStatus(OK)
    public JsonResponseBody reviewHandle(
        @PathVariable("id") Long id,
        @RequestBody OverTimeApplicationFormHandleDTO dto
    );

    @RequestMapping(
        method = GET,
        value = "/filter",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<OverTimeApplicationFormFilterDTO> filter();

    @Operation(
        summary = "transfer",
        description = "transfer"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}/transfer"
    )
    @ResponseStatus(OK)
    public JsonResponseBody transfer(
        @PathVariable("id") Long id,
        @RequestBody OverTimeApplicationFormTransferDTO dto
    );

    @RequestMapping(
        method = GET,
        value = "/download"
    )
    void attendanceDownload(
        OverTimeApplicationFormSearchDTO criteriaDTO
    ) throws IOException, ParseException;
}
