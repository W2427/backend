package com.ose.tasks.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.*;
import com.ose.tasks.entity.Suggestion;
import org.springframework.data.domain.Page;

public interface SuggestionInterface {

    Suggestion create(OperatorDTO operatorDTO, SuggestionAddDTO dto,ContextDTO context);

    Page<Suggestion> getList(SuggestionSearchDTO dto, Long operatorId);

    Suggestion get(Long id);

    Suggestion modify(OperatorDTO operatorDTO, ContextDTO context, Long id, SuggestionEditDTO dto);

    Suggestion close(OperatorDTO operatorDTO, Long id);
}
