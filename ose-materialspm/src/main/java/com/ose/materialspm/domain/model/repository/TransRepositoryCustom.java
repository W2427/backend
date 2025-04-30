package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.dto.ReceiveReceiptResultDTO;

public interface TransRepositoryCustom {

    ReceiveReceiptResultDTO callProcedure(String mrrNumber, String projId, String transType);
}
