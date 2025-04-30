package com.ose.tasks.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.SuggestionAPI;
import com.ose.tasks.domain.model.service.SuggestionInterface;
import com.ose.tasks.dto.SuggestionAddDTO;
import com.ose.tasks.dto.SuggestionEditDTO;
import com.ose.tasks.dto.SuggestionSearchDTO;
import com.ose.tasks.entity.Suggestion;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "优化意见")
@RestController
@RequestMapping(value = "/system/suggestion")
public class SuggestionController extends BaseController implements SuggestionAPI {

    private final static Logger logger = LoggerFactory.getLogger(SuggestionController.class);

    @Value("${application.files.temporary}")
    private String temporaryDir;

    private final SuggestionInterface suggestionService;

    @Autowired
    public SuggestionController(SuggestionInterface suggestionService) {
        this.suggestionService = suggestionService;
    }


    @Override
    @Operation(
        summary = "create",
        description = "create"
    )
    @RequestMapping(
        method = POST,
        value = "/create"
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<Suggestion> create(
        @RequestBody SuggestionAddDTO dto) {

        return new JsonObjectResponseBody<>(
            getContext(),
            suggestionService.create(getContext().getOperator(), dto,getContext())
        );
    }


    @RequestMapping(method = GET, value = "")
    @Operation(summary = "sacs上传历史", description = "查询sacs上传历史记录")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<Suggestion> getList(
        SuggestionSearchDTO dto) {
        Long operatorId = getContext().getOperator().getId();
        return new JsonListResponseBody<>(
            suggestionService.getList(dto, operatorId));
    }


    @RequestMapping(method = GET, value = "/{id}")
    @Operation(summary = "sacs上传历史", description = "查询sacs上传历史记录")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<Suggestion> get(
        @PathVariable("id") Long id) {
        Long operatorId = getContext().getOperator().getId();
        return new JsonObjectResponseBody<>(
            suggestionService.get(id)
        );
    }

    @Override
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
        @RequestBody SuggestionEditDTO dto) {
        return new JsonObjectResponseBody<>(
            getContext(),
            suggestionService.modify(getContext().getOperator(), getContext(), id, dto)
        );
    }

    @Override
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
    public JsonResponseBody close(@PathVariable @Parameter(description = "id") Long id) {
        suggestionService.close(getContext().getOperator(), id);
        return new JsonResponseBody();
    }



}
