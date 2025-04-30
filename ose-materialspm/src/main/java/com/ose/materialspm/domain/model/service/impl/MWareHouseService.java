package com.ose.materialspm.domain.model.service.impl;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ose.materialspm.domain.model.repository.MWareHouseRepository;
import com.ose.materialspm.domain.model.service.MWareHouseInterface;
import com.ose.materialspm.entity.MWareHouseEntity;

@Component
public class MWareHouseService implements MWareHouseInterface {

    private MWareHouseRepository mWareHouseRepository;

    @Autowired
    public MWareHouseService(MWareHouseRepository mWareHouseRepository) {
        this.mWareHouseRepository = mWareHouseRepository;
    }

    @Override
    public List<MWareHouseEntity> getMWareHouse(String projId) {
        System.out.println("进入service时间："+ new Date());
        return mWareHouseRepository.findByProjId(projId);
    }

    @Override
    public List<MWareHouseEntity> getOverdoseWareHouses(Integer companyId) {
        return mWareHouseRepository.findByCompanyId(companyId);
    }

}
