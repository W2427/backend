package com.ose.materialspm.domain.model.service;

import com.ose.materialspm.dto.SpmMatchLnCriteriaDTO;
import com.ose.materialspm.dto.SpmMatchLnNodeDTO;
import com.ose.materialspm.entity.SpmListPosDTO;
import com.ose.materialspm.entity.SpmMatchLns;
import com.ose.materialspm.entity.SpmMatchLnNode;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

/**
 * SPMMatchLnNodeInterface service接口
 */
public interface SpmMatchLnNodeInterface {

    Long getMatchLnNodeCount(SpmMatchLnNodeDTO spmMatchLnNodeDTO);

    List<SpmMatchLnNode> getList(SpmMatchLnNodeDTO spmMatchLnNodeDTO);

    Page<SpmMatchLns> getMatchList(SpmMatchLnCriteriaDTO spmMatchLnCriteriaDTO);

    List<SpmListPosDTO> getListPos(String spmProjId, List<BigDecimal> lnIds);
}
