package com.ose.tasks.domain.model.repository.wps;


import com.ose.tasks.dto.wps.WpsCriteriaDTO;
import com.ose.tasks.entity.wps.WelderCertificate;

import java.util.List;

public interface WelderCertificateRepositoryCustom {

    /**
     * 获取wps列表。
     *
     * @param welderId       焊工ID
     * @param wpsCriteriaDTO 查询条件
     * @return wps列表
     */
    List<WelderCertificate> search(Long welderId, WpsCriteriaDTO wpsCriteriaDTO);

}
