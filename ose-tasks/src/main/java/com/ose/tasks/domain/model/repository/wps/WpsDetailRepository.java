package com.ose.tasks.domain.model.repository.wps;

import com.ose.tasks.entity.wps.WpsDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Set;

public interface WpsDetailRepository extends PagingAndSortingRepository<WpsDetail, Long> {

    /**
     * 获取wps详情列表。
     *
     * @param wpsId
     * @return wps详情列表
     */
    Page<WpsDetail> findByWpsIdAndDeletedIsFalse(Long wpsId, Pageable pageable);


    /**
     * 获取wps所有详情列表。
     *
     * @param wpsId
     * @return wps详情列表
     */
    List<WpsDetail> findByWpsIdAndDeletedIsFalse(Long wpsId);

    /**
     * 获取wps详情。
     */
    WpsDetail findByIdAndDeletedIsFalse(Long detailId);

    List<WpsDetail> findByIdInAndDeletedIsFalse(List<Long> wpsDetails);

    /**
     * 根据wpsID列表获取wps详情信息。
     *
     * @param wpsIds
     * @return wps详情列表。
     */
    List<WpsDetail> findByWpsIdInAndDeletedIsFalse(Set<Long> wpsIds);
}
