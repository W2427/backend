package com.ose.materialspm.domain.model.service;

import com.ose.materialspm.entity.MWareHouseEntity;

import java.util.List;

/**
 * service接口
 */
public interface MWareHouseInterface {

    List<MWareHouseEntity> getMWareHouse(String projId);

    /**
     * 查找SPM过量库
     *
     * @param companyId
     * @return
     */
    List<MWareHouseEntity> getOverdoseWareHouses(Integer companyId);

}
