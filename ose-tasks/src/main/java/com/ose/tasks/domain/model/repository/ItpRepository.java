package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.Itp;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ItpRepository extends PagingAndSortingWithCrudRepository<Itp, Long>, ItpRepositoryCustom {
    /**
     * 获取ITP详情。
     *
     * @param itpId ITPID
     * @return ITP详情
     */
    Itp findByIdAndDeletedIsFalse(Long itpId);
}
