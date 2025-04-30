package com.ose.tasks.domain.model.service;

import com.ose.tasks.dto.PerformanceEvaluationDataDTO;
import com.ose.tasks.dto.PerformanceEvaluationSearchDTO;
import org.springframework.data.domain.Page;

import java.io.File;
import java.text.ParseException;

public interface PerformanceEvaluationInterface {

    Page<PerformanceEvaluationDataDTO> getList(PerformanceEvaluationSearchDTO dto, Long operatorId);

    File saveDownloadFile(PerformanceEvaluationSearchDTO criteriaDTO, Long operatorId) throws ParseException;

}
