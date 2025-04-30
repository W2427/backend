package com.ose.report.controller;

import com.ose.docs.api.UploadFeignAPI;
import com.ose.report.api.TagWeightStatisticsAPI;
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
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "")
@RestController
public class TagWeightStatisticsController extends BaseReportController implements TagWeightStatisticsAPI {

    private final ArchiveDataRepository archiveDataRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public TagWeightStatisticsController(ArchiveDataRepository archiveDataRepository,ReportHistoryInterface reportHistoryService,
                                         @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
                                             UploadFeignAPI uploadFeignAPI) {
        super(reportHistoryService, uploadFeignAPI);
        this.archiveDataRepository = archiveDataRepository;
    }

    @Override
    @Operation(description = "")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/tag-weight-statistics",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
//    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonResponseBody postTagWeightStatistics(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId) {

        archiveDataRepository.archiveTagWeightDates(projectId).forEach(
            archive -> {
                ArchiveData archiveData = new ArchiveData();

                archiveData.setArchiveDay(24);
                archiveData.setArchiveWeek(202152);
                archiveData.setArchiveMonth(12);
                archiveData.setArchiveYear(2021);

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
                archiveData.setAddress(archive.get("address") == null ? null : archive.get("address").toString());

                archiveData.setProjectId((Number) archive.get("project_id"));
                archiveData.setValue01(archive.get("weight_total") == null ? 0.0 : (Double)archive.get("weight_total"));
                archiveData.setValue02(archive.get("weight_done") == null ? 0.0 : (Double)archive.get("weight_done"));
                archiveData.setValue03(archive.get("wbs_entry_total") == null ? 0.0 : Double.parseDouble(archive.get("wbs_entry_total").toString()));
                archiveData.setValue04(archive.get("wbs_entry_done") == null ? 0.0 : Double.parseDouble(archive.get("wbs_entry_done").toString()));
                archiveData.setValue05(archive.get("length_total") == null ? 0.0 : Double.parseDouble(archive.get("length_total").toString()));
                archiveData.setValue06(archive.get("length_done") == null ? 0.0 : Double.parseDouble(archive.get("length_done").toString()));
                archiveData.setArchiveType(ArchiveDataType.WBS_TAG_WEIGHT_RATE_PROGRESS);
                archiveData.setScheduleType(ArchiveScheduleType.DAILY);



                archiveDataRepository.save(archiveData);
            }
        );

        return new JsonResponseBody(getContext());
    }
}
