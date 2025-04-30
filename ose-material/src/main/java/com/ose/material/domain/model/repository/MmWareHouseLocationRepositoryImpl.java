package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmWareHouseLocationSearchDTO;
import com.ose.material.entity.MmWareHouseLocationEntity;
import com.ose.material.vo.WareHouseLocationType;
import com.ose.repository.BaseRepository;
import org.springframework.data.domain.Page;


public class MmWareHouseLocationRepositoryImpl extends BaseRepository implements MmWareHouseLocationRepositoryCustom {

    @Override
    public Page<MmWareHouseLocationEntity> search(
        Long orgId,
        Long projectId,
        MmWareHouseLocationSearchDTO mmWareHouseLocationSearchDTO
    ) {
        SQLQueryBuilder queryBuilder = getSQLQueryBuilder(MmWareHouseLocationEntity.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .like("name", mmWareHouseLocationSearchDTO.getKeyword())
            .is("deleted", false);

        if (mmWareHouseLocationSearchDTO.getType() != null) {
            queryBuilder.is("type", WareHouseLocationType.valueOf(mmWareHouseLocationSearchDTO.getType()));
        }

        if (mmWareHouseLocationSearchDTO.getParentWareHouseId() != null) {
            queryBuilder.is("parentWareHouseId", mmWareHouseLocationSearchDTO.getParentWareHouseId());
        }

//        if (mmWareHouseLocationSearchDTO.getFetchAll() == null ||
//            (mmWareHouseLocationSearchDTO.getFetchAll() != null && !mmWareHouseLocationSearchDTO.getFetchAll())) {
//            queryBuilder.paginate(mmWareHouseLocationSearchDTO.toPageable());
//        }

        if (mmWareHouseLocationSearchDTO.getFetchAll() != null && mmWareHouseLocationSearchDTO.getFetchAll()) {
            int pageSize = Integer.MAX_VALUE;
            mmWareHouseLocationSearchDTO.getPage().setSize(pageSize);
        }

        return queryBuilder
            .paginate(mmWareHouseLocationSearchDTO.toPageable())
            .exec()
            .page();
    }

}
