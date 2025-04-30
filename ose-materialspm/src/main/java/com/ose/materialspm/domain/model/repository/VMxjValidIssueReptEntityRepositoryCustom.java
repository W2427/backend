package com.ose.materialspm.domain.model.repository;

import org.springframework.data.domain.Page;

import com.ose.materialspm.dto.IssueReceiptDTO;
import com.ose.materialspm.dto.IssueReceiptListResultsDTO;

public interface VMxjValidIssueReptEntityRepositoryCustom {

    Page<IssueReceiptListResultsDTO> getIssueReceiptList(IssueReceiptDTO issueReceiptDTO);

}
