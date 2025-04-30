package com.ose.materialspm.domain.model.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ose.materialspm.domain.model.repository.MSiteMatlStatusRepository;
import com.ose.materialspm.domain.model.service.MSiteMatlStatusInterface;
import com.ose.materialspm.entity.MSiteMatlStatusEntity;

@Component
public class MSiteMatlStatusService implements MSiteMatlStatusInterface {

    private MSiteMatlStatusRepository mSiteMatlStatusRepository;

    @Autowired
    public MSiteMatlStatusService(MSiteMatlStatusRepository mSiteMatlStatusRepository) {
        this.mSiteMatlStatusRepository = mSiteMatlStatusRepository;
    }

    @Override
    public List<MSiteMatlStatusEntity> getMSiteMatlStatus(String projId) {
        return mSiteMatlStatusRepository.findByProjId(projId);
    }


}
