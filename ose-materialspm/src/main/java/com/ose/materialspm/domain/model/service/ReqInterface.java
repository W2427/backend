package com.ose.materialspm.domain.model.service;

import com.ose.docs.api.UploadFeignAPI;
import com.ose.materialspm.dto.ExportFileDTO;
import org.springframework.data.domain.Page;

import com.ose.materialspm.dto.ReqDetailDTO;
import com.ose.materialspm.dto.ReqListCriteriaDTO;
import com.ose.materialspm.entity.ViewMxjReqs;
import com.ose.materialspm.entity.ReqDetail;

/**
 * ReqService接口
 */
public interface ReqInterface {

    /**
     * 查询请购单列表。
     *
     * @param reqListDTO 查询条件
     * @return 请购单列表分页数据
     */
    Page<ViewMxjReqs> getList(ReqListCriteriaDTO reqListDTO);

    /**
     * 查询请购单详情信息。
     *
     * @param reqDetailDTO 请购单
     * @return 请购单详情分页数据
     */
    Page<ReqDetail> getDetail(ReqDetailDTO reqDetailDTO);

    ViewMxjReqs getReq(ReqDetailDTO reqDetailDTO);

    ExportFileDTO exportReq(Long orgId, Long projectId, ReqDetailDTO reqDetailDTO, UploadFeignAPI uploadFeignAPI);

}
