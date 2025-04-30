package com.ose.tasks.api;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.tasks.entity.ExportExcel;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileNotFoundException;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

public interface XlsExportAPI {

    /**
     * 取得所有通用XLS 导出 列表信息。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param pageDTO   分页信息
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/exported-xls",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ExportExcel> list(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO pageDTO
    );


    /**
     * 导出excel。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @Param excelView 导出的excel视图名称
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/export-excel",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    void exportExcel(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestParam("excel_view") String excelView
    ) throws FileNotFoundException;

}
