package com.ose.materialspm.domain.model.service;

import com.ose.materialspm.dto.FMaterialIssueReceiptDTO;
import com.ose.materialspm.dto.ReceiveReceiptResultDTO;
import org.springframework.data.domain.Page;

import com.ose.materialspm.dto.IssueReceiptDTO;
import com.ose.materialspm.dto.IssueReceiptListResultsDTO;

/**
 * Demoservice接口
 */
public interface IssueReceiptInterface {

    Page<IssueReceiptListResultsDTO> getIssueReceipt(IssueReceiptDTO issueReceiptDTO);

    ReceiveReceiptResultDTO saveMir(String projId, FMaterialIssueReceiptDTO issueReceiptDTO);

    ReceiveReceiptResultDTO runMir(String projId, FMaterialIssueReceiptDTO issueReceiptDTO);

}
