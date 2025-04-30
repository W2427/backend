package com.ose.tasks.domain.model.repository.trident;

import com.ose.tasks.entity.trident.PreSubSystemTcpRelation;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface PreSubSystemTcpRelationRepository extends PagingAndSortingRepository<PreSubSystemTcpRelation, Long> {

    PreSubSystemTcpRelation findByProjectIdAndSubSystemId(Long projectId, Long id);
}
