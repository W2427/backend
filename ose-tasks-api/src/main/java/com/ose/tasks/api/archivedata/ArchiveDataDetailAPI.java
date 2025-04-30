package com.ose.tasks.api.archivedata;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ose.report.vo.ArchiveDataType;
import com.ose.report.vo.ArchiveScheduleType;
import com.ose.tasks.dto.archivedata.ArchiveDataCriteriaDTO;

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface ArchiveDataDetailAPI {

    /**
     * 下载 归档数据详情信息
     *
     * @throws IOException
     */
    @RequestMapping(
        method = GET,
        value = "archive-data/{archiveType}/details",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    void downloadDataDetailsFile(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("archiveType") ArchiveDataType archiveType,
        @RequestParam("scheduleType") ArchiveScheduleType scheduleType,
        ArchiveDataCriteriaDTO criteriaDTO
    ) throws IOException;
}
