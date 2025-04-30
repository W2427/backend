package com.ose.report.api.completion;

import com.ose.report.dto.completion.McItrDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * TEST PACKAGE MC ITR 检查单报表接口
 */
@FeignClient(name = "ose-report", contextId = "testPkgMcItrFeign")
public interface TestPackageMcItrReportFeignAPI {

    /**
     * 模拟检查单制作
     *
     * @param orgId        组织
     * @param projectId    项目ID
     * @param mcItrDTO 模拟检查单ID
     * @return 制作完成的检查单
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/test-package-mc-itr/generate",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<ReportHistory> generateMcItrReport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody @Parameter(description = "TEST PACKAGE MC ITR内容") McItrDTO mcItrDTO
    );


}
