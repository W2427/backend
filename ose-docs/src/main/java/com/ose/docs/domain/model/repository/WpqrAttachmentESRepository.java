package com.ose.docs.domain.model.repository;

import com.ose.docs.entity.WpqrAttachmentES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface WpqrAttachmentESRepository extends ElasticsearchRepository<WpqrAttachmentES, String> {
}
