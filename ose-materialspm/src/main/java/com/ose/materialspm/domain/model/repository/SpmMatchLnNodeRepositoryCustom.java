package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.dto.SpmMatchLnCriteriaDTO;
import com.ose.materialspm.dto.SpmMatchLnNodeDTO;
import com.ose.materialspm.entity.SpmListPosDTO;
import com.ose.materialspm.entity.SpmMatchLns;
import com.ose.materialspm.entity.SpmMatchLnNode;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;


/**
 * MATCH BOM NODE 查询接口。
 */
//@Transactional
public interface SpmMatchLnNodeRepositoryCustom {


    Long getMatchLnNodeCount(SpmMatchLnNodeDTO spmMatchLnNodeDTO);

    List<SpmMatchLnNode> getList(SpmMatchLnNodeDTO spmMatchLnNodeDTO);

    Page<SpmMatchLns> getMatchList(SpmMatchLnCriteriaDTO spmMatchLnCriteriaDTO);

    List<SpmListPosDTO> getListPos(String spmProjId, List<BigDecimal> lnIds);
}
