package com.ose.materialspm.domain.model.service;

import org.springframework.data.domain.Page;

import com.ose.materialspm.dto.FMaterialReceiveReceiptDTO;
import com.ose.materialspm.dto.ReceiveReceiptDTO;
import com.ose.materialspm.dto.ReceiveReceiptListResultsDTO;
import com.ose.materialspm.dto.ReceiveReceiptResultDTO;

/**
 * Demoservice接口
 */
public interface ReceiveReceiptInterface {

    Page<ReceiveReceiptListResultsDTO> getReceiveReceipt(ReceiveReceiptDTO receiveReceiptDTO);

    ReceiveReceiptResultDTO saveMrr(String projId, FMaterialReceiveReceiptDTO receiveReceiptDTO);

    ReceiveReceiptResultDTO runProcedure(String projId, FMaterialReceiveReceiptDTO receiveReceiptDTO);
}
