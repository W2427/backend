package com.ose.materialspm.domain.model.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import com.ose.materialspm.entity.ReqDetail;

/**
 * 请购单详情查询接口。
 */
@Transactional
public interface ReqDetailRepository extends PagingAndSortingRepository<ReqDetail, String>, ReqDetailRepositoryCustom {
}
