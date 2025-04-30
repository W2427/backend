package com.ose.tasks.domain.model.repository.archivedata;

import java.util.List;
import java.util.Map;

import com.ose.tasks.domain.model.repository.archivedata.ArchiveDataDetailRepositoryImpl.InspectionType;
import com.ose.tasks.domain.model.repository.archivedata.ArchiveDataDetailRepositoryImpl.QualifiedType;
import com.ose.tasks.dto.archivedata.ArchiveDataCriteriaDTO;

public interface ArchiveDataDetailRepositoryCustom {

    List<Map<String, Object>> getWbsPassRateProgressFpyQualified(
        Long projectId,
        ArchiveDataCriteriaDTO criteriaDTO,
        InspectionType inspectionType,
        QualifiedType qualifiedType
    );

    List<Map<String, Object>> getWbsWeldRateProgress(
        Long projectId,
        ArchiveDataCriteriaDTO criteriaDTO,
        QualifiedType qualified
    );

    List<Map<String, Object>> getWbsNdtRateProgress(
        Long projectId,
        ArchiveDataCriteriaDTO criteriaDTO,
        QualifiedType qualified
    );

}
