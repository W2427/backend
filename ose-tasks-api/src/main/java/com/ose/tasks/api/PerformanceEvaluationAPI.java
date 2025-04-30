package com.ose.tasks.api;

import com.ose.response.JsonListResponseBody;
import com.ose.tasks.dto.PerformanceEvaluationDataDTO;
import com.ose.tasks.dto.PerformanceEvaluationSearchDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;


import java.io.IOException;
import java.text.ParseException;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping(value = "/performanceEvaluation")
public interface PerformanceEvaluationAPI {

    @Operation(
        summary = "search",
        description = "search"
    )
    @RequestMapping(method = GET, value = "/search")
    @ResponseStatus(OK)
    JsonListResponseBody<PerformanceEvaluationDataDTO> getList(
        PerformanceEvaluationSearchDTO dto);

    @RequestMapping(
        method = GET,
        value = "/download"
    )
    void attendanceDownload(
        PerformanceEvaluationSearchDTO criteriaDTO
    ) throws IOException, ParseException;
}
