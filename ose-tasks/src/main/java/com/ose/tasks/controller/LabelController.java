package com.ose.tasks.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.LabelAPI;
import com.ose.tasks.domain.model.service.LabelInterface;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.Label;
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
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "标签")
@RestController
@RequestMapping(value = "/label")
public class LabelController extends BaseController implements LabelAPI {

    private final static Logger logger = LoggerFactory.getLogger(LabelController.class);

    @Value("${application.files.temporary}")
    private String temporaryDir;

    private final LabelInterface labelService;

    @Autowired
    public LabelController(LabelInterface labelService) {
        this.labelService = labelService;
    }

    /**
     * 获取标签列表
     *
     * @return 标签列表
     */
    @Override
    @Operation(
        summary = "查询标签",
        description = "获取标签列表。"
    )
    @RequestMapping(
        method = GET
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<Label> getList(
        LabelCriteriaDTO criteriaDTO) {

        return new JsonListResponseBody<>(
            labelService.getList(criteriaDTO)
        );
    }

    @Override
    @Operation(
        summary = "查询标签信息",
        description = "查询标签信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<Label> get(
        @PathVariable @Parameter(description = "id") Long id) {
        return new JsonObjectResponseBody<>(
            getContext(),
            labelService.get(id)
        );
    }

    @Override
    @Operation(
        summary = "创建标签",
        description = "根据标签信息，创建标签。"
    )
    @RequestMapping(
        method = POST
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<Label> create(
        @RequestBody @Parameter(description = "工序信息") LabelDTO labelDTO) {

        return new JsonObjectResponseBody<>(
            labelService.create(
                getContext().getOperator(),
                labelDTO
            )
        );
    }


    @Override
    @Operation(
        summary = "编辑标签",
        description = "编辑指定的标签"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<Label> edit(
        @RequestBody @Parameter(description = "工序信息") LabelDTO labelDTO,
        @PathVariable @Parameter(description = "工序id") Long id) {


        return new JsonObjectResponseBody<>(
            labelService.modify(getContext().getOperator(), id, labelDTO)
        );
    }


    @Override
    @Operation(
        summary = "删除标签",
        description = "删除指定的标签"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "工序id") Long id) {
        labelService.delete(id, getContext().getOperator());
        return new JsonResponseBody();
    }
}
