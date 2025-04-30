package com.ose.materialspm.domain.model.service;

import com.ose.materialspm.dto.FMaterialReturnReceiptDTO;
import com.ose.materialspm.dto.ReceiveReceiptResultDTO;

/**
 * Demoservice接口
 */
public interface ReturnReceiptInterface {

    // Page<IssueReceiptListResultsDTO> getReturnReceipt(IssueReceiptDTO issueReceiptDTO);

    ReceiveReceiptResultDTO saveRti(String projId, FMaterialReturnReceiptDTO returnReceiptDTO);

    ReceiveReceiptResultDTO runRti(String projId, FMaterialReturnReceiptDTO returnReceiptDTO);

}
