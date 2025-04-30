package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.dto.ReqListCriteriaDTO;
import com.ose.materialspm.entity.ViewMxjReqs;
import org.springframework.data.domain.Page;

public interface ReqListRepositoryCustom {

    /**
     * 查询用户。
     *
     * @param criteriaDTO 查询条件
     * @param pageable    分页参数
     * @return 用户查询结果分页数据
     */
    Page<ViewMxjReqs> getList(ReqListCriteriaDTO reqListDTO);
}
