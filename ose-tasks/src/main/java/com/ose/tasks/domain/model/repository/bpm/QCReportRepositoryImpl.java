package com.ose.tasks.domain.model.repository.bpm;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.bpm.QCReportCriteriaDTO;
import com.ose.tasks.entity.report.QCReport;
import com.ose.vo.EntityStatus;


/**
 * QC REPORT 查询。
 */
public class QCReportRepositoryImpl extends BaseRepository implements QCReportRepositoryCustom {

    @Override
    public Page<QCReport> getList(Long orgId, Long projectId, Pageable pageable, QCReportCriteriaDTO criteriaDTO) {
        SQLQueryBuilder<QCReport> builder = getSQLQueryBuilder(QCReport.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("status", EntityStatus.ACTIVE);

        if (criteriaDTO.getProcess() != null
            && !"".equals(criteriaDTO.getProcess())) {
            builder.like("process", criteriaDTO.getProcess());
        }

        if (criteriaDTO.getReportNo() != null
            && !"".equals(criteriaDTO.getReportNo())) {
            builder.like("reportNo", criteriaDTO.getReportNo());
        }

        if (criteriaDTO.getEntityNo() != null
            && !"".equals(criteriaDTO.getEntityNo())) {
            builder.like("entityNos", criteriaDTO.getEntityNo().replaceAll("\"", "\\\\\\\\\""));
        }


        if (criteriaDTO.getReportStatusItemList() != null) {
            builder.in("reportStatus", criteriaDTO.getReportStatusItemList());
        }

        if (criteriaDTO.getSeriesNo() != null
            && !"".equals(criteriaDTO.getSeriesNo())) {
            try {
                int seriesNo = Integer.parseInt(criteriaDTO.getSeriesNo());
                builder.is("seriesNo", seriesNo);
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }

        }

        if (criteriaDTO.getOperatorName() != null
            && !"".equals(criteriaDTO.getOperatorName())) {
            builder.like("operatorName", criteriaDTO.getOperatorName());
        }

        if (criteriaDTO.getNdeType() != null
            && !"".equals(criteriaDTO.getNdeType())) {
            if ("--".equals(criteriaDTO.getNdeType())) {
                builder.isNull("ndeType");
            } else {
                builder.is("ndeType", criteriaDTO.getNdeType());
            }
        }

        return builder.paginate(pageable)
            .exec()
            .page();
    }

}
