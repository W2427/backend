package com.ose.materialspm.domain.model.service.impl;

import com.ose.materialspm.domain.model.repository.SpmMatchLnNodeRepository;
import com.ose.materialspm.domain.model.service.SpmMatchLnNodeInterface;
import com.ose.materialspm.dto.SpmMatchLnCriteriaDTO;
import com.ose.materialspm.dto.SpmMatchLnNodeDTO;
import com.ose.materialspm.entity.SpmListPosDTO;
import com.ose.materialspm.entity.SpmMatchLnNode;
import com.ose.materialspm.entity.SpmMatchLns;
import com.ose.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SpmMatchLnNodeService implements SpmMatchLnNodeInterface {

    // SPM BOM NODE MATCH 信息操作仓库
    private final SpmMatchLnNodeRepository spmMatchLnNodeRepository;


    /**
     * 构造方法。
     *
     * @param spmMatchLnNodeRepository BOM NODE MATCH 操作仓库
     */
    @Autowired
    public SpmMatchLnNodeService(
        SpmMatchLnNodeRepository spmMatchLnNodeRepository
    ) {
        this.spmMatchLnNodeRepository = spmMatchLnNodeRepository;
    }

    /**
     * 查询SPM BOM NODE MATCH 总数。
     *
     * @param spmMatchLnNodeDTO
     * @return SPM BOM NODE MATCH 总数。
     */
    @Override
    public Long getMatchLnNodeCount(SpmMatchLnNodeDTO spmMatchLnNodeDTO) {
        return spmMatchLnNodeRepository.getMatchLnNodeCount(spmMatchLnNodeDTO);
    }

    /**
     * 查询SPM BOM NODE MATCH 列表。
     *
     * @param spmMatchLnNodeDTO
     * @return SPM BOM NODE MATCH 列表分页数据
     */
    @Override
    public List<SpmMatchLnNode> getList(SpmMatchLnNodeDTO spmMatchLnNodeDTO) {
        return spmMatchLnNodeRepository.getList(spmMatchLnNodeDTO);
    }

    /**
     * 查询SPM BOM NODE MATCH 汇总列表。
     *
     * @param spmMatchLnCriteriaDTO
     * @return SPM BOM 匹配 汇总 列表分页数据
     */
    @Override
    public Page<SpmMatchLns> getMatchList(SpmMatchLnCriteriaDTO spmMatchLnCriteriaDTO) {
        if (!StringUtils.isEmpty(spmMatchLnCriteriaDTO.getSpmProjId())) {
            if (!StringUtils.isEmpty(spmMatchLnCriteriaDTO.getLnIdsString())) {
                return spmMatchLnNodeRepository.getMatchList(spmMatchLnCriteriaDTO);

            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public List<SpmListPosDTO> getListPos(String spmProjId, List<BigDecimal> lnIds) {
        return spmMatchLnNodeRepository.getListPos(spmProjId, lnIds);
    }
}
