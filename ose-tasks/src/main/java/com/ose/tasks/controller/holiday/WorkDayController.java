package com.ose.tasks.controller.holiday;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.holiday.WorkDayAPI;
import com.ose.tasks.domain.model.service.holiday.WorkDayInterface;
import com.ose.tasks.dto.holiday.WorkDayCreateDTO;
import com.ose.tasks.dto.holiday.WorkDaySearchDTO;
import com.ose.tasks.entity.holiday.WorkDayData;
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

@Tag(name = "法定周末调休")
@RestController
@RequestMapping(value = "/system/work-day")
public class WorkDayController extends BaseController implements WorkDayAPI {

    private final WorkDayInterface workDayService;

    @Autowired
    public WorkDayController(WorkDayInterface workDayService) {
        this.workDayService = workDayService;
    }

    @Override
    @Operation(
        summary = "获取法定周末调休列表",
        description = "获取法定周末调休列表。"
    )
    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<WorkDayData> list(
        WorkDaySearchDTO workDaySearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            workDayService.search(workDaySearchDTO)
        );
    }

    @Override
    @Operation(
        summary = "创建法定周末调休",
        description = "创建法定周末调休。"
    )
    @RequestMapping(
        method = POST
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonResponseBody create(
        @RequestBody @Parameter(description = "法定周末调休信息") WorkDayCreateDTO workDayCreateDTO) {
        workDayService.create(workDayCreateDTO, getContext());
        return new JsonResponseBody();
    }

//    @Override
//    @Operation(
//        value = "编辑法定周末调休",
//        notes = "编辑法定周末调休"
//    )
//    @RequestMapping(
//        method = POST,
//        value = "/{id}"
//    )
//    @WithPrivilege
//    @ResponseStatus(OK)
//    public JsonResponseBody edit(
//        @PathVariable @Parameter(description = "法定周末调休id") Long id,
//        @RequestBody @Parameter(description = "法定周末调休信息") HolidayCreateDTO holidayCreateDTO
//
//    ) {
//        holidayService.update(id, holidayCreateDTO, getContext());
//        return new JsonResponseBody();
//    }

//    @Override
//    @Operation(
//        value = "获取节假日详细信息",
//        notes = "获取节假日详细信息"
//    )
//    @RequestMapping(
//        method = GET,
//        value = "/{id}",
//        produces = APPLICATION_JSON_VALUE
//    )
//    @WithPrivilege
//    @SetUserInfo
//    @ResponseStatus(OK)
//    public JsonObjectResponseBody<HolidayData> detail(
//        @PathVariable @Parameter(description = "节假日id") Long id) {
//        return new JsonObjectResponseBody<>(
//            getContext(), holidayService.detail(id)
//        );
//    }

    @Override
    @Operation(
        summary = "删除法定周末调休",
        description = "删除法定周末调休"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "法定周末调休id") Long id) {
        workDayService.delete(id, getContext());
        return new JsonResponseBody();
    }
}
