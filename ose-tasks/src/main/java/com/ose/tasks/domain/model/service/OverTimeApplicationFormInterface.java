package com.ose.tasks.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.OverTimeApplicationFormSearchDTO;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.OverTimeApplicationForm;
import org.springframework.data.domain.Page;

import java.io.File;
import java.text.ParseException;

public interface OverTimeApplicationFormInterface {

    Page<OverTimeApplicationForm> getList(OverTimeApplicationFormSearchDTO dto, Long operatorId);

    Page<OverTimeApplicationForm> getAllList(OverTimeApplicationFormSearchDTO dto, Long operatorId);

    OverTimeApplicationForm create(OperatorDTO operatorDTO, OverTimeApplicationFormCreateDTO dto) throws ParseException;

    void delete(OperatorDTO operatorDTO, Long id);

    OverTimeApplicationForm modify(OperatorDTO operatorDTO, ContextDTO context, Long id, OverTimeApplicationFormCreateDTO dto) throws ParseException;

    OverTimeApplicationForm get(Long id);

    void handle(Long id, OverTimeApplicationFormHandleDTO dto, OperatorDTO operatorDTO);

    void reviewHandle(Long id, OverTimeApplicationFormHandleDTO dto, OperatorDTO operatorDTO);

    OverTimeApplicationFormFilterDTO filter();

    void transfer(Long id, OverTimeApplicationFormTransferDTO dto, OperatorDTO operatorDTO);

    File saveDownloadFile(OverTimeApplicationFormSearchDTO criteriaDTO, Long operatorId) throws ParseException;
}
