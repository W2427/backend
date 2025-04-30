package com.ose.report.api;

import com.ose.report.dto.InspectionReportPostDTO;
import com.ose.report.dto.InspectionReportApplicationFormPostDTO;
import com.ose.report.dto.InspectionApplicationPostDTO;
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
@FeignClient(name = "ose-report", contextId = "piFitupReportFeign")
public interface PipelineFitUpReportFeignAPI {

    /**
     * 管线组对检验报告
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param reportPostDTO 报告数据
     * @return 制作完成的检查单
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/pipeline-fit-up-inspection-reports",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<ReportHistory> postPipelineFitUpInspectionReport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody InspectionReportPostDTO reportPostDTO
    );

    /**
     * 管线组对内检检查单
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param reportPostDTO 报告数据
     * @return 制作完成的检查单
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/pipeline-fit-up-inspection-applications",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<ReportHistory> postApplicationForPipelineFitUpInspection(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody InspectionApplicationPostDTO reportPostDTO
    );


    /**
     * 管线组对内检检查单封面
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param reportPostDTO 报告数据
     * @return 制作完成的检查单
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/pipeline-fit-up-inspection-application-forms",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<ReportHistory> postApplicationFormForPipelineFitUpInspection(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody InspectionReportApplicationFormPostDTO reportPostDTO
    );

}
