package com.ose.tasks.api.holiday;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.holiday.HolidayCreateDTO;
import com.ose.tasks.dto.holiday.HolidaySearchDTO;
import com.ose.tasks.entity.holiday.HolidayData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping(value = "/system/holiday")
public interface HolidayAPI {
    /**
     * 获取节假日清单列表
     *
     * @return 节假日清单列表
     */
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<HolidayData> list(
        HolidaySearchDTO holidaySearchDTO
    );

    @Operation(
        summary = "创建节假日",
        description = "创建节假日。"
    )
    @RequestMapping(
        method = POST
    )
    @ResponseStatus(CREATED)
    JsonResponseBody create(
        @RequestBody @Parameter(description = "节假日信息") HolidayCreateDTO holidayCreateDTO);


    @Operation(
        summary = "编辑节假日",
        description = "编辑节假日"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}"
    )
    @ResponseStatus(OK)
    JsonResponseBody edit(
        @PathVariable @Parameter(description = "节假日id") Long id,
        @RequestBody @Parameter(description = "节假日信息") HolidayCreateDTO holidayCreateDTO

    );

    @Operation(
        summary = "获取节假日详细信息",
        description = "获取节假日详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<HolidayData> detail(
        @PathVariable @Parameter(description = "节假日id") Long id);

    @Operation(
        summary = "删除节假日",
        description = "删除节假日"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody delete(
        @PathVariable @Parameter(description = "节假日id") Long id);

}
