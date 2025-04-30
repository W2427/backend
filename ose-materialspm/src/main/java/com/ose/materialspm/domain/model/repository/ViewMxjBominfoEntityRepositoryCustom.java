package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.dto.*;
import com.ose.materialspm.entity.ViewMxjBominfoEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ViewMxjBominfoEntityRepositoryCustom {

    Page<ViewMxjBominfoEntity> search(BominfoListSimpleDTO bominfoListSimpleDTO);

    Page<FadListResultsDTO> getFadList(FadListDTO fadListDTO);

    List<FadListResultsDTO> getFadListNoPage(FadListDTO fadListDTO);

    Page<FaListResultsDTO> getFahList(FaListDTO fadListDTO);

    FaListResultsDTO getFah(FadListDTO fadListDTO);

}
