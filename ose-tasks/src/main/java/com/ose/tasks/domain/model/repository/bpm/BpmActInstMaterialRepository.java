package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.BpmActInstMaterial;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.annotation.Nullable;
import java.util.List;

public interface BpmActInstMaterialRepository extends PagingAndSortingWithCrudRepository<BpmActInstMaterial, Long>, BpmActInstMaterialRepositoryCustom {

    List<BpmActInstMaterial> findByActInstIdOrderByCreatedAtDesc(Long actInstId);

    @Nullable
    List<BpmActInstMaterial> findByEntityIdOrderByCreatedAtDesc(Long entityId);

    BpmActInstMaterial findFirstByEntityIdAndActInstIdOrderByCreatedAtDesc(Long entityId, Long actInstId);

    BpmActInstMaterial findFirstByEntityIdOrderByCreatedAtDesc(Long entityId);
}
