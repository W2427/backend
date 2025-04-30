package com.ose.tasks.controller.holiday;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.holiday.HolidayAPI;
import com.ose.tasks.domain.model.service.holiday.HolidayInterface;
import com.ose.tasks.dto.holiday.HolidayCreateDTO;
import com.ose.tasks.dto.holiday.HolidaySearchDTO;
import com.ose.tasks.entity.holiday.HolidayData;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "节假日")
@RestController
@RequestMapping(value = "/system/holiday")
public class HolidayController extends BaseController implements HolidayAPI {

    private final HolidayInterface holidayService;

    @Autowired
    public HolidayController(HolidayInterface holidayService) {
        this.holidayService = holidayService;
    }

    @Override
    @Operation(
        summary = "获取节假日列表",
        description = "获取节假日列表。"
    )
    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<HolidayData> list(
        HolidaySearchDTO holidaySearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            holidayService.search(holidaySearchDTO)
        );
    }

    @Override
    @Operation(
        summary = "创建节假日",
        description = "创建节假日。"
    )
    @RequestMapping(
        method = POST
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonResponseBody create(
        @RequestBody @Parameter(description = "节假日信息") HolidayCreateDTO holidayCreateDTO) {
        holidayService.create(holidayCreateDTO, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "编辑节假日",
        description = "编辑节假日"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody edit(
        @PathVariable @Parameter(description = "节假日id") Long id,
        @RequestBody @Parameter(description = "节假日信息") HolidayCreateDTO holidayCreateDTO

    ) {
        holidayService.update(id, holidayCreateDTO, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "获取节假日详细信息",
        description = "获取节假日详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<HolidayData> detail(
        @PathVariable @Parameter(description = "节假日id") Long id) {
        return new JsonObjectResponseBody<>(
            getContext(), holidayService.detail(id)
        );
    }

    @Override
    @Operation(
        summary = "删除节假日",
        description = "删除节假日"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "节假日id") Long id) {
        holidayService.delete(id, getContext());
        return new JsonResponseBody();
    }
}
