package com.ose.material.domain.model.repository;

import com.ose.material.dto.HeatBatchNoSearchDTO;
import com.ose.material.entity.MmHeatBatchNoEntity;
import com.ose.repository.BaseRepository;
import org.springframework.data.domain.Page;

public class HeatBatchNoRepositoryImpl extends BaseRepository implements HeatBatchNoRepositoryCustom {

    @Override
    public Page<MmHeatBatchNoEntity> search(Long orgId,
                                            Long projectId,
                                            HeatBatchNoSearchDTO heatBatchNoSearchDTO) {

        SQLQueryBuilder builder = getSQLQueryBuilder(MmHeatBatchNoEntity.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("deleted", false);

        if (heatBatchNoSearchDTO.getHeatNo() != null) {
            builder.is("heatNoCode", heatBatchNoSearchDTO.getHeatNo());
        }

        if (heatBatchNoSearchDTO.getBatchNo() != null) {
            builder.is("batchNoCode", heatBatchNoSearchDTO.getBatchNo());
        }

        return builder.paginate(heatBatchNoSearchDTO.toPageable())
            .exec()
            .page();
    }

}
