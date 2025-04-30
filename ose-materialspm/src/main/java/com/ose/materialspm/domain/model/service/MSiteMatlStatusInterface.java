package com.ose.materialspm.domain.model.service;

import com.ose.materialspm.entity.MSiteMatlStatusEntity;

import java.util.List;

/**
 * service接口
 */
public interface MSiteMatlStatusInterface {

    List<MSiteMatlStatusEntity> getMSiteMatlStatus(String projId);

}
