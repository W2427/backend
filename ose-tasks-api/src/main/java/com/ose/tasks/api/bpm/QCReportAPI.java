package com.ose.tasks.api.bpm;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.ExportFileDTO;
import com.ose.tasks.dto.bpm.QCReportCriteriaDTO;
import com.ose.tasks.dto.bpm.QCReportPackageDTO;
import com.ose.tasks.dto.bpm.ReportUploadDTO;
import com.ose.tasks.entity.report.QCReport;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 工序管理接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface QCReportAPI {


    @RequestMapping(
        method = GET,
        value = "qc-reports"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<QCReport> getList(
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable("projectId") @Parameter(description = "projectId") Long projectId,
        QCReportCriteriaDTO criteriaDTO,
        PageDTO page
    );

    /**
     * 上传图纸文件
     */
    @RequestMapping(
        method = POST,
        value = "upload-qc-report"
    )
    JsonResponseBody uploadReport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody ReportUploadDTO uploadDTO
    );

    /**
     * 补传报告
     */
    @RequestMapping(
        method = POST,
        value = "qc-reports/{reportId}/patch-upload"
    )
    JsonResponseBody patchUploadReport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("reportId") Long reportId,
        @RequestBody ReportUploadDTO uploadDTO
    );

    /**
     * 导出qc报告列表。
     *
     * @param orgId
     * @param projectId
     * @param criteriaDTO
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "export-qc-reports"
    )
    JsonObjectResponseBody<ExportFileDTO> exportQcReport(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projectId") Long projectId,
        QCReportCriteriaDTO criteriaDTO
    );

    /**
     * 打包图纸文件
     */
    @RequestMapping(
        method = POST,
        value = "qcReportId/{qcReportId}/package"
    )
    JsonResponseBody startZip(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody QCReportPackageDTO qcReportPackageDTO
    );
}
