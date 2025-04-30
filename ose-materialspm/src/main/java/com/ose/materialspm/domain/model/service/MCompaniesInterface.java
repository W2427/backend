package com.ose.materialspm.domain.model.service;

import com.ose.materialspm.entity.MCompaniesEntity;

import java.util.List;

/**
 * service接口
 */
public interface MCompaniesInterface {

    List<MCompaniesEntity> getMCompanies(String projId);
}
