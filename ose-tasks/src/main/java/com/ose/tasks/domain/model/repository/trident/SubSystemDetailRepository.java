package com.ose.tasks.domain.model.repository.trident;

import com.ose.tasks.entity.trident.SubSystemDetail;
import com.ose.vo.EntityStatus;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SubSystemDetailRepository extends SubSystemDetailRepositoryCustom, PagingAndSortingRepository<SubSystemDetail, Long> {

    List<SubSystemDetail> findBySubSystemIdAndDisciplineCodeAndStatus(Long subSystemId, String disciplineCode, EntityStatus status);

    List<SubSystemDetail> findBySubSystemIdAndDisciplineCodeAndDetailType(Long subSystemId, String disciplineCode, String remarks);

    List<SubSystemDetail> findBySubSystemIdAndDisciplineCode(Long subSystemId, String disciplineCode);

    List<SubSystemDetail> findByProjectIdAndSubSystemIdAndDisciplineCodeAndStatus(Long projectId, Long subSystemId, String disc, EntityStatus status);
}
