package com.ose.material.domain.model.repository;

import com.ose.material.entity.MmMaterialCodeTypeEntity;
import com.ose.repository.BaseRepository;
import org.springframework.data.domain.Page;


public class MmMaterialCodeTypeProjectRepositoryImpl extends BaseRepository implements MmMaterialCodeTypeProjectRepositoryCustom {

    /**
     * 查询请购单详情。
     *
     * @param id 请购单id
     * @return 请购单详情分页数据
     */
    @SuppressWarnings("unchecked")
    @Override
    public Page<MmMaterialCodeTypeEntity> search(
        String id
    ) {

        return null;
    }

}
