package com.ose.materialspm.domain.model.repository;

import org.springframework.data.domain.Page;

import com.ose.materialspm.dto.ReceiveReceiptDTO;
import com.ose.materialspm.dto.ReceiveReceiptListResultsDTO;

public interface VMxjMatlRecvRptsEntityRepositoryCustom {

    Page<ReceiveReceiptListResultsDTO> getReceiveReceiptList(ReceiveReceiptDTO receiveReceiptDTO);

}
