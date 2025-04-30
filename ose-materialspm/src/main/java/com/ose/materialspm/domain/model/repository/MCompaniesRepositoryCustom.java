package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.entity.MCompaniesEntity;

import java.util.List;


/**
 * 查询接口。
 */
public interface MCompaniesRepositoryCustom {

    List<MCompaniesEntity> findByProjId(String projId);

}
