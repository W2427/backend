package com.ose.tasks.domain.model.repository;

import com.ose.tasks.entity.QRCode;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * 二维码代码数据仓库。
 */
public interface QRCodeRepository extends PagingAndSortingRepository<QRCode, Long> {

    /**
     * 取得二维码代码信息。
     *
     * @param code 二维码代码
     * @return 二维码代码信息
     */
    Optional<QRCode> findByCodeAndDeletedIsFalse(String code);

    /**
     * 取得二维码信息。
     *
     * @param projectId 项目 ID
     * @param targetId  目标 ID
     * @return 二维码代码信息
     */
    Optional<QRCode> findByProjectIdAndTargetIdAndDeletedIsFalse(Long projectId, Long targetId);

}
