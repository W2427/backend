package com.ose.tasks.domain.model.repository.wps;

import com.ose.tasks.entity.wps.WelderCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface WelderCertificateRepository extends WelderCertificateRepositoryCustom, PagingAndSortingRepository<WelderCertificate, Long> {

    /**
     * 获取焊工证书列表。
     *
     * @param welderId 焊工ID
     * @param pageable 分页参数
     * @return 焊工列表
     */
    Page<WelderCertificate> findByWelderIdAndDeletedIsFalse(Long welderId, Pageable pageable);

    /**
     * 获取焊工证信息。
     *
     * @param certificateId 焊工证ID
     * @return 焊工证信息
     */
    WelderCertificate findByIdAndDeletedIsFalse(Long certificateId);

    /**
     * 获取焊工证书列表。
     *
     * @param welderIds 焊工ID列表
     * @return 焊工列表
     */
    List<WelderCertificate> findByWelderIdInAndDeletedIsFalseOrderByWelderIdDesc(List<Long> welderIds);

    /**
     * 获取单个焊工证书列表。
     *
     * @param welderId 焊工ID
     * @return 焊工列表
     */
    List<WelderCertificate> findByWelderIdIsAndDeletedIsFalse(Long welderId);

    /**
     * 查找焊工是否含有当前WPS的信息。
     *
     * @param welderId 焊工ID
     * @return 焊工列表
     */
    List<WelderCertificate> findByWelderIdIsAndWpsNoAndDeletedIsFalse(Long welderId, String wpsNo);
}
