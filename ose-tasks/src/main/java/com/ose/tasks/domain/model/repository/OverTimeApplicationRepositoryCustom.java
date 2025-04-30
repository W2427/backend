package com.ose.tasks.domain.model.repository;

import com.ose.tasks.dto.OverTimeApplicationFormSearchDTO;
import com.ose.tasks.dto.PerformanceEvaluationDataDTO;
import com.ose.tasks.dto.PerformanceEvaluationSearchDTO;
import com.ose.tasks.entity.OverTimeApplicationForm;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface OverTimeApplicationRepositoryCustom {

    Page<OverTimeApplicationForm> search(
        Long operatorId,
        OverTimeApplicationFormSearchDTO dto
    );

    Page<OverTimeApplicationForm> searchAll(
        Long operatorId,
        OverTimeApplicationFormSearchDTO dto
    );

    Page<PerformanceEvaluationDataDTO> searchEvaluation(
        Long operatorId,
        PerformanceEvaluationSearchDTO dto,
        Map<String,Integer> companyToNormalDays,
        Map<String,List<String>> companyToHolidates
    );

}
