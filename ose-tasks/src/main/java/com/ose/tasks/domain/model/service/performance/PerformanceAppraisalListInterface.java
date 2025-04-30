package com.ose.tasks.domain.model.service.performance;

import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.PerformanceBatchResultDTO;
import com.ose.tasks.dto.performance.PerformanceAppraisalListImportDTO;
import com.ose.tasks.entity.BatchTask;

public interface PerformanceAppraisalListInterface {

    BatchResultDTO importDetailList(OperatorDTO operator,
                                BatchTask batchTask,
                                PerformanceAppraisalListImportDTO importDTO);
}
