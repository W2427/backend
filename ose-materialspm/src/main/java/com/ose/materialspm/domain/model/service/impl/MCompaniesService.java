package com.ose.materialspm.domain.model.service.impl;


import com.ose.materialspm.domain.model.repository.MCompaniesRepository;
import com.ose.materialspm.domain.model.service.MCompaniesInterface;
import com.ose.materialspm.entity.MCompaniesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MCompaniesService implements MCompaniesInterface {

    private final MCompaniesRepository mCompaniesRepository;

    @Autowired
    public MCompaniesService(MCompaniesRepository mCompaniesRepository) {
        this.mCompaniesRepository = mCompaniesRepository;
    }

    @Override
    public List<MCompaniesEntity> getMCompanies(String projId) {
        return mCompaniesRepository.findByProjId(projId);
    }
}
