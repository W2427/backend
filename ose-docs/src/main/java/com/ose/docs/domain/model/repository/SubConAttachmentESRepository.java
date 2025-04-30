package com.ose.docs.domain.model.repository;

import com.ose.docs.entity.SubConAttachmentES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface SubConAttachmentESRepository extends ElasticsearchRepository<SubConAttachmentES, String> {
}
