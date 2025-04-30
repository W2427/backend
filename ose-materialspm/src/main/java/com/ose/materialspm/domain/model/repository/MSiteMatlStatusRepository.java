package com.ose.materialspm.domain.model.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ose.materialspm.entity.MSiteMatlStatusEntity;


/**
 * 查询接口。
 */
@Transactional
public interface MSiteMatlStatusRepository extends PagingAndSortingRepository<MSiteMatlStatusEntity, String> {

    List<MSiteMatlStatusEntity> findByProjId(String projId);

}
