package com.ose.material.domain.model.repository;

import com.ose.material.entity.MmMaterialCodeEntity;
import com.ose.repository.BaseRepository;
import org.springframework.data.domain.Page;


public class MmMaterialCodeRepositoryImpl extends BaseRepository implements MmMaterialCodeRepositoryCustom {

    /**
     * 查询请购单详情。
     *
     * @param id 请购单id
     * @return 请购单详情分页数据
     */
    @SuppressWarnings("unchecked")
    @Override
    public Page<MmMaterialCodeEntity> search(
        String id
    ) {

        return null;
    }

}
