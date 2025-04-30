package com.ose.tasks.api.performance;

import com.ose.material.dto.MmImportBatchTaskImportDTO;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.performance.PerformanceAppraisalListImportDTO;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

public interface PerformanceAPI {
    @RequestMapping(
        method = POST,
        value = "/performance-appraisal-list/import"
    )
    @ResponseStatus(OK)
    JsonResponseBody importPerformanceAppraisalList(
        @RequestBody PerformanceAppraisalListImportDTO importDTO);
}
