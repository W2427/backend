package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.entity.MLocationEntity;

import java.util.List;


/**
 * 查询接口。
 */
public interface MLocationRepositoryCustom {

    List<MLocationEntity> findByProjId(String projId);

}
