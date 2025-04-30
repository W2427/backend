package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.entity.MWareHouseEntity;

import java.util.List;


/**
 * 查询接口。
 */
public interface MWareHouseRepositoryCustom {

    List<MWareHouseEntity> findByProjId(String projId);

    /**
     * 查找过量库。
     *
     * @param companyId
     * @return
     */
    List<MWareHouseEntity> findByCompanyId(Integer companyId);
}
