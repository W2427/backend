package com.ose.materialspm.domain.model.service;

import com.ose.materialspm.dto.*;
import com.ose.materialspm.entity.ViewMxjBominfoEntity;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Demoservice接口
 */
public interface SpmMaterialRequisitionInterface {

    /**
     * 查询bom信息
     *
     * @param bominfoListSimpleDTO
     * @return
     */
    Page<ViewMxjBominfoEntity> getBominfoListSimple(BominfoListSimpleDTO bominfoListSimpleDTO);

    /**
     * 查询FaList
     *
     * @param faListDTO
     * @return
     */
    Page<FaListResultsDTO> getFahList(FaListDTO faListDTO);

    /**
     * 查询FadList
     *
     * @param fadListDTO
     * @return
     */
    Page<FadListResultsDTO> getFadList(FadListDTO fadListDTO);

    List<FadListResultsDTO> getFadListNoPage(FadListDTO fadListDTO);

    FaListResultsDTO getFah(FadListDTO fadListDTO);

}
