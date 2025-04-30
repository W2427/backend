package com.ose.docs.domain.model.repository;

import com.ose.docs.entity.IssueAttachmentES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface IssueAttachmentRepository extends ElasticsearchRepository<IssueAttachmentES, String> {
}
