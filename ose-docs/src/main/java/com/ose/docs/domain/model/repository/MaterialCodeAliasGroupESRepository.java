package com.ose.docs.domain.model.repository;

import com.ose.docs.entity.MaterialCodeAliasGroupES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface MaterialCodeAliasGroupESRepository extends ElasticsearchRepository<MaterialCodeAliasGroupES, String> {
}
