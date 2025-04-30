package com.ose.docs.domain.model.repository;

import com.ose.docs.entity.NdtInspectorCertificateES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface NdtInspectorCertificateESRepository extends ElasticsearchRepository<NdtInspectorCertificateES, String> {
}
