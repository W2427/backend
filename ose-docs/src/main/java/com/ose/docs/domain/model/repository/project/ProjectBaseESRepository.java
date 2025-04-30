package com.ose.docs.domain.model.repository.project;

import com.ose.docs.entity.project.ProjectBaseES;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
interface ProjectBaseESRepository<T extends ProjectBaseES> extends ElasticsearchRepository<T, String> {

    Page<T> findByOrgIdAndProjectIdOrderByCommittedAtDesc(String orgId, String projectId, Pageable pageable);

}
