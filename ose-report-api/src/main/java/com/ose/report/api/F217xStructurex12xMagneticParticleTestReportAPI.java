package com.ose.report.api;

import com.ose.report.dto.StructureMagneticParticleTestInspectionReportDTO;
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
@FeignClient(name = "ose-report", contextId = "f217StMtFeign")
public interface F217xStructurex12xMagneticParticleTestReportAPI {

    /**
     * MT报告
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param reportPostDTO 报告数据
     * @return 制作完成的检查单
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/structure-magnetic-particle-test-reports",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<ReportHistory> postStructureMagneticParticleTestReport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody StructureMagneticParticleTestInspectionReportDTO reportPostDTO
    );
}
