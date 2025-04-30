package com.ose.docs.domain.model.repository;

import com.ose.docs.entity.MaterialStructureES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface MaterialStructureESRepository extends ElasticsearchRepository<MaterialStructureES, String> {
}
