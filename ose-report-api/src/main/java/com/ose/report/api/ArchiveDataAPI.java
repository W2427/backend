package com.ose.report.api;

import com.ose.report.dto.statistics.*;
import com.ose.report.vo.ArchiveDataGroupKey;
import com.ose.report.vo.ArchiveDataType;
import com.ose.report.vo.ArchiveScheduleType;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 归档数据查询接口。
 */
public interface ArchiveDataAPI {

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/archive-data/{archiveType}"
    )
    JsonResponseBody archive(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("archiveType") ArchiveDataType archiveType
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/archive-data/{archiveType}/archive-dates"
    )
    JsonListResponseBody<ArchiveTimeDTO> archiveDates(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("archiveType") ArchiveDataType archiveType
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/archive-data/{archiveType}/{archiveYear:[0-9]+}-{archiveMonth:[0-9]+}-{archiveDay:[0-9]+}/{keyNames}"
    )
    JsonObjectResponseBody<ArchiveDataPeriodsDTO> groupKeys(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("archiveType") ArchiveDataType archiveType,
        @PathVariable("archiveYear") int archiveYear,
        @PathVariable("archiveMonth") int archiveMonth,
        @PathVariable("archiveDay") int archiveDay,
        @PathVariable("keyNames") ArchiveDataGroupKey[] keyNames,
        @RequestParam("scheduleType") ArchiveScheduleType scheduleType,
        ArchiveDataGroupDTO groupDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/archive-data/{archiveType}/{archiveYear:[0-9]+}-{archiveMonth:[0-9]+}-{archiveDay:[0-9]+}"
    )
    JsonObjectResponseBody<ArchiveDataPeriodDataDTO> periodData(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("archiveType") ArchiveDataType archiveType,
        @PathVariable("archiveYear") int archiveYear,
        @PathVariable("archiveMonth") int archiveMonth,
        @PathVariable("archiveDay") int archiveDay,
        @RequestParam("scheduleType") ArchiveScheduleType scheduleType,
        @RequestParam(name = "fetchLast", required = false) List<String> fetchLast,
        ArchiveDataDateRangeDTO dateRangeDTO,
        ArchiveDataGroupDTO groupDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/archive-summary/{archiveType}"
    )
    JsonObjectResponseBody<ArchiveDataPeriodDataDTO> archiveSummary(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("archiveType") ArchiveDataType archiveType,
        @RequestParam("scheduleType") ArchiveScheduleType scheduleType,
        @RequestParam(name = "groupKey", required = false) List<String> groupKeys,
        @RequestParam(name = "fetchLast", required = false) List<String> fetchLast,
        @RequestParam(name = "archiveDate", required = false) String archiveDate,
        ArchiveDataDateRangeDTO dateRangeDTO
    );
}
