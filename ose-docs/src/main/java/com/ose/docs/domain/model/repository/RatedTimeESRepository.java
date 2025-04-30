package com.ose.docs.domain.model.repository;

import com.ose.docs.entity.RatedTimeES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface RatedTimeESRepository extends ElasticsearchRepository<RatedTimeES, String> {
}
