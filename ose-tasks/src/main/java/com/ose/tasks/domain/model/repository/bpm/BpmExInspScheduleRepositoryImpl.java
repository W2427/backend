package com.ose.tasks.domain.model.repository.bpm;

import com.ose.dto.PageDTO;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.bpm.ExInspScheduleCriteriaDTO;
import com.ose.tasks.entity.bpm.BpmExInspSchedule;
import com.ose.tasks.vo.bpm.ExInspScheduleCoordinateCategory;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;

/**
 * 根据二维码查询得到外检申请的聚合数据。
 */
public class BpmExInspScheduleRepositoryImpl extends BaseRepository implements BpmExInspScheduleRepositoryCustom {

    @Override
    public Page<BpmExInspSchedule> findByOrgIdAndProjectIdOrderByCreatedAtDesc(Long orgId,
                                                                               Long projectId, PageDTO page, ExInspScheduleCriteriaDTO criteriaDTO) {
        SQLQueryBuilder<BpmExInspSchedule> builder = getSQLQueryBuilder(BpmExInspSchedule.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("status", EntityStatus.ACTIVE)
            .is("isSendEmail", true);

        if (criteriaDTO.getKeyword() != null) {
            builder.is("seriesNos", criteriaDTO.getKeyword());
        }

        if (criteriaDTO.getCoordinateCategory() != null) {
            ExInspScheduleCoordinateCategory category = ExInspScheduleCoordinateCategory.valueOf(criteriaDTO.getCoordinateCategory());
            builder.is("coordinateCategory", category);
            if (category == ExInspScheduleCoordinateCategory.NO_COORDINATE) {
                builder.is("operator", criteriaDTO.getOperator());
            }
        }

        if (criteriaDTO.getState() != null) {
            builder.is("state", ReportStatus.valueOf(criteriaDTO.getState()));
        }

        builder.sort(page.toPageable().getSort());

        return builder.paginate(page.toPageable())
            .exec()
            .page();
    }

}
