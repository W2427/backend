package com.ose.materialspm.domain.model.service.impl;

import com.ose.materialspm.domain.model.repository.ViewMxjBominfoEntityRepository;
import com.ose.materialspm.domain.model.service.SpmMaterialRequisitionInterface;
import com.ose.materialspm.dto.*;
import com.ose.materialspm.entity.ViewMxjBominfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpmMaterialRequisitionService implements SpmMaterialRequisitionInterface {

    private final ViewMxjBominfoEntityRepository viewMxjBominfoEntityRepository;

    /**
     * 构造方法
     */
    @Autowired
    public SpmMaterialRequisitionService(ViewMxjBominfoEntityRepository viewMxjBominfoEntityRepository) {
        this.viewMxjBominfoEntityRepository = viewMxjBominfoEntityRepository;
    }

    /**
     * 查询bom信息
     */
    @Override
    public Page<ViewMxjBominfoEntity> getBominfoListSimple(BominfoListSimpleDTO bominfoListSimpleDTO) {
        return viewMxjBominfoEntityRepository.search(bominfoListSimpleDTO);
    }

    /**
     * 查询FaList
     */
    @Override
    public Page<FaListResultsDTO> getFahList(FaListDTO faListDTO) {
        return viewMxjBominfoEntityRepository.getFahList(faListDTO);
    }

    /**
     * 查询FadList
     */
    @Override
    public Page<FadListResultsDTO> getFadList(FadListDTO fadListDTO) {
        return viewMxjBominfoEntityRepository.getFadList(fadListDTO);
    }

    @Override
    public FaListResultsDTO getFah(FadListDTO fadListDTO) {

        return viewMxjBominfoEntityRepository.getFah(fadListDTO);
    }

    @Override
    public List<FadListResultsDTO> getFadListNoPage(FadListDTO fadListDTO) {

        return viewMxjBominfoEntityRepository.getFadListNoPage(fadListDTO);
    }

}
