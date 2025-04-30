package com.ose.docs.domain.model.repository;

import com.ose.docs.entity.ExperienceAttachmentES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface ExperienceAttachmentRepository extends ElasticsearchRepository<ExperienceAttachmentES, String> {
}
