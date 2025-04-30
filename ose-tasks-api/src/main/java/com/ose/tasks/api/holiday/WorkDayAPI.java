package com.ose.tasks.api.holiday;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.holiday.WorkDayCreateDTO;
import com.ose.tasks.dto.holiday.WorkDaySearchDTO;
import com.ose.tasks.entity.holiday.WorkDayData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping(value = "/system/work-day")
public interface WorkDayAPI {
    /**
     * 获取节假日清单列表
     *
     * @return 节假日清单列表
     */
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<WorkDayData> list(
        WorkDaySearchDTO workDaySearchDTO
    ) throws IOException;

    @Operation(
        summary = "创建节假日",
        description = "创建节假日。"
    )
    @RequestMapping(
        method = POST
    )
    @ResponseStatus(CREATED)
    JsonResponseBody create(
        @RequestBody @Parameter(description = "节假日信息") WorkDayCreateDTO workDayCreateDTO);


//    @Operation(
//        value = "编辑节假日",
//        notes = "编辑节假日"
//    )
//    @RequestMapping(
//        method = POST,
//        value = "/{id}"
//    )
//    @ResponseStatus(OK)
//    JsonResponseBody edit(
//        @PathVariable @Parameter(description = "节假日id") Long id,
//        @RequestBody @Parameter(description = "节假日信息") HolidayCreateDTO holidayCreateDTO
//
//    );

//    @Operation(
//        value = "获取节假日详细信息",
//        notes = "获取节假日详细信息"
//    )
//    @RequestMapping(
//        method = GET,
//        value = "/{id}",
//        produces = APPLICATION_JSON_VALUE
//    )
//    @ResponseStatus(OK)
//    JsonObjectResponseBody<HolidayData> detail(
//        @PathVariable @Parameter(description = "节假日id") Long id);

    @Operation(
        summary = "删除法定周末调休",
        description = "删除法定周末调休"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody delete(
        @PathVariable @Parameter(description = "法定周末调休id") Long id);

}
