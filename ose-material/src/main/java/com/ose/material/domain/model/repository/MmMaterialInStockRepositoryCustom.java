package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmMaterialInStockSearchDTO;
import com.ose.material.entity.MmMaterialInStockEntity;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 * 入库材料二维码库。
 */
@Transactional
public interface MmMaterialInStockRepositoryCustom {

    Page<MmMaterialInStockEntity> search(Long orgId,
                                         Long projectId,
                                         MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO);

}
