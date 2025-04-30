package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.dto.ReqDetailDTO;
import com.ose.materialspm.entity.ReqDetail;

import org.springframework.data.domain.Page;

import java.util.List;

public interface ReqDetailRepositoryCustom {

    /**
     * 查询请购单详情。
     *
     * @param reqId 请购单id
     * @return 请购单详情分页数据
     */
    Page<ReqDetail> getDetail(ReqDetailDTO reqDetailDTO);

    List<ReqDetail> getDetailNoPage(ReqDetailDTO reqDetailDTO);
}
