package com.ose.report.controller;

import com.ose.docs.api.UploadFeignAPI;
import com.ose.report.api.WeldStatisticsAPI;
import com.ose.report.domain.repository.statistics.ArchiveDataRepository;
import com.ose.report.domain.service.ReportHistoryInterface;
import com.ose.report.entity.statistics.ArchiveData;
import com.ose.report.vo.ArchiveDataType;
import com.ose.report.vo.ArchiveScheduleType;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "")
@RestController
public class WeldStatisticsController extends BaseReportController implements WeldStatisticsAPI {

    private final ArchiveDataRepository archiveDataRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public WeldStatisticsController(ArchiveDataRepository archiveDataRepository, ReportHistoryInterface reportHistoryService,
                                    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
                                             UploadFeignAPI uploadFeignAPI) {
        super(reportHistoryService, uploadFeignAPI);
        this.archiveDataRepository = archiveDataRepository;
    }

    @Override
    @Operation(description = "")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/weld-statistics",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
//    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonResponseBody postWeldStatistics(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId) {

        archiveDataRepository.archiveTagWeightDates(projectId).forEach(
            archive -> {
                ArchiveData archiveData = new ArchiveData();

                archiveData.setArchiveDay((Number)archive.get("group_day"));
                archiveData.setArchiveWeek((Number)archive.get("group_week"));
                archiveData.setArchiveMonth((Number)archive.get("group_month"));
                archiveData.setArchiveYear((Number) archive.get("group_year"));

                archiveData.setGroupDay((Number)archive.get("group_day"));
                archiveData.setGroupWeek((Number)archive.get("group_week"));
                archiveData.setGroupMonth((Number)archive.get("group_month"));
                archiveData.setGroupYear((Number)archive.get("group_year"));
                archiveData.setGroupDate((Number)archive.get("group_date"));

                archiveData.setModule(archive.get("module") == null ? null : archive.get("module").toString());
                archiveData.setDeck(archive.get("deck") == null ? null : archive.get("deck").toString());
                archiveData.setPanel(archive.get("panel") == null ? null : archive.get("panel").toString());
                archiveData.setWp04No(archive.get("wp04_no") == null ? null : archive.get("wp04_no").toString());
                archiveData.setEntitySubType(archive.get("entity_category") == null ? null : archive.get("entity_category").toString());
                archiveData.setProcess(archive.get("process") == null ? null : archive.get("process").toString());
                archiveData.setTeamName(archive.get("work_group") == null ? null : archive.get("work_group").toString());
                archiveData.setAddress(archive.get("work_group") == null ? null : archive.get("address").toString());

                archiveData.setProjectId((Number) archive.get("project_id"));
                archiveData.setValue01(archive.get("length") == null ? 0.0 : (Double)archive.get("length"));
                archiveData.setValue02(archive.get("done_length") == null ? 0.0 : (Double)archive.get("done_length"));
                archiveData.setArchiveType(ArchiveDataType.WBS_TAG_WEIGHT_RATE_PROGRESS);
                archiveData.setScheduleType(ArchiveScheduleType.DAILY);



                archiveDataRepository.save(archiveData);
            }
        );

        return new JsonResponseBody(getContext());
    }
}
