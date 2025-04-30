package com.ose.docs.domain.model.repository;

import com.ose.docs.entity.WpsAttachmentES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface WpsAttachmentESRepository extends ElasticsearchRepository<WpsAttachmentES, String> {
}
