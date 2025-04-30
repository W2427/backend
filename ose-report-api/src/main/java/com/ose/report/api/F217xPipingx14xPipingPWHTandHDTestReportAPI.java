package com.ose.report.api;

import com.ose.report.dto.F217xPipingx14xPipingPWHTandHDTestReportDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
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
 * 检查单报表接口
 */
@FeignClient(name = "ose-report", contextId = "f217PiPwhtHdFeign")
public interface F217xPipingx14xPipingPWHTandHDTestReportAPI {

    /**
     * PT报告
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param reportPostDTO 报告数据
     * @return 制作完成的检查单
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/piping-pwht-test-reports",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<ReportHistory> postPipingPwhtTestReport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody F217xPipingx14xPipingPWHTandHDTestReportDTO reportPostDTO
    );
}
