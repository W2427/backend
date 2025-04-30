package com.ose.materialspm.domain.model.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ose.materialspm.entity.ViewMxjValidPohEntity;


/**
 * 请购单查询接口。
 */
@Transactional
public interface ReleaseNoteRepository extends PagingAndSortingRepository<ViewMxjValidPohEntity, String>, ReleaseNoteRepositoryCustom {

}
