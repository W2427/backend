package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.entity.SpmMatchLnNode;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;


/**
 * 请购单查询接口。
 */
@Transactional
public interface SpmMatchLnNodeRepository extends PagingAndSortingRepository<SpmMatchLnNode, String>, SpmMatchLnNodeRepositoryCustom {

}
