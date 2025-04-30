package com.ose.docs.domain.model.repository;

import com.ose.docs.entity.WelderES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface WelderESRepository extends ElasticsearchRepository<WelderES, String> {
}
