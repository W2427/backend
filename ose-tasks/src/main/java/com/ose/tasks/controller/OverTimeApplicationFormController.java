package com.ose.tasks.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.OverTimeApplicationFormAPI;
import com.ose.tasks.domain.model.service.OverTimeApplicationFormInterface;
import com.ose.tasks.dto.OverTimeApplicationFormSearchDTO;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.OverTimeApplicationForm;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "加班申请")
@RestController
@RequestMapping(value = "/overTime/applyForm")
public class OverTimeApplicationFormController extends BaseController implements OverTimeApplicationFormAPI {

    private final static Logger logger = LoggerFactory.getLogger(OverTimeApplicationFormController.class);

    private final OverTimeApplicationFormInterface overTimeApplicationFormService;

    @Autowired
    public OverTimeApplicationFormController(OverTimeApplicationFormInterface overTimeApplicationFormService) {
        this.overTimeApplicationFormService = overTimeApplicationFormService;
    }


    @Override
    @Operation(
        summary = "search",
        description = "search。"
    )
    @RequestMapping(
        method = GET
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<OverTimeApplicationForm> getList(
        OverTimeApplicationFormSearchDTO dto) {

        return new JsonListResponseBody<>(
            overTimeApplicationFormService
                .getList(dto, getContext().getOperator().getId())
        );
    }

    @Override
    @Operation(
        summary = "searchAll",
        description = "searchAll。"
    )
    @RequestMapping(
        method = GET, value = "/all"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<OverTimeApplicationForm> getAllList(
        OverTimeApplicationFormSearchDTO dto) {

        return new JsonListResponseBody<>(
            overTimeApplicationFormService
                .getAllList(dto, getContext().getOperator().getId())
        );
    }

    @Override
    @Operation(
        summary = "create",
        description = "create"
    )
    @RequestMapping(
        method = POST
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<OverTimeApplicationForm> create(
        @RequestBody OverTimeApplicationFormCreateDTO dto) throws ParseException {

        return new JsonObjectResponseBody<>(
            getContext(),
            overTimeApplicationFormService.create(getContext().getOperator(), dto)
        );
    }

    @Override
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
        @PathVariable Long id,
        @RequestBody OverTimeApplicationFormCreateDTO dto) throws ParseException {
        overTimeApplicationFormService.modify(getContext().getOperator(), getContext(), id, dto);
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "delete",
        description = "delete"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable Long id) {
        overTimeApplicationFormService.delete(getContext().getOperator(), id);
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "detail",
        description = "detail"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<OverTimeApplicationForm> get(
        @PathVariable Long id) {
        return new JsonObjectResponseBody<>(
            getContext(),
            overTimeApplicationFormService.get(id)
        );
    }

    @Override
    @Operation(
        summary = "handle",
        description = "handle"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}/handle"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody handle(
        @PathVariable("id") Long id,
        @RequestBody OverTimeApplicationFormHandleDTO dto) {
        overTimeApplicationFormService.handle(
            id,
            dto,
            getContext().getOperator()
        );
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "review-handle",
        description = "review-handle"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}/review-handle"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody reviewHandle(
        @PathVariable("id") Long id,
        @RequestBody OverTimeApplicationFormHandleDTO dto) {
        overTimeApplicationFormService.reviewHandle(
            id,
            dto,
            getContext().getOperator()
        );
        return new JsonResponseBody();
    }

    @RequestMapping(
        method = GET,
        value = "filter",
        consumes = MediaType.ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonObjectResponseBody<OverTimeApplicationFormFilterDTO> filter() {
        return (new JsonObjectResponseBody<>(
            getContext(),
            overTimeApplicationFormService.filter()
        ));
    }

    @Override
    @Operation(
        summary = "transfer",
        description = "transfer"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}/transfer"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody transfer(
        @PathVariable("id") Long id,
        @RequestBody OverTimeApplicationFormTransferDTO dto) {
        overTimeApplicationFormService.transfer(
            id,
            dto,
            getContext().getOperator()
        );
        return new JsonResponseBody();
    }

    /**
     *
     * @param criteriaDTO
     * @throws IOException
     */
    @RequestMapping(
        method = GET,
        value = "/download"
    )
    @Operation(description = "按条件下载")
    @WithPrivilege
    @Override
    public void attendanceDownload(
        OverTimeApplicationFormSearchDTO criteriaDTO
    ) throws IOException, ParseException {
        final OperatorDTO operator = getContext().getOperator();
        File excel = overTimeApplicationFormService.saveDownloadFile(criteriaDTO, operator.getId());
        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-attendance-manHour.xlsx\""
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
}
