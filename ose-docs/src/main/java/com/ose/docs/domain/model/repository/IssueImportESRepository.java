package com.ose.docs.domain.model.repository;

import com.ose.docs.entity.IssueImportFileES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IssueImportESRepository extends ElasticsearchRepository<IssueImportFileES, String> {
}
