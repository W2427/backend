package com.ose.materialspm.domain.model.service;

import com.ose.materialspm.entity.MLocationEntity;

import java.util.List;

/**
 * service接口
 */
public interface MLocationInterface {

    List<MLocationEntity> getMLocation(String projId);

}
