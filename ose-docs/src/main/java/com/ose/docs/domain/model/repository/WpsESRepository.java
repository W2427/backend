package com.ose.docs.domain.model.repository;

import com.ose.docs.entity.WpsES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface WpsESRepository extends ElasticsearchRepository<WpsES, String> {
}
