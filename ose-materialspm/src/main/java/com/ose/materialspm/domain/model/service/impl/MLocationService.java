package com.ose.materialspm.domain.model.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ose.materialspm.domain.model.repository.MLocationRepository;
import com.ose.materialspm.domain.model.service.MLocationInterface;
import com.ose.materialspm.entity.MLocationEntity;

@Component
public class MLocationService implements MLocationInterface {

    private MLocationRepository mLocationRepository;

    @Autowired
    public MLocationService(MLocationRepository mLocationRepository) {
        this.mLocationRepository = mLocationRepository;
    }

    @Override
    public List<MLocationEntity> getMLocation(String projId) {
        return mLocationRepository.findByProjId(projId);
    }


}
